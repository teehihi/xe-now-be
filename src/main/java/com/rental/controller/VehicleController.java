package com.rental.controller;

import com.rental.dto.ApiResponse;
import com.rental.dto.VehicleDTO;
import com.rental.dto.VehicleImageDTO;
import com.rental.entity.Vehicle;
import com.rental.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<VehicleDTO>>> getAll(
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("vehicleId").descending());
        Page<Vehicle> vehicles;
        
        if (type != null && !type.isEmpty()) {
            vehicles = vehicleService.getAvailableByType(type, pageable);
        } else {
            vehicles = vehicleService.getAvailableVehicles(pageable);
        }
        
        Page<VehicleDTO> dtos = vehicles.map(this::convertToDTO);
        
        return ResponseEntity.ok(ApiResponse.success(dtos, "Lấy danh sách xe trống thành công"));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VehicleDTO>> getById(@PathVariable Integer id) {
        Vehicle vehicle = vehicleService.getById(id);
        if (vehicle == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.notFound("Không tìm thấy xe với mã: " + id));
        }
        return ResponseEntity.ok(ApiResponse.success(convertToDTO(vehicle), "Lấy thông tin xe thành công"));
    }

    private VehicleDTO convertToDTO(Vehicle vehicle) {
        VehicleDTO dto = new VehicleDTO();
        
        // IDs
        dto.setId(vehicle.getVehicleId());
        dto.setVehicleId(vehicle.getVehicleId());
        
        // Basic info
        dto.setLicensePlate(vehicle.getLicensePlate());
        // Dynamic name generation
        dto.setName(vehicle.getName());
        dto.setModelName(vehicle.getModelName());
        dto.setBrandName(vehicle.getBrandName());
        dto.setModel(vehicle.getModelName()); // For compatibility
        dto.setBrand(vehicle.getBrandName()); // For compatibility
        
        if (vehicle.getModel() != null) {
            dto.setModelId(vehicle.getModel().getModelId());
        }
        
        // Year
        dto.setYear(vehicle.getManufactureYear());
        dto.setManufactureYear(vehicle.getManufactureYear());
        
        // Pricing and status
        dto.setPricePerDay(vehicle.getPricePerDay());
        dto.setDailyRate(vehicle.getPricePerDay());
        dto.setStatus(vehicle.getStatus());
        dto.setMileage(vehicle.getMileage());
        dto.setAverageRating(vehicle.getAverageRating());
        
        // Type
        if (vehicle.getType() != null) {
            dto.setType(vehicle.getType());
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
        
        // Images from database
        if (vehicle.getImages() != null && !vehicle.getImages().isEmpty()) {
            List<VehicleImageDTO> imgDTOs = vehicle.getImages().stream()
                .map(img -> VehicleImageDTO.builder()
                    .imageId(img.getImageId())
                    .imageUrl(img.getImageUrl())
                    .isPrimary(Boolean.TRUE.equals(img.getIsPrimary()))
                    .build())
                .collect(Collectors.toList());
            dto.setImages(imgDTOs);
            
            // Set primary image URL for main display
            String primaryUrl = imgDTOs.stream()
                .filter(img -> Boolean.TRUE.equals(img.getIsPrimary()))
                .map(VehicleImageDTO::getImageUrl)
                .findFirst()
                .orElse(imgDTOs.isEmpty() ? "/images/car-toyota-camry.webp" : imgDTOs.get(0).getImageUrl());
            dto.setImage(primaryUrl);
        } else {
            dto.setImage("/images/car-toyota-camry.webp");
        }
        
        return dto;
    }
}
