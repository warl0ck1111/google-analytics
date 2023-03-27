package com.example.ga4demo.googleanalytics4.core.types;

import lombok.Getter;

public enum GA4TimeIncrement {
    WEEK("week"),
    DATE("date");

    @Getter
    private final String value;

    GA4TimeIncrement(String value) {
        this.value = value;
    }
}
