package com.rental.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Contract")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ContractID")
    private Integer contractId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BookingID", unique = true)
    private Booking booking;

    @Column(name = "SignedDate")
    private LocalDateTime signedDate;

    @Column(name = "Terms", columnDefinition = "TEXT")
    private String terms;
}
