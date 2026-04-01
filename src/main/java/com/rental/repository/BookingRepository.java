package com.rental.repository;

import com.rental.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findByCustomer_UserIdOrderByStartDateDesc(Integer userId);
    Page<Booking> findByCustomer_UserIdOrderByStartDateDesc(Integer userId, Pageable pageable);
    
    List<Booking> findByStatus(Booking.Status status);
    Page<Booking> findByStatus(Booking.Status status, Pageable pageable);


    List<Booking> findByVehicleVehicleId(Integer vehicleId);
    
    @org.springframework.data.jpa.repository.Query("SELECT b FROM Booking b WHERE b.vehicle.vehicleId = :vehicleId " +
           "AND b.status IN :statuses " +
           "AND b.startDate < :endDate AND b.endDate > :startDate")
    List<Booking> findOverlappingBookings(
        @org.springframework.data.repository.query.Param("vehicleId") Integer vehicleId, 
        @org.springframework.data.repository.query.Param("startDate") java.time.LocalDateTime startDate, 
        @org.springframework.data.repository.query.Param("endDate") java.time.LocalDateTime endDate,
        @org.springframework.data.repository.query.Param("statuses") List<Booking.Status> statuses);

    List<Booking> findAllByOrderByStartDateDesc();
    Page<Booking> findAllByOrderByStartDateDesc(Pageable pageable);
}

