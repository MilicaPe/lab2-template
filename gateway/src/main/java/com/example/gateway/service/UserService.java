package com.example.gateway.service;

import com.example.gateway.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Value("${reservation.service.url}")
    private String basicReservation;

    @Value("${loyalty.service.url}")
    private String basicLoyalty;

    @Autowired
    PaymentService paymentService;

    public UserInfoResponseDTO getUserInfo(String username) throws URISyntaxException {
        System.out.println(" Stigao na gateway");
        LoyaltyInfoResponseDTO loyalty = getLoyaltyForUser(username);
        List<ReservationResponseDTO> reservations = getReservationsDTOForUser(username);
        return new UserInfoResponseDTO(reservations, loyalty);
    }

    public List<ReservationResponseDTO> getReservationsDTOForUser(String username) throws URISyntaxException {
        List<ReservationResponseDTO> reservations = getReservationsForUser(username);
        this.setPaymentForReservations(reservations);
        return reservations;
    }

    public LoyaltyInfoResponseDTO getLoyaltyForUser(String username) throws URISyntaxException {
        URI uri = new URI(this.basicLoyalty.toString() + "/loyalty/"+username);
        System.out.println(uri.toString());
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<LoyaltyInfoResponseDTO> result = restTemplate.exchange(uri, HttpMethod.GET, entity, LoyaltyInfoResponseDTO.class);
        System.out.println(result.getBody());
        System.out.println(result);
        return result.getBody();
    }


    private List<ReservationResponseDTO> getReservationsForUser(String username) throws URISyntaxException {
        URI uri = new URI(this.basicReservation.toString() + "/reservation/"+username);
        System.out.println(uri.toString());
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<ArrayList<ReservationResponseDTO>> result = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {});
        System.out.println(result.getBody());
        System.out.println(result);
        return result.getBody();
    }

    private void setPaymentForReservations(List<ReservationResponseDTO> reservations) throws URISyntaxException {
        for(ReservationResponseDTO r: reservations){
            r.setPayment(paymentService.getPaymentInfo(r.getPaymentUid()));
        }
    }

}
