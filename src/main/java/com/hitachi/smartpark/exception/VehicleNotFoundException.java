package com.hitachi.smartpark.exception;

public class VehicleNotFoundException extends RuntimeException{
    public VehicleNotFoundException(String licensePlate){
        super(licensePlate);
    }
}
