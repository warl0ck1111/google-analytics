package com.example.ga4demo.googleanalytics4.utils;

import com.example.ga4demo.googleanalytics4.types.GoogleAnalyticsMetricType;
import com.example.ga4demo.googleanalytics4.types.MediumType;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class Constants {

    public static final String PREFIX = "ga:";
    public static final String URL_PREFIX_HTTPS = "https://";
    public static final String URL_HOME_PATH = "/";

    public static final List<String> ALL_METRICS = ImmutableList.of(
            GoogleAnalyticsMetricType.BOUNCE_RATE.getValue(),
            GoogleAnalyticsMetricType.SESSIONS.getValue()
    );

    public static final List<MediumType> ALL_MEDIUM_TYPES = new ArrayList<>(
            ImmutableList.of(
                    MediumType.DIRECT,
                    MediumType.ORGANIC,
                    MediumType.PAID,
                    MediumType.EMAIL,
                    MediumType.REFERRAL,
                    MediumType.NOT_SET,
                    MediumType.OTHERS
            ));
}
