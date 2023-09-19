package com.acerasoni.carpark.service;

import com.acerasoni.carpark.model.Car;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.time.Duration;

class EntryServiceTest {

    @Test
    void generateEntryQueue_whenDelaySuppliedIs100ms_thenProduceCarsEvery100ms() {
        StepVerifier
                .create(new EntryService(100).generateEntryQueue())
                .expectSubscription()
                .expectNoEvent(Duration.ofMillis(100))
                .expectNext(new Car(0L))
                .thenAwait(Duration.ofMillis(100))
                .expectNext(new Car(1L))
                .expectNoEvent(Duration.ofMillis(50))
                .thenAwait(Duration.ofMillis(75))
                .expectNext(new Car(2L))
                .thenCancel()
                .verify();
    }
}