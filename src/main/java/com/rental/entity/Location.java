package com.rental.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Location")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LocationID")
    private Integer locationId;

    @Column(name = "BranchName", nullable = false, length = 100)
    private String branchName;

    @Column(name = "Address", length = 255)
    private String address;

    @Column(name = "City", length = 50)
    private String city;

    @Column(name = "Phone", length = 20)
    private String phone;

    @Column(name = "DeletedAt")
    private LocalDateTime deletedAt;
}
