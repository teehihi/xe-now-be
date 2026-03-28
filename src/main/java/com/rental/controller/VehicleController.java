package com.rental.controller;

import com.rental.entity.Vehicle;
import com.rental.dto.VehicleDTO;
import com.rental.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @GetMapping
    public List<VehicleDTO> getAll(@RequestParam(required = false) Integer typeId) {
        return vehicleService.getAvailableByType(typeId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleDTO> getById(@PathVariable Integer id) {
        Vehicle vehicle = vehicleService.getById(id);
        if (vehicle == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(convertToDTO(vehicle));
    }

    private VehicleDTO convertToDTO(Vehicle vehicle) {
        VehicleDTO dto = new VehicleDTO();
        
        // IDs
        dto.setId(vehicle.getVehicleId());
        dto.setVehicleId(vehicle.getVehicleId());
        
        // Basic info
        dto.setLicensePlate(vehicle.getLicensePlate());
        dto.setName(vehicle.getName());
        dto.setModel(vehicle.getModel());
        dto.setBrand(vehicle.getBrand());
        
        // Year
        dto.setYear(vehicle.getManufactureYear());
        dto.setManufactureYear(vehicle.getManufactureYear());
        dto.setYearMade(vehicle.getManufactureYear());
        
        // Pricing and status
        dto.setPricePerDay(vehicle.getPricePerDay());
        dto.setDailyRate(vehicle.getPricePerDay());
        dto.setStatus(vehicle.getStatus());
        dto.setMileage(vehicle.getMileage());
        dto.setAverageRating(vehicle.getAverageRating());
        
        // Type
        if (vehicle.getType() != null) {
            String typeName = vehicle.getType().getTypeName();
            dto.setType(typeName);
            dto.setTypeName(typeName);
            
            // Determine category based on type (case-insensitive and trim whitespace)
            String typeNameLower = typeName.toLowerCase().trim();
            if (typeNameLower.contains("tay ga") || typeNameLower.contains("số") || 
                typeNameLower.equals("xe tay ga") || typeNameLower.equals("xe số")) {
                dto.setCategory("motorcycle");
            } else {
                dto.setCategory("car");
            }
        } else {
            // Default to car if no type
            dto.setCategory("car");
        }
        
        // Vehicle specs from database
        dto.setSeats(vehicle.getSeats());
        dto.setFuel(vehicle.getFuelType());
        dto.setTransmission(vehicle.getTransmission());
        
        // Location
        if (vehicle.getCurrentLocation() != null) {
            String locationName = vehicle.getCurrentLocation().getBranchName();
            dto.setLocation(locationName);
            dto.setLocationName(locationName);
        }
        
        // Image - placeholder for now
        dto.setImage("/images/car-toyota-camry.webp");
        
        return dto;
    }
}
