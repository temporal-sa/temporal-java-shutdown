package com.example;

import io.temporal.worker.WorkerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootApplication
public class SimpleApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SimpleApplication.class);
        app.addListeners((ApplicationListener<ContextClosedEvent>) event -> {
            log.info("\n\n**** ContextClosedEvent received.\n");

            WorkerFactory workerFactory = event.getApplicationContext().getBean(WorkerFactory.class);
            if (workerFactory != null) {
                log.info("\n\n**** Gracefully shutting down WorkerFactory...\n");

                // Initiate orderly shutdown
                workerFactory.shutdown();

                // Block until all tasks are completed (or 20 second timeout)
                workerFactory.awaitTermination(20, TimeUnit.SECONDS);

                log.info("\n\n**** Graceful shutdown complete!\n");
            }
        });
        app.run(args);
    }
}
