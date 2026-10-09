package com.hitachi.smartpark.controller;

import com.hitachi.smartpark.entity.ParkingLot;
import com.hitachi.smartpark.entity.Vehicle;
import com.hitachi.smartpark.entity.record.ParkingLotAvailability;
import com.hitachi.smartpark.entity.record.ParkingLotRequest;
import com.hitachi.smartpark.entity.record.ParkingRequest;
import com.hitachi.smartpark.exception.ParkingLotNotFoundException;
import com.hitachi.smartpark.service.ParkingLotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/parking-lot")
public class ParkingLotController {
    private final ParkingLotService parkingLotService;

    @PostMapping("/register")
    public ResponseEntity<ParkingLot> registerParkingLot(@RequestBody @Valid ParkingLotRequest parkingLotRequest){
        ParkingLot parkingLot = parkingLotService.registerParkingLot(parkingLotRequest);
        return ResponseEntity.status(HttpStatus.OK).body(parkingLot);
    }

    @GetMapping("/{lotId}")
    public ResponseEntity<ParkingLot> getParkingLotByLotId(@PathVariable String lotId){
        ParkingLot parkingLot = parkingLotService.getParkingLotById(lotId);
        return ResponseEntity.status(HttpStatus.OK).body(parkingLot);
    }

    @PostMapping("/{lotId}/check-in")
    public ResponseEntity<ParkingLot> checkInVehicle(@PathVariable String lotId, @RequestBody @Valid ParkingRequest parkingRequest){
        ParkingLot parkingLot = parkingLotService.checkInParking(parkingRequest, lotId);
        return ResponseEntity.status(HttpStatus.OK).body(parkingLot);
    }

    @PostMapping("/{lotId}/check-out")
    public ResponseEntity<ParkingLot> checkOutVehicle(@PathVariable String lotId, @RequestBody @Valid ParkingRequest parkingRequest){
        ParkingLot parkingLot = parkingLotService.checkOutParking(parkingRequest, lotId);
        return ResponseEntity.status(HttpStatus.OK).body(parkingLot);
    }

    @GetMapping("/{lotId}/availability")
    public ResponseEntity<ParkingLotAvailability> getParkingLotAvailability(@PathVariable String lotId){
        ParkingLot parkingLot = parkingLotService.getParkingLotById(lotId);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ParkingLotAvailability(
                    parkingLot.getLotId(),
                    parkingLot.getCapacity(),
            parkingLot.getCapacity()-parkingLot.getOccupiedSpaces()
                ));
    }

    @GetMapping("/{lotId}/vehicles")
    public ResponseEntity<List<Vehicle>> getParkingLotParkedVehicles(@PathVariable String lotId){
        ParkingLot parkingLot = parkingLotService.getParkingLotById(lotId);
        return ResponseEntity.status(HttpStatus.OK).body(parkingLot.getParkedVehicles());
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleDuplicate(DataIntegrityViolationException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("lotId", "Lot ID already exists"));
    }

}
