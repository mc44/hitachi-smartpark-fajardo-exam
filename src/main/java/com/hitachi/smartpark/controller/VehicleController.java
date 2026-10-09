package com.hitachi.smartpark.controller;

import com.hitachi.smartpark.entity.Vehicle;
import com.hitachi.smartpark.entity.record.VehicleRequest;
import com.hitachi.smartpark.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vehicle")
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<Vehicle> registerVehicle(@RequestBody @Valid VehicleRequest vehicleDto){
        Vehicle vehicle = vehicleService.registerVehicle(vehicleDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(vehicle);
    }

    @GetMapping("/{licensePlate}")
    public ResponseEntity<Vehicle> getVehicle(@PathVariable String licensePlate){
        Vehicle vehicle = vehicleService.getVehicle(licensePlate);
        return ResponseEntity.status(HttpStatus.OK).body(vehicle);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String,String>> handleDuplicate(DataIntegrityViolationException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("licensePlate", "License plate is already registered."));
    }

}
