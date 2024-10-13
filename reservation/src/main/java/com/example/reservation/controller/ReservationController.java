package com.example.reservation.controller;

import com.example.reservation.dto.ReservationResponseDTO;
import com.example.reservation.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    @GetMapping(value = "/reservation/{username}")
    public ResponseEntity<List<ReservationResponseDTO>> getReservationForUser(@PathVariable String username){
        List<ReservationResponseDTO> result = this.reservationService.getReservationsByUser(username);
        return ResponseEntity.status(200).body(result);
    }
}
