package com.acerasoni.carpark.config;

import com.acerasoni.carpark.model.Bill;
import com.acerasoni.carpark.model.Car;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

@Configuration
@NoArgsConstructor
public class CarparkConfig {

    @Value("${carpark.size}")
    private int carparkSize;

    @Bean
    public Sinks.Many<Car> carPark() {
        return Sinks.many().unicast().onBackpressureBuffer(new LinkedBlockingDeque<>(carparkSize));
    }

    @Bean
    public Set<Bill> billRegister() {
        return ConcurrentHashMap.newKeySet();
    }
}
