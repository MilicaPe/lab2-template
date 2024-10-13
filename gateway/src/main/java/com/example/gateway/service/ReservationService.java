package com.example.gateway.service;

import com.example.gateway.dto.ReservationResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Service
public class ReservationService {
    @Value("${reservation.service.url}")
    private String basicReservation;

    @Autowired
    PaymentService paymentService;



    public Object getUserReservationsByUid(String username, String reservationUid) throws URISyntaxException {
        URI uri = new URI(this.basicReservation.toString() + "/reservation/"+reservationUid + "/"+username);
        System.out.println(uri.toString());
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<Object> result = restTemplate.exchange(uri, HttpMethod.GET, entity, Object.class);
        try {
            ReservationResponseDTO r = (ReservationResponseDTO) result.getBody();
            r.setPayment(this.paymentService.getPaymentInfo(r.getPaymentUid()));
            return r;
        }catch (Exception e){
            return result;
        }
    }

}
