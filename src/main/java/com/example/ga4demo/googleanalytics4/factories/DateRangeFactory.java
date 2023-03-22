package com.example.ga4demo.googleanalytics4.factories;

import org.springframework.stereotype.Component;

@Component
public class DateRangeFactory {

    private static final int START_DAY_DEFAULT = 30;
    private static final String DATE_SUFFIX = "DaysAgo";
    private static final String TODAY = "today";


    public String buildDateLimit(int dayLimit) {
        return dayLimit + DATE_SUFFIX;
    }
}
