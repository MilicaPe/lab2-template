package com.example.gateway.controller;


import com.example.gateway.dto.CreateReservationRequest;
import com.example.gateway.dto.CreateReservationResponse;
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

    @PostMapping(value="/reservations")
    public ResponseEntity makeNewReservation(@RequestHeader("X-User-Name") String username, @RequestBody CreateReservationRequest request) throws URISyntaxException {
        CreateReservationResponse response = this.reservationService.makeReservation(username, request);
        return ResponseEntity.status(200).body(response);
    }

    @DeleteMapping(value = "/reservations/{reservationUid}")
    public ResponseEntity<?> deleteReservation(@RequestHeader("X-User-Name") String username, @PathVariable String reservationUid) throws URISyntaxException {
        if(this.reservationService.deleteReservation(username, reservationUid))
            return ResponseEntity.status(204).build();
        return ResponseEntity.status(404).build();
    }
}
