package com.hitachi.smartpark.service;

import com.hitachi.smartpark.entity.ParkingLot;
import com.hitachi.smartpark.entity.Vehicle;
import com.hitachi.smartpark.entity.record.ParkingLotRequest;
import com.hitachi.smartpark.entity.record.ParkingRequest;
import com.hitachi.smartpark.exception.*;
import com.hitachi.smartpark.repository.ParkingLotRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ParkingLotService {
    private final ParkingLotRepository parkingLotRepository;
    private final VehicleService vehicleService;

    public ParkingLot registerParkingLot(ParkingLotRequest parkingLotRequest){
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setLotId(parkingLotRequest.lotId());
        parkingLot.setLocation(parkingLotRequest.location());
        parkingLot.setCapacity(parkingLotRequest.capacity());
        parkingLot.setOccupiedSpaces(0);
        parkingLot.setParkedVehicles(List.of());
        parkingLotRepository.save(parkingLot);
        return parkingLot;
    }

    @Transactional
    public ParkingLot checkInParking(ParkingRequest parkingRequest, String lotId){
        ParkingLot parkingLot = fetchParkingLot(lotId, parkingLotRepository::findByLotIdForUpdate);
        if(parkingLot.getCapacity() - parkingLot.getOccupiedSpaces() <=0 ){
            throw new ParkingLotFullException(lotId);
        }
        Vehicle vehicle = vehicleService.getVehicle(parkingRequest.licensePlate());
        if(vehicle.getParkingLot() != null){
            throw new VehicleAlreadyParkedException(vehicle.getParkingLot().getLotId());
        }
        vehicle.setParkingLot(parkingLot);
        parkingLot.setOccupiedSpaces(parkingLot.getOccupiedSpaces() + 1);

        return parkingLot;
    }

    @Transactional
    public ParkingLot checkOutParking(ParkingRequest parkingRequest, String lotId){
        ParkingLot parkingLot = fetchParkingLot(lotId, parkingLotRepository::findByLotId);
        Vehicle vehicle = vehicleService.getVehicle(parkingRequest.licensePlate());
        if(vehicle.getParkingLot() == null){
            throw new VehicleNotParkedException(vehicle.getLicensePlate());
        }
        if(!lotId.equals(vehicle.getParkingLot().getLotId())){
            throw new VehicleNotParkedHereException(vehicle.getLicensePlate());
        }
        vehicle.setParkingLot(null);
        parkingLot.setOccupiedSpaces(parkingLot.getOccupiedSpaces() - 1);

        return parkingLot;
    }

    public ParkingLot getParkingLotById(String lotId) {
        return fetchParkingLot(lotId, parkingLotRepository::findByLotId);
    }

    private ParkingLot fetchParkingLot(String lotId, Function<String, ParkingLot> finder) {
        ParkingLot parkingLot = finder.apply(lotId);
        if (parkingLot == null) {
            throw new ParkingLotNotFoundException(lotId);
        }
        return parkingLot;
    }

}
