package com.hitachi.smartpark.exception;

public class ParkingLotNotFoundException extends RuntimeException{
    public ParkingLotNotFoundException(String lotId){
        super(lotId);
    }
}
