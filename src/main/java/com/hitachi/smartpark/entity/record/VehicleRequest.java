package com.hitachi.smartpark.entity.record;

import com.hitachi.smartpark.entity.enums.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record VehicleRequest(
        @NotBlank
        @NotNull
        @Pattern(regexp = "^[A-Za-z0-9-]+$", message = "License plate can only contain letters, numbers, and hyphens")
        String licensePlate,

        @NotBlank
        @NotNull
        @Pattern(
                regexp = "^[A-Za-z]+(?: [A-Za-z]+)*$",
                message = "Owner name can only contain letters and single spaces"
        )
        String ownerName,

        @NotNull
        VehicleType vehicleType
) {}
