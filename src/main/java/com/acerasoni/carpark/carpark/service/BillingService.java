package com.acerasoni.carpark.carpark.service;

import com.acerasoni.carpark.carpark.model.Car;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class BillingService {

    public void billCar(final Car car) {
        Objects.requireNonNull(car.getArrivalTime());
        Objects.requireNonNull(car.getDepartureTime());
    }
}
