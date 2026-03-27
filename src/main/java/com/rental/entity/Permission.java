package com.rental.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Permission")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PermissionID")
    private Integer permissionId;

    @Column(name = "PermissionName", nullable = false, unique = true, length = 100)
    private String permissionName;
}
