package com.acerasoni.carpark.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@Data
@RequiredArgsConstructor
public final class Car {

    private final long id;
    private Instant admissionTime;
    private Instant departureTime;
    private Bill bill;
}
