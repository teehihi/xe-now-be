package com.rental.service;

import com.rental.entity.Booking;
import com.rental.entity.Vehicle;
import com.rental.repository.BookingRepository;
import com.rental.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final VehicleRepository vehicleRepository;

    public List<Booking> getAllBookings() {
        return bookingRepository.findAllByOrderByStartDateDesc();
    }

    public List<Booking> getBookingsByCustomer(Integer userId) {
        return bookingRepository.findByCustomerUserId(userId);
    }

    public List<Booking> getBookingsByStatus(Booking.Status status) {
        return bookingRepository.findByStatus(status);
    }

    public Booking getById(Integer id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn đặt xe với ID: " + id));
    }

    @Transactional
    public Booking createBooking(Booking booking) {
        long days = ChronoUnit.DAYS.between(booking.getStartDate(), booking.getEndDate());
        if (days <= 0) throw new IllegalArgumentException("Ngày trả phải sau ngày nhận xe");

        BigDecimal pricePerDay = booking.getVehicle().getPricePerDay();
        booking.setTotalPrice(pricePerDay.multiply(BigDecimal.valueOf(days)));
        booking.setStatus(Booking.Status.Pending);
        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking updateStatus(Integer bookingId, Booking.Status newStatus) {
        Booking booking = getById(bookingId);
        booking.setStatus(newStatus);

        Vehicle vehicle = booking.getVehicle();
        if (newStatus == Booking.Status.PickedUp) {
            vehicle.setStatus(Vehicle.Status.Rented);
            vehicleRepository.save(vehicle);
        } else if (newStatus == Booking.Status.Returned || newStatus == Booking.Status.Cancelled) {
            vehicle.setStatus(Vehicle.Status.Available);
            vehicleRepository.save(vehicle);
        }

        return bookingRepository.save(booking);
    }
}
