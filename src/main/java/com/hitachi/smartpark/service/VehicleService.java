package com.hitachi.smartpark.service;

import com.hitachi.smartpark.entity.ParkingLot;
import com.hitachi.smartpark.entity.Vehicle;
import com.hitachi.smartpark.entity.record.VehicleRequest;
import com.hitachi.smartpark.exception.ParkingLotNotFoundException;
import com.hitachi.smartpark.exception.VehicleNotFoundException;
import com.hitachi.smartpark.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    public Vehicle registerVehicle(VehicleRequest vehicleDto){
        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate(vehicleDto.licensePlate());
        vehicle.setVehicleType(vehicleDto.vehicleType());
        vehicle.setOwnerName(vehicleDto.ownerName());
        vehicleRepository.save(vehicle);
        return vehicle;
    }

    public Vehicle getVehicle(String licensePlate){
        Vehicle vehicle = vehicleRepository.findByLicensePlate(licensePlate);
        if(vehicle == null){
            throw new VehicleNotFoundException(licensePlate);
        }
        return vehicle;
    }
}
