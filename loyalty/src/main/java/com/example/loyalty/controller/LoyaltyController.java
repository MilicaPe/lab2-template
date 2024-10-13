package com.example.loyalty.controller;

import com.example.loyalty.dto.LoyaltyInfoResponseDTO;
import com.example.loyalty.model.Loyalty;
import com.example.loyalty.repository.LoyaltyRepository;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class LoyaltyController {
    @Autowired
    private LoyaltyRepository loyaltyRepository;

    @GetMapping(value = "/loyalty/{username}")
    public ResponseEntity<LoyaltyInfoResponseDTO> getLoyaltyInfoResponseForUser(@PathVariable String username){
        Loyalty loyalty = this.loyaltyRepository.getLoyaltyByUsername(username);
        return ResponseEntity.status(200).body(new LoyaltyInfoResponseDTO(loyalty));
    }
}
