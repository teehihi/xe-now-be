package com.rental.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Staff")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Staff {
    @Id
    @Column(name = "UserID")
    private Integer userId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "UserID")
    private User user;
}
