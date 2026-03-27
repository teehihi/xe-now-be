package com.rental.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Coupon")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CouponID")
    private Integer couponId;

    @Column(name = "Code", nullable = false, unique = true, length = 20)
    private String code;

    @Column(name = "DiscountPercent", nullable = false)
    private Integer discountPercent = 0;

    @Column(name = "MinBookingValue", precision = 15, scale = 2)
    private BigDecimal minBookingValue = BigDecimal.ZERO;

    @Column(name = "ExpiryDate", nullable = false)
    private LocalDate expiryDate;

    @Column(name = "DeletedAt")
    private LocalDateTime deletedAt;
}
