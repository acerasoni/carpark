package com.acerasoni.carpark.carpark;

import com.acerasoni.carpark.carpark.service.BillingService;
import com.acerasoni.carpark.carpark.service.EntryService;
import com.acerasoni.carpark.carpark.service.ExitService;
import com.acerasoni.carpark.carpark.service.ParkingService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.scheduler.Schedulers;

//TODO: Testing https://www.baeldung.com/spring-webflux-backpressure
@SpringBootApplication
@AllArgsConstructor
public class CarparkApplication implements CommandLineRunner {

    private final EntryService entryService;
    private final ParkingService parkingService;
    private final ExitService exitService;
    private final BillingService billingService;

    public static void main(final String[] args) {
        SpringApplication.run(CarparkApplication.class, args);
    }

    @Override
    public void run(final String... args) {
        entryService.generateCars()
                .subscribeOn(Schedulers.parallel())
                .subscribe(parkingService::parkCar);
        exitService.departCars()
                .subscribeOn(Schedulers.parallel())
                .subscribe(billingService::billCar);

        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
