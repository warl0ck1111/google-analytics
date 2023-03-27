package com.example.ga4demo.googleanalytics4.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadLocalRandom;

@UtilityClass
@Slf4j
public class ThreadUtils_INC {

    public void sleep(long origin, long bound) {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextLong(origin, bound + 1));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
