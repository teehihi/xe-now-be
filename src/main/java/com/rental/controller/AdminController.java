package com.rental.controller;

import com.rental.entity.Booking;
import com.rental.entity.Customer;
import com.rental.entity.Vehicle;
import com.rental.dto.*;
import com.rental.service.BookingService;
import com.rental.service.CustomerService;
import com.rental.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final BookingService bookingService;
    private final VehicleService vehicleService;
    private final CustomerService customerService;

    @GetMapping("/dashboard")
    public DashboardStatsDTO dashboard() {
        long totalVehicles = vehicleService.getAllVehicles().size();
        long availableVehicles = vehicleService.getAvailableVehicles().size();
        long pendingBookings = bookingService.getBookingsByStatus(Booking.Status.Pending).size();
        long ongoingBookings = bookingService.getBookingsByStatus(Booking.Status.Ongoing).size();

        return DashboardStatsDTO.builder()
                .totalVehicles(totalVehicles)
                .availableVehicles(availableVehicles)
                .pendingBookings(pendingBookings)
                .ongoingBookings(ongoingBookings)
                .recentBookings(bookingService.getAllBookings().stream()
                        .map(this::convertToBookingDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    @GetMapping("/bookings")
    public List<BookingDTO> allBookings() {
        return bookingService.getAllBookings().stream()
                .map(this::convertToBookingDTO)
                .collect(Collectors.toList());
    }

    @PostMapping("/bookings/{id}/status")
    public ResponseEntity<?> updateBookingStatus(@PathVariable Integer id,
                                               @RequestParam String status) {
        try {
            bookingService.updateStatus(id, Booking.Status.valueOf(status));
            return ResponseEntity.ok("Cập nhật trạng thái thành công!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/vehicles")
    public List<VehicleDTO> allVehicles() {
        return vehicleService.getAllVehicles().stream()
                .map(this::convertToVehicleDTO)
                .collect(Collectors.toList());
    }

    @PostMapping("/vehicles/{id}/status")
    public ResponseEntity<?> updateVehicleStatus(@PathVariable Integer id,
                                               @RequestParam String status) {
        try {
            vehicleService.updateStatus(id, Vehicle.Status.valueOf(status));
            return ResponseEntity.ok("Cập nhật trạng thái xe thành công!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/customers")
    public List<CustomerDTO> allCustomers() {
        return customerService.getAll().stream()
                .map(this::convertToCustomerDTO)
                .collect(Collectors.toList());
    }

    private BookingDTO convertToBookingDTO(Booking booking) {
        BookingDTO dto = new BookingDTO();
        dto.setBookingId(booking.getBookingId());
        dto.setVehicleId(booking.getVehicle().getVehicleId());
        dto.setVehicleModel(booking.getVehicle().getModel());
        dto.setCustomerName(booking.getCustomer().getName());
        dto.setStartDate(booking.getStartDate());
        dto.setEndDate(booking.getEndDate());
        dto.setTotalPrice(booking.getTotalPrice());
        dto.setStatus(booking.getStatus());
        return dto;
    }

    private VehicleDTO convertToVehicleDTO(Vehicle vehicle) {
        VehicleDTO dto = new VehicleDTO();
        dto.setVehicleId(vehicle.getVehicleId());
        dto.setLicensePlate(vehicle.getLicensePlate());
        dto.setBrand(vehicle.getBrand());
        dto.setModel(vehicle.getModel());
        dto.setDailyRate(vehicle.getDailyRate());
        dto.setStatus(vehicle.getStatus());
        return dto;
    }

    private CustomerDTO convertToCustomerDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setCustomerId(customer.getCustomerId());
        dto.setName(customer.getName());
        dto.setEmail(customer.getEmail());
        dto.setPhone(customer.getPhone());
        return dto;
    }
}
