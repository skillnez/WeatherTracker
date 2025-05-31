package com.skillnez.weathertracker.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@Table(name = "locations")
@NoArgsConstructor
@AllArgsConstructor
public class Location implements BaseEntity<Long> {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Objects.equals(user, location.user);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(user);
    }

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn (name = "user_id", nullable = false)
    private User user;

    @Column(name = "latitude",nullable = false, precision = 8, scale = 5)
    private BigDecimal latitude;

    @Column(name = "longitude",nullable = false, precision = 8, scale = 5)
    private BigDecimal longitude;
}
