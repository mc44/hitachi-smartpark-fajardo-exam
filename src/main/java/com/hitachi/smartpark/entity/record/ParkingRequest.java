package com.hitachi.smartpark.entity.record;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.NonNull;

public record ParkingRequest(
        @NotBlank
        @NonNull
        @Pattern(regexp = "^[A-Za-z0-9-]+$", message = "License plate can only contain letters, numbers, and hyphens")
        String licensePlate
)
{}
