package com.acerasoni.carpark.service;

import com.acerasoni.carpark.exception.CarparkException;
import com.acerasoni.carpark.model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import reactor.core.publisher.Sinks;

import java.util.concurrent.LinkedBlockingDeque;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({MockitoExtension.class, OutputCaptureExtension.class})
class ParkingServiceTest {

    private Sinks.Many<Car> testCarPark;
    private ParkingService parkingService;

    @BeforeEach
    void setUp(@Mock final FormattingService formattingService) {
        testCarPark = Sinks.many().unicast().onBackpressureBuffer(new LinkedBlockingDeque<>(1));
        parkingService = new ParkingService(testCarPark, formattingService);
    }

    @Test
    void parkCar_whenCarEmissionOk_thenSetAdmissionTime() {
        final var car = new Car(0L);

        parkingService.parkCar(car);

        assertNotNull(car.getAdmissionTime());
        assertNull(car.getDepartureTime());
        assertNull(car.getBill());
    }

    @Test
    void parkCar_whenCarEmissionFailsWithOverflow_thenWarnCarparkIsFull(final CapturedOutput output) {
        final var carOne = new Car(0L);
        final var carTwo = new Car(1L);

        parkingService.parkCar(carOne);
        parkingService.parkCar(carTwo);

        assertTrue(output.getOut().contains("Unable to park car #1. The carpark is currently full."));
        assertNull(carTwo.getAdmissionTime());
        assertNull(carTwo.getDepartureTime());
        assertNull(carTwo.getBill());
    }

    @Test
    void parkCar_whenCarEmissionFailsWithOtherReason_thenThrowException() {
        testCarPark.tryEmitComplete();

        final var car = new Car(0L);
        final CarparkException exception = assertThrows(CarparkException.class, () -> parkingService.parkCar(car));
        assertEquals("Encountered unexpected error FAIL_TERMINATED when attempting to park car #0", exception.getMessage());
        assertNull(car.getAdmissionTime());
        assertNull(car.getDepartureTime());
        assertNull(car.getBill());
    }
}