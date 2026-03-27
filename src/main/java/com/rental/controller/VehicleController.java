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
        dto.setVehicleId(vehicle.getVehicleId());
        dto.setLicensePlate(vehicle.getLicensePlate());
        dto.setBrand(vehicle.getBrand());
        dto.setModel(vehicle.getModel());
        dto.setYearMade(vehicle.getYearMade());
        dto.setColor(vehicle.getColor());
        dto.setDailyRate(vehicle.getDailyRate());
        dto.setStatus(vehicle.getStatus());
        if (vehicle.getType() != null) {
            dto.setTypeName(vehicle.getType().getTypeName());
        }
        return dto;
    }
}
