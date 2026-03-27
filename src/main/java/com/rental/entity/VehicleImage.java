package com.rental.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "VehicleImage")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ImageID")
    private Integer imageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VehicleID")
    private Vehicle vehicle;

    @Column(name = "ImageURL", nullable = false, length = 255)
    private String imageUrl;

    @Column(name = "IsPrimary")
    private Boolean isPrimary = false;
}
