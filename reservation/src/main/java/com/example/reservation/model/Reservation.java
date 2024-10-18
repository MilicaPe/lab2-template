package com.example.reservation.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="reservation")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)  // ????? UUID
    private UUID reservationUid;

    @Column(nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private UUID paymentUid;

    @ManyToOne
    @JoinColumn(name="hotel_id")
    private Hotel hotel;

    @Column(nullable = false)
    private Status status;

    @Column
    private LocalDate startDate;

    @Column
    private LocalDate endDate;

}
