package com.example.ga4demo.googleanalytics4.utils;

import com.google.common.collect.ImmutableList;
import com.example.ga4demo.googleanalytics4.types.GA4MediumType;
import com.example.ga4demo.googleanalytics4.types.GoogleAnalytics4MetricType;

import java.util.ArrayList;
import java.util.List;

public class Constants {

    public static final String PREFIX = "ga:";
    public static final String URL_PREFIX_HTTPS = "https://";
    public static final String URL_HOME_PATH = "/";

    public static final List<String> ALL_METRICS = ImmutableList.of(
            GoogleAnalytics4MetricType.BOUNCE_RATE.getValue(),
            GoogleAnalytics4MetricType.SESSIONS.getValue()
    );

    public static final List<GA4MediumType> ALL_MEDIUM_TYPES = new ArrayList<>(
            ImmutableList.of(
                    GA4MediumType.DIRECT,
                    GA4MediumType.ORGANIC,
                    GA4MediumType.PAID,
                    GA4MediumType.EMAIL,
                    GA4MediumType.REFERRAL,
                    GA4MediumType.NOT_SET,
                    GA4MediumType.OTHERS
            ));
}
