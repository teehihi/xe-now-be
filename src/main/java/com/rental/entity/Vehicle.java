package com.rental.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Vehicle")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {

    public enum Status {
        Available, Rented, Maintenance, Broken
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VehicleID")
    private Integer vehicleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TypeID")
    private VehicleType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CurrentLocationID")
    private Location currentLocation;

    @Column(name = "LicensePlate", nullable = false, unique = true, length = 20)
    private String licensePlate;

    @Column(name = "Name", nullable = false, length = 100)
    private String name;

    @Column(name = "Brand", length = 50)
    private String brand;

    @Column(name = "ManufactureYear")
    private Integer manufactureYear;
    
    // Getter methods for compatibility
    public String getModel() {
        return name;
    }
    
    public Integer getYearMade() {
        return manufactureYear;
    }
    
    public String getColor() {
        return "N/A"; // Default value, add color field if needed
    }
    
    public BigDecimal getDailyRate() {
        return pricePerDay;
    }

    @Column(name = "Mileage")
    @Builder.Default
    private Integer mileage = 0;

    @Column(name = "PricePerDay", nullable = false, precision = 15, scale = 2)
    private BigDecimal pricePerDay;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status")
    @Builder.Default
    private Status status = Status.Available;

    @Column(name = "AverageRating")
    @Builder.Default
    private Float averageRating = 0f;

    @Column(name = "TotalReviews")
    @Builder.Default
    private Integer totalReviews = 0;

    @Column(name = "DeletedAt")
    private LocalDateTime deletedAt;
}
