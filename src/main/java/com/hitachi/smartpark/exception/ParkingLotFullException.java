package com.hitachi.smartpark.exception;

public class ParkingLotFullException extends RuntimeException{
    public ParkingLotFullException(String lotId){
        super(lotId);
    }
}
