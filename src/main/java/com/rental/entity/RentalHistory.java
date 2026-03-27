package com.rental.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "RentalHistory")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentalHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HistoryID")
    private Integer historyId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BookingID", unique = true)
    private Booking booking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CustomerID")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VehicleID")
    private Vehicle vehicle;

    @Column(name = "ActualStartDate")
    private LocalDateTime actualStartDate;

    @Column(name = "ActualEndDate")
    private LocalDateTime actualEndDate;

    @Column(name = "PricePerDayAtBooking", precision = 15, scale = 2)
    private BigDecimal pricePerDayAtBooking;

    @Column(name = "TotalPriceAtBooking", precision = 15, scale = 2)
    private BigDecimal totalPriceAtBooking;

    @Column(name = "PenaltyFee", precision = 15, scale = 2)
    private BigDecimal penaltyFee = BigDecimal.ZERO;

    @Column(name = "DamageFee", precision = 15, scale = 2)
    private BigDecimal damageFee = BigDecimal.ZERO;

    @Column(name = "TotalFinalPrice", precision = 15, scale = 2)
    private BigDecimal totalFinalPrice;
}
