package com.example.activities;

import io.temporal.activity.Activity;
import io.temporal.spring.boot.ActivityImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@ActivityImpl(workers = "simple-worker")
public class SimpleActivitiesImpl implements SimpleActivities {

    private static void sleep(long seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String aOne(String input) {
        log.info("  BEGIN: aOne activity, input = {}", input);

        sleep(3);
        String result = input;

        log.info("  END  : aOne activity, result = {}", result);
        return result;
    }

    @Override
    public String bTwo(String input) {
        String result = null;
        try {
            log.info("  BEGIN: bTwo activity, input = {}", input);

            sleep(3);
            result = input;

            Activity.getExecutionContext().heartbeat(null);
            log.info("  END  : bTwo activity, result = {}", result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return result;
        }
    }

    @Override
    public String cThree(String input) {
        log.info("  BEGIN: cThree activity, input = {}", input);

        sleep(3);
        String result = input;

        log.info("  END  : cThree activity, result = {}", input);
        return result;
    }
}
