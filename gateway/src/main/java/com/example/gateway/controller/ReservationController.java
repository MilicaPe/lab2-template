package com.example.gateway.controller;


import com.example.gateway.dto.ReservationResponseDTO;
import com.example.gateway.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

@RestController
@RequestMapping
public class ReservationController {

    @Autowired
    ReservationService reservationService;

    @GetMapping(value = "/reservations/{reservationUid}")
    public ResponseEntity<?> getUserReservationsByUid (@RequestHeader("X-User-Name") String username, @PathVariable String reservationUid) throws URISyntaxException {
        Object result = this.reservationService.getUserReservationsByUid(username, reservationUid);
        try {
            ReservationResponseDTO r = (ReservationResponseDTO) result;
            return ResponseEntity.status(200).body(r);
        }catch (Exception e) {
            return ResponseEntity.status(404).body(result);
        }
    }

}
