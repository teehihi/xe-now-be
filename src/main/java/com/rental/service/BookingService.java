package com.rental.service;

import com.rental.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface BookingService {


    List<Booking> getAllBookings();
    Page<Booking> getAllBookings(Pageable pageable);
    List<Booking> getBookingsByCustomer(Integer userId);
    Page<Booking> getBookingsByCustomer(Integer userId, Pageable pageable);
    List<Booking> getBookingsByStatus(Booking.Status status);
    Page<Booking> getBookingsByStatus(Booking.Status status, Pageable pageable);
    Booking getById(Integer id);
    Booking createBooking(Booking booking);
    Booking updateStatus(Integer bookingId, Booking.Status newStatus, Integer mileage, String note);
    Booking updateStatus(Integer bookingId, Booking.Status newStatus);
}

