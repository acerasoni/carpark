package com.acerasoni.carpark.carpark.service;

import com.acerasoni.carpark.carpark.model.Car;
import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ParkingService implements CacheEventListener<Object, Object> {

    @Cacheable(value = "carPark", key = "#car.getId()")
    public Instant parkCar(final Car car) {
        System.out.println(car.getId());
        return Instant.now();
    }

    // ...

    @Override
    public void onEvent(
            CacheEvent<? extends Object, ? extends Object> cacheEvent) {
        System.out.println("" +
                cacheEvent.getKey() + cacheEvent.getOldValue() + cacheEvent.getNewValue());
    }
}
