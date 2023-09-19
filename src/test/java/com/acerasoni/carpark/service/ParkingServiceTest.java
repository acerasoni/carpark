package com.acerasoni.carpark.service;

import com.acerasoni.carpark.model.Car;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Sinks;

@ExtendWith(MockitoExtension.class)
class ParkingServiceTest {

    @Mock
    private FormattingService formattingService;

    @Test
    void parkCar() {

        Sinks.Many<Car> carPark;
    }
}