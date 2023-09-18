package com.acerasoni.carpark.carpark.model;

import lombok.Data;

@Data
public final class Car {

    private final long id;
    private Bill bill;
}
