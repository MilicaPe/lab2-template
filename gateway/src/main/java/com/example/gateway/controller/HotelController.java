package com.example.gateway.controller;

import com.example.gateway.dto.PaginationResponseDTO;
import com.example.gateway.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;

@RestController
@RequestMapping
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @GetMapping(value = "/hotels/{page}/{row}")
    private ResponseEntity<?> getAllHotels(@PathVariable int page, @PathVariable int row) {
        try {
            PaginationResponseDTO response = this.hotelService.getHotels(page, row);
            return ResponseEntity.status(200).body(response);
        }catch (URISyntaxException e){
            System.out.println(" POGRESNA putanja");
            return ResponseEntity.status(400).build();
        }
    }


}
