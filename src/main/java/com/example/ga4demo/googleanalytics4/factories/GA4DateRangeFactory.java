package com.example.ga4demo.googleanalytics4.factories;

import com.google.analytics.data.v1beta.DateRange;
import org.springframework.stereotype.Component;

@Component
public class GA4DateRangeFactory {

    private static final int START_DAY_DEFAULT = 30;
    private static final String DATE_SUFFIX = "DaysAgo";
    private static final String TODAY = "today";


    public String buildDateLimit(int dayLimit) {
        return dayLimit + DATE_SUFFIX;
    }

    public DateRange get(String startDate, String endDate) {
        return  DateRange.newBuilder().setStartDate(startDate).setEndDate(endDate).build();
    }
}
