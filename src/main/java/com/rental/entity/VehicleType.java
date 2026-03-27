package com.rental.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "VehicleType")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TypeID")
    private Integer typeId;

    @Column(name = "TypeName", nullable = false, unique = true, length = 50)
    private String typeName;

    @Column(name = "DeletedAt")
    private LocalDateTime deletedAt;
}
