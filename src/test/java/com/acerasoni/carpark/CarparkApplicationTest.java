package com.acerasoni.carpark;

import com.acerasoni.carpark.model.Car;
import com.acerasoni.carpark.model.RevenueReport;
import com.acerasoni.carpark.service.BillingService;
import com.acerasoni.carpark.service.EntryService;
import com.acerasoni.carpark.service.ExitService;
import com.acerasoni.carpark.service.ParkingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import reactor.core.publisher.Flux;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, OutputCaptureExtension.class})
class CarparkApplicationTest {

    @Mock
    private EntryService entryService;
    @Mock
    private ParkingService parkingService;
    @Mock
    private ExitService exitService;
    @Mock
    private BillingService billingService;

    @Test
    void run_whenExecuted_thenRunsCarparkApplication(final CapturedOutput output) {
        final var carparkApplication = new CarparkApplication(5, 1, 1, 2,
                entryService, parkingService, exitService, billingService);

        final var carOne = new Car(0L);
        final var carTwo = new Car(1L);
        when(entryService.generateEntryQueue()).thenReturn(Flux.just(carOne, carTwo));
        when(exitService.generateExitQueue()).thenReturn(Flux.just(carOne, carTwo));
        when(billingService.generateRevenueReport()).thenReturn(new RevenueReport(3, "£50"));

        carparkApplication.run(new String[]{});
        verify(parkingService).parkCar(carOne);
        verify(parkingService).parkCar(carTwo);
        verify(billingService).billCar(carOne);
        verify(billingService).billCar(carTwo);

        assertTrue(output.getOut().contains("Carpark closed after 2 seconds. It hosted 3 cars and generated £50."), "Carpark closure message differs from expected value");
    }
}