package com.example;

import io.temporal.worker.WorkerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Order(0)
public class ContextClosedListener implements ApplicationListener<ContextClosedEvent> {
    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        // do nothing. this method is just for testing Order(0) vs. HIGHEST_PRECEDENCE and LOWEST_PRECEDENCE
        log.info("**** ContextClosedEvent received. ORDER(0)");
        WorkerFactory workerFactory = event.getApplicationContext().getBean(WorkerFactory.class);
        log.info("WorkerFactory: Started={}, Shutdown={}, Terminated={}",
                workerFactory.isStarted(), workerFactory.isShutdown(), workerFactory.isTerminated());
    }
}
