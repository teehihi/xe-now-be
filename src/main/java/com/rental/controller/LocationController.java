package com.rental.controller;

import com.rental.dto.ApiResponse;
import com.rental.entity.Location;
import com.rental.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationRepository locationRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Location>>> getAll() {
        List<Location> locations = locationRepository.findAll();
        return ResponseEntity.ok(ApiResponse.success(locations, "Lấy danh sách địa điểm thành công"));
    }
}
