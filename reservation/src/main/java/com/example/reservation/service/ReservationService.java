package com.example.reservation.service;

import com.example.reservation.dto.ErrorResponse;
import com.example.reservation.dto.HotelInfoDTO;
import com.example.reservation.dto.ReservationResponseDTO;
import com.example.reservation.model.Reservation;
import com.example.reservation.repository.ReservationRepository;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;


    public List<ReservationResponseDTO> getReservationsByUser(String username) {
        List<ReservationResponseDTO> responseDTOS = new ArrayList<>();
        List<Reservation> reservations = this.reservationRepository.getAllByUsername(username);
        for(Reservation r : reservations){
            HotelInfoDTO hotelInfoDTO = new HotelInfoDTO(r.getHotel());
            responseDTOS.add(new ReservationResponseDTO(r.getReservationUid().toString(),
                    hotelInfoDTO, r.getStartDate().toString(), r.getEndDate().toString(), r.getPaymentUid().toString()));
        }
        return responseDTOS;
    }

    public ReservationResponseDTO getReservationByUser(String reservationUid, String username) {
        Reservation r = this.reservationRepository.getReservationByUsernameAndReservationUid(username, UUID.fromString(reservationUid));
        if (r==null){
         throw new ObjectNotFoundException("Not Found", new ErrorResponse());
        }
        else {
            HotelInfoDTO hotelInfoDTO = new HotelInfoDTO(r.getHotel());
            ReservationResponseDTO responseDTO = new ReservationResponseDTO(r.getReservationUid().toString(),
                    hotelInfoDTO, r.getStartDate().toString(), r.getEndDate().toString(), r.getPaymentUid().toString());
            return responseDTO;
        }
    }
}
