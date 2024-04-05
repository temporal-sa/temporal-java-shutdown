package com.example;

import io.temporal.worker.WorkerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.util.StopWatch;

import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootApplication
public class SimpleApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SimpleApplication.class);
        app.addListeners((ApplicationListener<ContextClosedEvent>) event -> {
            log.info("**** ContextClosedEvent received, gracefully shutting down WorkerFactory...");

            WorkerFactory workerFactory = event.getApplicationContext().getBean(WorkerFactory.class);

            StopWatch stopWatch = new StopWatch("WorkerFactory");
            stopWatch.start("shutdown");
            // Initiate orderly shutdown
            workerFactory.shutdown();
            stopWatch.stop();

            stopWatch.start("awaitTermination");
            // Block until all tasks are completed (or 20 second timeout)
            workerFactory.awaitTermination(20, TimeUnit.SECONDS);
            stopWatch.stop();
            log.info(stopWatch.prettyPrint());

            log.info("**** Graceful shutdown complete!");
        });
        app.run(args);
    }
}
