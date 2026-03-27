package com.rental.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Payment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    public enum Method {
        Cash, CreditCard, BankTransfer, EWallet
    }

    public enum Status {
        Pending, Completed, Failed, Refunded
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PaymentID")
    private Integer paymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BookingID")
    private Booking booking;

    @Column(name = "Amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "PaymentDate")
    private LocalDateTime paymentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "PaymentMethod")
    private Method paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status")
    private Status status = Status.Pending;

    @Column(name = "TransactionCode", length = 100)
    private String transactionCode;

    @PrePersist
    protected void onCreate() {
        paymentDate = LocalDateTime.now();
    }
}
