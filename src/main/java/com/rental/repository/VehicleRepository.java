package com.rental.repository;

import com.rental.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {
    List<Vehicle> findByStatus(Vehicle.Status status);
    Page<Vehicle> findByStatus(Vehicle.Status status, Pageable pageable);
    
    List<Vehicle> findByType(String type);
    Page<Vehicle> findByType(String type, Pageable pageable);

    List<Vehicle> findByCurrentLocationLocationId(Integer locationId);
    long countByCurrentLocationLocationId(Integer locationId);

    @Query("SELECT v FROM Vehicle v WHERE v.status = 'Available' AND (:type IS NULL OR v.type = :type)")
    List<Vehicle> findAvailableByType(String type);

    @Query("SELECT v FROM Vehicle v WHERE v.status = 'Available' AND (:type IS NULL OR v.type = :type)")
    Page<Vehicle> findAvailableByType(String type, Pageable pageable);
}

