package com.acerasoni.carpark;

import com.acerasoni.carpark.service.BillingService;
import com.acerasoni.carpark.service.EntryService;
import com.acerasoni.carpark.service.ExitService;
import com.acerasoni.carpark.service.ParkingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.concurrent.TimeUnit;

@ComponentScan(basePackages = "com.acerasoni.carpark")
@Slf4j
public class CarparkApplication implements CommandLineRunner {

    private final int carparkSize;
    private final double hourlyRate;
    private final int speedUpFactor;
    private final int simulationLengthSeconds;
    private final EntryService entryService;
    private final ParkingService parkingService;
    private final ExitService exitService;
    private final BillingService billingService;

    public CarparkApplication(@Value("${carpark.size}") final int carparkSize,
                              @Value("${carpark.hourly-rate}") final double hourlyRate,
                              @Value("${carpark.speed-up-factor}") final int speedUpFactor,
                              @Value("${carpark.simulation-length-seconds}") final int simulationLengthSeconds,
                              final EntryService entryService,
                              final ParkingService parkingService,
                              final ExitService exitService,
                              final BillingService billingService) {
        this.carparkSize = carparkSize;
        this.hourlyRate = hourlyRate;
        this.speedUpFactor = speedUpFactor;
        this.simulationLengthSeconds = simulationLengthSeconds;
        this.entryService = entryService;
        this.parkingService = parkingService;
        this.exitService = exitService;
        this.billingService = billingService;
    }

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
        log.info("Carpark closed after {} seconds. It hosted {} cars and generated {}.",
                simulationLengthSeconds,
                revenueReport.numberOfCars(),
                revenueReport.revenue()
        );
    }
}
