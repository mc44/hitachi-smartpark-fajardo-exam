package com.hitachi.smartpark.entity.record;

public record ParkingLotAvailability(
        String lotId,
        Integer capacity,
        Integer available
) {}
