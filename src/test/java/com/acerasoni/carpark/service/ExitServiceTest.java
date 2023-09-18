package com.acerasoni.carpark.service;

import com.acerasoni.carpark.model.Car;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Sinks;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class ExitServiceTest {

    @Mock
    private FormattingService formattingService;

    @Test
    void testGenerateExitQueue() {
        final Sinks.Many<Car> testSink = Sinks.many().unicast().onBackpressureBuffer();
        final Car carOne = new Car(0L);
        final Car carTwo = new Car(1L);
        final Car carThree = new Car(3L);
        testSink.tryEmitNext(carOne);
        testSink.tryEmitNext(carTwo);

        final ExitService exitService = new ExitService(100, formattingService, testSink);

        final var stepVerifier = StepVerifier
                .create(exitService.generateExitQueue());

        stepVerifier
                .expectSubscription()
                .expectNext(carOne)
                .expectNext(carTwo);

        testSink.tryEmitNext(carThree);

        stepVerifier
                .expectNext(carThree)
                .thenCancel()
                .verify();
    }
}