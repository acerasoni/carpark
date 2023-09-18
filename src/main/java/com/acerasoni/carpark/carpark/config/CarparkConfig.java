package com.acerasoni.carpark.carpark.config;

import com.acerasoni.carpark.carpark.model.Car;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;

import java.util.concurrent.LinkedBlockingDeque;

@Configuration
public class CarparkConfig {

    @Value("${carpark.size}")
    private int carparkSize;

    @Bean
    public Sinks.Many<Car> carPark() {
        return Sinks.many().unicast().onBackpressureBuffer(new LinkedBlockingDeque<>(20));
    }
}
