package com.example.ga4demo.googleanalytics4.core.types;

import lombok.Getter;

public enum GoogleAnalytics4DimensionType {
    DIMENSION_HOSTNAME("hostname"),
    DIMENSION_PAGE_PATH("pagePathPlusQueryString"),
//    DIMENSION_LANDING_PAGE_PATH("landingPagePath"),
    DIMENSION_CHANNEL_GROUPING("firstUserDefaultChannelGroup"),
    DIMENSION_MEDIUM("sessionDefaultChannelGrouping"),
    DIMENSION_COUNTRY("countryId");
    @Getter
    private final String value;

    GoogleAnalytics4DimensionType(String value) {
        this.value = value;
    }
}
