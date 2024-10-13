package com.example.gateway.controller;

import com.example.gateway.dto.UserInfoResponseDTO;
import com.example.gateway.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;

@RestController
@RequestMapping
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/me")
    public ResponseEntity<UserInfoResponseDTO> getLoyaltyInfoResponseForUser(@RequestHeader("X-User-Name") String username) throws URISyntaxException {
        UserInfoResponseDTO response = this.userService.getUserInfo(username);
        return ResponseEntity.status(200).body(response);

    }
}
