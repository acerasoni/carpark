# Overview

Carpark is a highly concurrent parking application written in Java's [Project Reactor](https://projectreactor.io/) powered by Java 20 and [Spring-Boot](https://spring.io/projects/spring-boot). It is scalable to many threads and adapts to the number of cores on the host machine.

## Application Design

To model the carpark, I went with reactor's [Sink.Many](https://projectreactor.io/docs/core/release/api/reactor/core/publisher/Sinks.Many.html) backed by a bounded blocking double-ended queue (effectively non-blocking). I considered using the highly optimised cache provider [Caffeine](https://github.com/ben-manes/caffeine), which is concurrent, highly scalable, and provides near-optimal efficiency within the JVM, but preferred to stay within the reactor ecosystem.

To model car arrivals and departures, I have chosen [Project Reactor](https://projectreactor.io/). I considered simpler imperative Java and Java8 streams, but reactor is best-in-class for highly concurrent publisher/consumer scenarios. For this project, car arrivals are modeled by publishing events, and car departures by consuming events. Reactor allows for fast and scalable concurrency with minimal setup. It is also easily testable and its performance can be monitored via [Reactor Metrics](https://projectreactor.io/docs/core/release/reference/#metrics)

## Run Instructions

*Note*: this project requires [Java20](https://jdk.java.net/20/) in `preview` mode.

Run [application.properties](src/main/resources/application.properties).

## Problem Statement

- When a car comes in, check if there is a space available, otherwise return a message saying it is full.
- There are 100 spaces available in the car park.
- Multiple cars may come in at the same time
- The solution must be similar to production code

## Assumptions

For the purpose of this simulation, I have made the following assumptions, which may be configured by modifying the [application.properties](src/main/resources/application.properties).

- A new car appears every 120 milliseconds
- Cars stay parked for 150 milliseconds
- The simulation lasts 20 seconds
- The simulation is sped up by a factor of 7,200 (such that, in billing terms, 500 milliseconds equal 1 hour)
    > To run the simulation at normal speed, set `carpark.speed-up-factor=7200`
- The currency is GBP

All of the behaviour above can be configured by modifying the [application.properties](src/main/resources/application.properties). You may 

Add tag with coverage
## Reflection

I've tried to make it production ready but obv there's more things required of a true prod-ready app, see below for things I would add
due to time constraints
## Extension

- Containerising the app
- Use user input to simulate car arrival
- Car arrival at random time rather than fixed
- Tracing
- Monitoring through 
//TODO: Put code coverage tag