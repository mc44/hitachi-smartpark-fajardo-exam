package com.hitachi.smartpark.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hitachi.smartpark.entity.enums.VehicleType;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "license_plate", unique = true, nullable = false)
    private String licensePlate;

    @Column(name = "vehicle_type")
    private VehicleType vehicleType;

    @Column(name = "owner_name")
    private String ownerName;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "parking_lot_id")
    private ParkingLot parkingLot;
}
