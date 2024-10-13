package com.example.reservation.controller;

import com.example.reservation.dto.ErrorResponse;
import com.example.reservation.dto.ReservationResponseDTO;
import com.example.reservation.service.ReservationService;
import org.hibernate.ObjectNotFoundException;
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
    public ResponseEntity<List<ReservationResponseDTO>> getAllReservationsForUser(@PathVariable String username){
        List<ReservationResponseDTO> result = this.reservationService.getReservationsByUser(username);
        return ResponseEntity.status(200).body(result);
    }

    @GetMapping(value = "/reservation/{reservationUid}/{username}")
    public ResponseEntity <?> getReservationForUser(@PathVariable String reservationUid, @PathVariable String username){
        try {
            ReservationResponseDTO result = this.reservationService.getReservationByUser(reservationUid, username);
            return ResponseEntity.status(200).body(result);
        }catch (ObjectNotFoundException e){
            return  ResponseEntity.status(404).body(new ErrorResponse("User " + username + " doesnt have reservation " + reservationUid));
        }

    }
}
