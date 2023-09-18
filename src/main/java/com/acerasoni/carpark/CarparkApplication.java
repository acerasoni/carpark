package com.acerasoni.carpark;

import com.acerasoni.carpark.service.BillingService;
import com.acerasoni.carpark.service.EntryService;
import com.acerasoni.carpark.service.ExitService;
import com.acerasoni.carpark.service.ParkingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.concurrent.TimeUnit;

//TODO: Testing https://www.baeldung.com/spring-webflux-backpressure
@ComponentScan(basePackages = "com.acerasoni.carpark")
@RequiredArgsConstructor
@Slf4j
public class CarparkApplication implements CommandLineRunner {

    @Value("${carpark.size}")
    private int carparkSize;
    @Value("${carpark.hourly-rate}")
    private double hourlyRate;
    @Value("${carpark.speed-up-factor}")
    private int speedUpFactor;
    @Value("${carpark.simulation-length-seconds}")
    private int simulationLengthSeconds;

    private final EntryService entryService;
    private final ParkingService parkingService;
    private final ExitService exitService;
    private final BillingService billingService;

    public static void main(final String[] args) {
        SpringApplication.run(CarparkApplication.class, args);
    }

    @Override
    public void run(final String[] args) {
        log.info("Opening Carpark with a maximum size of '{}' and an hourly rate of '{}'. The simulation is sped-up by factor of '{}'",
                carparkSize, hourlyRate, speedUpFactor);

        entryService.generateEntryQueue().subscribe(parkingService::parkCar);
        exitService.generateExitQueue().subscribe(billingService::billCar);

        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(simulationLengthSeconds));
        } catch (final InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }

        final var revenueReport = billingService.generateRevenueReport();
        log.info("Carpark was open for {} seconds. It hosted {} cars and generated {}.",
                simulationLengthSeconds,
                revenueReport.numberOfCars(),
                revenueReport.revenue()
        );
    }
}
