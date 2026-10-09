package com.hitachi.smartpark.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<Map<String,String>> validationHandler(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
                );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String,String>> parkingLotNotFound(ParkingLotNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("lotId", "Parking Lot not found"));
    }

    @ExceptionHandler
    public ResponseEntity<Map<String,String>> vehicleNotFound(VehicleNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("licensePlate", "Vehicle not found"));
    }

    @ExceptionHandler
    public ResponseEntity<Map<String,String>> vehicleAlreadyParked(VehicleAlreadyParkedException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("licensePlate", "Vehicle already parked on " + ex.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<Map<String,String>> parkingLotFull(ParkingLotFullException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("lotId", "Parking lot is full: " + ex.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<Map<String,String>> vehicleNotParked(VehicleNotParkedException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("licensePlate", "Vehicle is not currently parked: " + ex.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<Map<String,String>> vehicleNotParkedHere(VehicleNotParkedHereException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("licensePlate", "Vehicle is not currently parked in the requested lot: " + ex.getMessage()));
    }

}
