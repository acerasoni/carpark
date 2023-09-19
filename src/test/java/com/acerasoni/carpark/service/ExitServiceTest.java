package com.acerasoni.carpark.service;

import com.acerasoni.carpark.model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Sinks;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class ExitServiceTest {

    private static final Car CAR_ONE = new Car(0L);
    private static final Car CAR_TWO = new Car(1L);
    private static final Car CAR_THREE = new Car(2L);

    private ExitService exitService;
    private Sinks.Many<Car> testCarPark;

    @BeforeEach
    void setUp(@Mock final FormattingService formattingService) {
        testCarPark = Sinks.many().unicast().onBackpressureBuffer();
        exitService = new ExitService(100, formattingService, testCarPark);
    }

    @Test
    void generateExitQueue_whenCarsEmittedAsync_thenVerifyFluxPublisherWithEmittedCars() {
        testCarPark.tryEmitNext(CAR_ONE);
        testCarPark.tryEmitNext(CAR_TWO);

        final var stepVerifier = StepVerifier
                .create(exitService.generateExitQueue());

        stepVerifier
                .expectSubscription()
                .expectNext(CAR_ONE)
                .expectNext(CAR_TWO);

        testCarPark.tryEmitNext(CAR_THREE);
        testCarPark.tryEmitComplete();

        stepVerifier
                .expectNext(CAR_THREE)
                .expectComplete()
                .verify();
    }

    @Test
    void generateExitQueue_whenCarsEmittedSync_thenVerifyFluxPublisherWithEmittedCars() {
        testCarPark.tryEmitNext(CAR_ONE);
        testCarPark.tryEmitNext(CAR_TWO);
        testCarPark.tryEmitNext(CAR_THREE);
        testCarPark.tryEmitComplete();

        StepVerifier
                .create(exitService.generateExitQueue())
                .expectSubscription()
                .expectNext(CAR_ONE)
                .expectNext(CAR_TWO)
                .expectNext(CAR_THREE)
                .expectComplete()
                .verify();
    }
}