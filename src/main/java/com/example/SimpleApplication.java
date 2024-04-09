package com.example;

import io.temporal.worker.WorkerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootApplication
public class SimpleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleApplication.class, args);
    }

    @EventListener
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public void handleContextClosedEventFirst(ContextClosedEvent event) {
        log.info("**** ContextClosedEvent received. ORDER(HIGHEST)");
        try {
            WorkerFactory workerFactory = event.getApplicationContext().getBean(WorkerFactory.class);
            log.info("Gracefully shutting down WorkerFactory...");

            // Initiate orderly shutdown
            workerFactory.shutdown();

            // Block until all tasks are completed (or 20 second timeout)
            workerFactory.awaitTermination(20, TimeUnit.SECONDS);

            log.info("Graceful shutdown complete!");
        } catch (BeansException e) {
            log.error("Failed to get WorkerFactory bean", e);
        }
    }

    @EventListener
    @Order(Ordered.LOWEST_PRECEDENCE)
    public void handleContextClosedEventLast(ContextClosedEvent event) {
        // do nothing. this method is just for testing HIGHEST_PRECEDENCE vs. LOWEST_PRECEDENCE
        log.info("**** ContextClosedEvent received. ORDER(LOWEST)");
        WorkerFactory workerFactory = event.getApplicationContext().getBean(WorkerFactory.class);
        log.info("WorkerFactory: Started={}, Shutdown={}, Terminated={}",
                workerFactory.isStarted(), workerFactory.isShutdown(), workerFactory.isTerminated());
    }
}
