package com.example.ga4demo.googleanalytics4.core.types;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
public enum GoogleAnalytics4SinceDateType {
    LAST_16_MONTHS("486daysAgo", 486),
    LAST_12_MONTHS("364daysAgo", 364),
    LAST_90_DAYS("89daysAgo", 89),
    LAST_30_DAYS("29daysAgo", 29),
    LAST_7_DAYS("6daysAgo", 6),
    TODAY("today", 0);

    @Getter
    private final String value;
    @Getter
    private final int daysAgo;
    @Getter
    private final LocalDate localDate;

    GoogleAnalytics4SinceDateType(String value, int daysAgo) {
        this.value = value;
        this.daysAgo = daysAgo;
        this.localDate = getLocalDateFromDaysAgo(daysAgo);
    }

    public static GoogleAnalytics4SinceDateType from(SinceDateType sinceDate) {
        switch (sinceDate) {
            case LAST_16_MONTHS:
                return LAST_16_MONTHS;
            case LAST_12_MONTHS:
                return LAST_12_MONTHS;
            case LAST_3_MONTHS:
                return LAST_90_DAYS;
            case LAST_MONTH:
                return LAST_30_DAYS;
            case LAST_7_DAYS:
                return LAST_7_DAYS;
            case LATEST_DAY:
                return TODAY;

            default:
                log.info("GoogleAnalyticsSinceDateType.from/message= No values found for provided sinceDate:" + sinceDate
                        + " - fallback: used LAST_30_DAYS");
                return LAST_30_DAYS;
        }
    }

    private LocalDate getLocalDateFromDaysAgo(int daysAgo) {
        return LocalDate.now().minusDays(daysAgo);
    }
}
