package com.hitachi.smartpark.exception;

public class VehicleNotParkedHereException extends RuntimeException{
    public VehicleNotParkedHereException(String licensePlate){
        super(licensePlate);
    }
}
