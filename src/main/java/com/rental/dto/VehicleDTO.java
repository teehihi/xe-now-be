package com.rental.dto;

import com.rental.entity.Vehicle;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class VehicleDTO {
    private Integer id; // For frontend compatibility
    private Integer vehicleId;
    private String licensePlate;
    private String name;
    private String model; // Vehicle model line (e.g., SH, Exciter, CR-V, Civic)
    private String brand;
    private Integer year; // For frontend
    private Integer manufactureYear;
    private Integer yearMade; // Added for compatibility
    private String color; // Added for compatibility
    private Integer mileage;
    private BigDecimal pricePerDay;
    private BigDecimal dailyRate; // Added for compatibility
    private Vehicle.Status status;
    private String type; // Vehicle type name
    private String typeName;
    private String location; // Location name
    private String locationName;
    private Float averageRating;
    private String category; // "car" or "motorcycle"
    private Integer seats; // Number of seats
    private String fuel; // Fuel type
    private String transmission; // Transmission type
    private String image; // Image URL
}
