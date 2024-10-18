package com.example.gateway.service;

import com.example.gateway.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Service
public class ReservationService {
    @Value("${reservation.service.url}")
    private String basicReservation;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private LoyaltyService loyaltyService;

    private final String paid = "PAID";
    private final String canceled = "CANCELED";



    public Object getUserReservationsByUid(String username, String reservationUid) throws URISyntaxException {
        URI uri = new URI(this.basicReservation.toString() + "/reservation/"+reservationUid);  // + "/"+username);
        System.out.println(uri.toString());
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(headers);
        headers.add("X-User-Name", username);   /// proveri da li radi
        ResponseEntity<ReservationResponseDTO> result = restTemplate.exchange(uri, HttpMethod.GET, entity, ReservationResponseDTO.class);
//        if result.getStatusCode()==200{}
        try {
            ReservationResponseDTO r = (ReservationResponseDTO) result.getBody();
            r.setPayment(this.paymentService.getPaymentInfo(r.getPaymentUid()));
            return r;
        }catch (Exception e){
            return result;
        }
    }

    public CreateReservationResponse makeReservation(String username, CreateReservationRequest request) throws URISyntaxException {
        if (!hotelService.isHotelExist(request.getHotelUid())){
           // throw new ObjectNotFoundException("Not Found", new ErrorResponse());
            return null;
        }
        int numOfNights = countNumberOfNights(request);
        Double pricePerNight = hotelService.getHotelPrice(request.getHotelUid());
        double fullPrice = numOfNights * pricePerNight;
        LoyaltyInfoResponseDTO loyalty = loyaltyService.getLoyaltyForUser(username);
        int discount = loyaltyService.getDiscountForStatus(loyalty.getStatus());
        int endPrice = (int) (fullPrice - (fullPrice * discount / 100));
        PaymentInfoDTO newPaymentInfo = new PaymentInfoDTO(paid, endPrice);
        String paymentUid = this.paymentService.saveNewPayment(newPaymentInfo);

        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setPaymentUid(paymentUid);
        reservationDTO.setHotelUid(request.getHotelUid());
        reservationDTO.setStartDate(request.getStartDate());
        reservationDTO.setEndDate(request.getEndDate());
        reservationDTO.setStatus(paid);

        CreateReservationResponse response = saveReservation(reservationDTO, username);
        response.setPayment(newPaymentInfo);
        response.setDiscount(discount);

        LoyaltyInfoResponseDTO loyaltyInfo = loyaltyService.addNewBooking(username);

        return response;
    }
    private int countNumberOfNights(CreateReservationRequest request){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(request.getStartDate(), formatter);
        LocalDate endDate = LocalDate.parse(request.getEndDate(), formatter);
        int numOfNights = (int) ChronoUnit.DAYS.between(startDate, endDate);
        return numOfNights;
    }

    private CreateReservationResponse saveReservation(ReservationDTO reservationDTO, String username) throws URISyntaxException {
        URI uri = new URI(this.basicReservation.toString() + "/reservation");
        System.out.println(uri.toString());
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-User-Name", username);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(reservationDTO, headers);
        ResponseEntity<CreateReservationResponse> result = restTemplate.exchange(uri, HttpMethod.POST, entity, CreateReservationResponse.class);
        return result.getBody();
    }

    public boolean deleteReservation(String username, String reservationUid) throws URISyntaxException {
        ReservationDTO r = deleteReservationForUser(username, reservationUid);
        if (r == null){
            System.out.println("reservation " + r);
            return false;
        }
        this.paymentService.deletePayment(r.getPaymentUid());
        this.loyaltyService.subtractBooking(username);
        return true;
    }

    private ReservationDTO deleteReservationForUser(String username, String reservationUid) throws URISyntaxException {
        URI uri = new URI(this.basicReservation.toString() + "/reservation/" + reservationUid);
        System.out.println(uri.toString());
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-User-Name", username);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<ReservationDTO> result = restTemplate.exchange(uri, HttpMethod.DELETE, entity, ReservationDTO.class);
        System.out.println(result.getBody());
        return result.getBody();
    }
}
