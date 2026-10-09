package com.hitachi.smartpark.exception;

public class VehicleNotParkedException extends RuntimeException{
    public VehicleNotParkedException(String licensePlate){
        super(licensePlate);
    }
}
