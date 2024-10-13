package com.example.payment.controller;

import com.example.payment.dto.PaymentInfoDTO;
import com.example.payment.model.Payment;
import com.example.payment.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping
public class PaymentController {
    @Autowired
    private PaymentRepository paymentRepository;
    @GetMapping(value = "/payment/{paymentUid}")
    public ResponseEntity<PaymentInfoDTO> getPaymentByPaymentUid(@PathVariable String paymentUid){
        Payment payment = this.paymentRepository.getPaymentByPaymentUid(UUID.fromString(paymentUid));
        return ResponseEntity.status(200).body(new PaymentInfoDTO(payment));
    }

}
