package com.rental.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "Role")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RoleID")
    private Integer roleId;

    @Column(name = "RoleName", nullable = false, unique = true, length = 50)
    private String roleName;

    @Column(name = "DeletedAt")
    private LocalDateTime deletedAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "RolePermission",
        joinColumns = @JoinColumn(name = "RoleID"),
        inverseJoinColumns = @JoinColumn(name = "PermissionID")
    )
    private Set<Permission> permissions;
}
