package com.example.ga4demo.googleanalytics4.types;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public enum GoogleAnalytics4MetricType {
    SESSIONS("sessions"),
    USERS("totalUsers"),
    NEW_USERS("newUsers"),
    BOUNCE_RATE("bounceRate"),
    AVG_PAGES_PER_SESSION("screenPageViewsPerSession"),
    AVG_TIME_PER_SESSION("averageSessionDuration"),
    TRANSACTIONS("transactions");

    public static final List<String> LANDING_URL_METRICS_VALUES = Arrays.asList(SESSIONS.getValue(),
            USERS.getValue(), NEW_USERS.getValue(), BOUNCE_RATE.getValue(), AVG_PAGES_PER_SESSION.getValue(), AVG_TIME_PER_SESSION.getValue(),
            TRANSACTIONS.getValue());
    @Getter
    private final String value;

    GoogleAnalytics4MetricType(String value) {
        this.value = value;
    }
}
