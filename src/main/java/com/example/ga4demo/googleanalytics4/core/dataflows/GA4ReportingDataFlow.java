package com.example.ga4demo.googleanalytics4.core.dataflows;


import com.example.ga4demo.googleanalytics4.core.types.GA4MediumType;
import com.example.ga4demo.googleanalytics4.core.types.GoogleAnalytics4PatternMatchingType;
import com.example.ga4demo.googleanalytics4.core.types.GoogleAnalytics4SinceDateType;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface GA4ReportingDataFlow {

    <T>T get(String domainName, String propertyId, String startDate, String endDate, String metricAlias);

    <T> T getByUrl(String domainName, String propertyId, String urlPattern, GoogleAnalytics4PatternMatchingType patternMatchingType, String startDate, String endDate, String metricAlias);

    Set<String> getUrls(String domainName, String propertyId, GA4MediumType mediumType, GoogleAnalytics4SinceDateType sinceDate);

    <T> Map<String, T> getOverTime(String domainName, String propertyId, String startDate, String endDate, String metricAlias);

    <T> Map<String, T> getByUrlOverTime(String domainName, String propertyId, String urlPattern, GoogleAnalytics4PatternMatchingType patternMatchingType, String startDate, String endDate, String metricAlias);

    <T> T getByUrlForMedium(String domainName, String propertyId, String urlPattern, GoogleAnalytics4PatternMatchingType patternMatchingType, GA4MediumType medium, String startDate, String endDate, String metricAlias);

    <T> Map<String, T> getByUrlForMediumOverTime(String domainName, String propertyId, String urlPattern, GoogleAnalytics4PatternMatchingType patternMatchingType, GA4MediumType type, String startDate, String endDate, String metricAlias);

    <T> Map<String, T> getByUrls(String domainName, String propertyId, List<String> urls, String startDate, String endDate, String metricAlias, boolean getNOTResult);

    <T> Map<String, Map<String, T>> getByUrlsOverTime(String domainName, String propertyId, List<String> urls, String startDate, String endDate, String metricAlias, boolean getNOTResult);

    <T> Map<String, Map<String, T>> getByUrlsOverTimeForMedium(String domainName, String propertyId, List<String> urls, GA4MediumType medium, String startDate, String endDate, String metricAlias, boolean getNOTResult);

        <T> Map<String, T> getByUrlOverTimeForMedium(String domainName, String propertyId, String urlPattern, GoogleAnalytics4PatternMatchingType patternMatchingType, GA4MediumType medium, String startDate, String endDate, String metricAlias);

}
