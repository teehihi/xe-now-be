package com.rental.dto;

import com.rental.entity.Vehicle;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class VehicleDTO {
    private Integer vehicleId;
    private String licensePlate;
    private String brand;
    private String model;
    private Integer yearMade;
    private String color;
    private BigDecimal dailyRate;
    private Vehicle.Status status;
    private String typeName;
}
