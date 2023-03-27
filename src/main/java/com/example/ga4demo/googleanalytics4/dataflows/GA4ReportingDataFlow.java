package com.example.ga4demo.googleanalytics4.dataflows;


import com.example.ga4demo.googleanalytics4.types.GA4MediumType;
import com.example.ga4demo.googleanalytics4.types.GoogleAnalytics4SinceDateType;

import java.util.Map;
import java.util.Set;

public interface GA4ReportingDataFlow {

    <T>T get(String domainName, String propertyId, String startDate, String endDate, String metricAlias);

    Set<String> getUrls(String domainName, String propertyId, GA4MediumType mediumType, GoogleAnalytics4SinceDateType sinceDate);

    <T> Map<String, T> getOverTime(String domainName, String propertyId, String startDate, String endDate, String metricAlias);


}
