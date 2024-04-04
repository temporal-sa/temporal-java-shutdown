package com.example.activities;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

@ActivityInterface
public interface SimpleActivities {

    @ActivityMethod
    String aOne(String input);

    @ActivityMethod
    String bTwo(String input);

    @ActivityMethod
    String cThree(String input);
}
