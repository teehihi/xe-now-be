package com.rental.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Booking")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    public enum Status {
        Pending, Confirmed, PickedUp, Ongoing, Returned, Cancelled
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BookingID")
    private Integer bookingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CustomerID", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VehicleID", nullable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ApprovedBy")
    private User approvedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PickupLocationID")
    private Location pickupLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ReturnLocationID")
    private Location returnLocation;

    @Column(name = "StartDate", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "EndDate", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "TotalPrice", precision = 15, scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "DepositAmount", precision = 15, scale = 2)
    private BigDecimal depositAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status")
    @Builder.Default
    private Status status = Status.Pending;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CouponID")
    private Coupon coupon;

    @Column(name = "DeletedAt")
    private LocalDateTime deletedAt;
}
