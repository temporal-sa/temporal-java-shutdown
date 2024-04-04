package com.example;

import io.temporal.worker.WorkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StopWatch;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class SimpleApplication implements CommandLineRunner {

    @Autowired
    private WorkerFactory workerFactory;

    public static void main(String[] args) {
        SpringApplication.run(SimpleApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\n\n**** Runtime shutdown hook starting...\n");

            StopWatch stopWatch = new StopWatch("ShutdownHook");

            stopWatch.start("shutdown()");
            workerFactory.shutdown();
            stopWatch.stop();

            stopWatch.start("awaitTermination()");
            workerFactory.awaitTermination(30, TimeUnit.SECONDS);
            stopWatch.stop();

            System.out.println("\n" + stopWatch.prettyPrint());

            System.out.println("**** Runtime shutdown hook is done!\n");
        }));
    }
}
