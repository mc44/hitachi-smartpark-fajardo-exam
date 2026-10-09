package com.hitachi.smartpark.exception;

public class VehicleAlreadyParkedException extends RuntimeException{
    public VehicleAlreadyParkedException(String lotId){
        super(lotId);
    }
}
