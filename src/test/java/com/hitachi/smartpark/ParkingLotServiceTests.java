package com.hitachi.smartpark;

import com.hitachi.smartpark.entity.ParkingLot;
import com.hitachi.smartpark.entity.Vehicle;
import com.hitachi.smartpark.entity.enums.VehicleType;
import com.hitachi.smartpark.entity.record.ParkingRequest;
import com.hitachi.smartpark.exception.*;
import com.hitachi.smartpark.repository.ParkingLotRepository;
import com.hitachi.smartpark.service.ParkingLotService;
import com.hitachi.smartpark.service.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class ParkingLotServiceTests {
    @Mock
    ParkingLotRepository parkingLotRepository;

    @Mock
    VehicleService vehicleService;

    @InjectMocks
    ParkingLotService parkingLotService;

    ParkingLot lot1;
    ParkingLot lot2;
    Vehicle vehicle1;
    Vehicle vehicle2;

    @BeforeEach
    void setUp() {
        lot1 = lot("LOT_1A", "Main Street");
        lot2 = lot("LOT_2B", "Second Avenue");
        vehicle1 = vehicle("ABC-123", "Ana Cruz");
        vehicle2 = vehicle("DEF-456", "Ben Santos");

        lenient().when(parkingLotRepository.findByLotId("LOT_1A")).thenReturn(lot1);
        lenient().when(parkingLotRepository.findByLotId("LOT_2B")).thenReturn(lot2);
        lenient().when(parkingLotRepository.findByLotIdForUpdate("LOT_1A")).thenReturn(lot1);
        lenient().when(parkingLotRepository.findByLotIdForUpdate("LOT_2B")).thenReturn(lot2);

        lenient().when(vehicleService.getVehicle("ABC-123")).thenReturn(vehicle1);
        lenient().when(vehicleService.getVehicle("DEF-456")).thenReturn(vehicle2);
    }

    @Test
    void throwWhenParkigLotNotFound(){
        assertThrows(ParkingLotNotFoundException.class,
                () -> parkingLotService.getParkingLotById("DOESNT-EXIST"));
    }

    @Test
    void rejectWhenParkingLotIsFull(){
        lot1.setOccupiedSpaces(1);

        assertThrows(ParkingLotFullException.class,
                () -> parkingLotService.checkInParking(new ParkingRequest(vehicle1.getLicensePlate()), lot1.getLotId()));
    }

    @Test
    void rejectWhenVehicleAlreadyParked() {
        parkingLotService.checkInParking(new ParkingRequest(vehicle1.getLicensePlate()), lot1.getLotId());

        assertThrows(VehicleAlreadyParkedException.class,
                () -> parkingLotService.checkInParking(new ParkingRequest(vehicle1.getLicensePlate()), lot2.getLotId())
        );
    }

    @Test
    void rejectWhenVehicleIsNotParked() {
        assertThrows(VehicleNotParkedException.class,
                () -> parkingLotService.checkOutParking(new ParkingRequest(vehicle1.getLicensePlate()), lot1.getLotId())
        );
    }

    @Test
    void rejectWhenVehicleIsNotParkedHere() {
        parkingLotService.checkInParking(new ParkingRequest(vehicle1.getLicensePlate()), lot1.getLotId());

        assertThrows(VehicleNotParkedHereException.class,
                () -> parkingLotService.checkOutParking(new ParkingRequest(vehicle1.getLicensePlate()), lot2.getLotId())
        );
    }

    @Test
    void checkInIncreasesOccupiedSpaces() {
        parkingLotService.checkInParking(new ParkingRequest(vehicle1.getLicensePlate()), lot1.getLotId());

        assertEquals(1, lot1.getOccupiedSpaces());
        assertEquals(0, lot1.getCapacity() - lot1.getOccupiedSpaces());
    }

    @Test
    void checkOutDecreasesOccupiedSpaces() {
        parkingLotService.checkInParking(new ParkingRequest(vehicle1.getLicensePlate()), lot1.getLotId());
        parkingLotService.checkOutParking(new ParkingRequest(vehicle1.getLicensePlate()), lot1.getLotId());

        assertEquals(0, lot1.getOccupiedSpaces());
        assertEquals(1, lot1.getCapacity() - lot1.getOccupiedSpaces());
    }


    private ParkingLot lot(String lotId, String location) {
        ParkingLot lot = new ParkingLot();
        lot.setLotId(lotId);
        lot.setLocation(location);
        lot.setCapacity(1);
        lot.setOccupiedSpaces(0);
        return lot;
    }

    private Vehicle vehicle(String licensePlate, String ownerName) {
        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate(licensePlate);
        vehicle.setOwnerName(ownerName);
        vehicle.setVehicleType(VehicleType.CAR);
        return vehicle;
    }
}
