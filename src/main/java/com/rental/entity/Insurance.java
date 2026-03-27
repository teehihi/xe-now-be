package com.rental.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "Insurance")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Insurance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "InsuranceID")
    private Integer insuranceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VehicleID")
    private Vehicle vehicle;

    @Column(name = "Provider", length = 100)
    private String provider;

    @Column(name = "PolicyNumber", length = 50)
    private String policyNumber;

    @Column(name = "StartDate")
    private LocalDate startDate;

    @Column(name = "ExpiryDate")
    private LocalDate expiryDate;
}
