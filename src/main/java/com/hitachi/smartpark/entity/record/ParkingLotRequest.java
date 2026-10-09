package com.hitachi.smartpark.entity.record;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ParkingLotRequest (
    @NotBlank
    @Size(max = 50, min = 1)
    String lotId,

    @NotBlank
    String location,

    @NotNull
    @Min(0)
    Integer capacity
){}
