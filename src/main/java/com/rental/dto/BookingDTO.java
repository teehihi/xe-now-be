package com.rental.dto;

import com.rental.entity.Booking;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BookingDTO {
    private Integer bookingId;
    private Integer vehicleId;
    private String vehicleModel;
    private String customerName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String pickupLocationName;
    private String returnLocationName;
    private BigDecimal totalPrice;
    private Booking.Status status;
}
