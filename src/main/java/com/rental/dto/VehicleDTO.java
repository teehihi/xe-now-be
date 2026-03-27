package com.rental.dto;

import com.rental.entity.Vehicle;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class VehicleDTO {
    private Integer vehicleId;
    private String licensePlate;
    private String name;
    private String model; // Added for compatibility
    private String brand;
    private Integer manufactureYear;
    private Integer yearMade; // Added for compatibility
    private String color; // Added for compatibility
    private Integer mileage;
    private BigDecimal pricePerDay;
    private BigDecimal dailyRate; // Added for compatibility
    private Vehicle.Status status;
    private String typeName;
    private String locationName;
    private Float averageRating;
}
