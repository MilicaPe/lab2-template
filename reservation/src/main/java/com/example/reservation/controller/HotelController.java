package com.example.reservation.controller;

import com.example.reservation.dto.HotelResponseDTO;
import com.example.reservation.dto.PaginationResponseDTO;
import com.example.reservation.model.Hotel;
import com.example.reservation.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping
public class HotelController {

    @Autowired
    private HotelRepository hotelRepository;

    @GetMapping(value = "/hotels/{page}/{row}")
    private ResponseEntity<PaginationResponseDTO> getAllHotels(@PathVariable int page, @PathVariable int row){
        System.out.println("Stigeo na reservation service");
        List<Hotel> hotels = this.hotelRepository.findAll(PageRequest.of(page, row)).getContent();
        PaginationResponseDTO paginationResponseDTO = getHotelsPagination(page, row, hotels);
        return ResponseEntity.status(200).body(paginationResponseDTO);
    }

    private PaginationResponseDTO getHotelsPagination(int page, int row, List<Hotel> hotels){
        List<HotelResponseDTO> hotelResponseDTOS = new ArrayList<>();
        for(Hotel hotel: hotels){
            hotelResponseDTOS.add(new HotelResponseDTO(hotel));
        }
        return new PaginationResponseDTO(page, row, hotelResponseDTOS.size(), Collections.singletonList(hotelResponseDTOS));
    }



}
