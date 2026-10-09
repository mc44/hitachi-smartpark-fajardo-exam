package com.hitachi.smartpark.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class ParkingLot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lot_id", unique = true, nullable = false, length = 50)
    private String lotId;

    @NotBlank
    @Column(name = "location")
    private String location;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "occupied_spaces")
    private Integer occupiedSpaces;

    @OneToMany(mappedBy = "parkingLot")
    private List<Vehicle> parkedVehicles;
}
