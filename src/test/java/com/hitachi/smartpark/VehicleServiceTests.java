package com.hitachi.smartpark;

import com.hitachi.smartpark.exception.VehicleNotFoundException;
import com.hitachi.smartpark.repository.VehicleRepository;
import com.hitachi.smartpark.service.VehicleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class VehicleServiceTests {

    @Mock
    VehicleRepository vehicleRepository;

    @InjectMocks
    VehicleService vehicleService;

    @Test
    void throwWhenVehicleNotFound(){
        assertThrows(VehicleNotFoundException.class,
                ()-> vehicleService.getVehicle("DOESNT-EXIST")
        );
    }
}
