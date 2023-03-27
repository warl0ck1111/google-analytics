package com.example.ga4demo.googleanalytics4;



import com.example.ga4demo.googleanalytics4.dto.GoogleAnalytics4Account;
import com.example.ga4demo.googleanalytics4.types.GA4MediumType;
import com.example.ga4demo.googleanalytics4.types.GoogleAnalytics4PatternMatchingType;
import com.example.ga4demo.googleanalytics4.types.GoogleAnalytics4SinceDateType;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface GoogleAnalytic4Service {
    /**
     * MANAGEMENT APIs
     */
    List<GoogleAnalytics4Account> getAccounts();


    /**
     * SESSIONS APIs
     */
    Long getSessionsNb(String domainName, String propertyId, LocalDate startDate, LocalDate endDate);

    Long getSessionsNbSince(String domainName, String propertyId, GoogleAnalytics4SinceDateType sinceDate);

    Set<String> getPropertyUrlsForMedium(String domainName, String propertyId, GA4MediumType mediumType);

    Set<String> getPropertyUrlsForMediumSince(String domainName, String propertyId, GA4MediumType mediumType, GoogleAnalytics4SinceDateType sinceDate);

    Set<String> getPropertyUrlsSince(String domainName, String propertyId, GoogleAnalytics4SinceDateType sinceDate);



    Map<String, Long> getSessionsNbOverTime(String domainName, String propertyId, LocalDate startDate, LocalDate endDate);


    Long getSessionsNbForUrl(String domainName, String propertyId, String urlPattern, GoogleAnalytics4PatternMatchingType patternMatchingType, LocalDate startDate, LocalDate endDate);

    Long getSessionsNbForUrlSince(String domainName, String propertyId, String urlPattern, GoogleAnalytics4PatternMatchingType patternMatchingType, GoogleAnalytics4SinceDateType sinceDate);

    Map<String, Long> getSessionsNbForUrlOverTime(
            String domainName, String propertyId, String urlPattern, GoogleAnalytics4PatternMatchingType patternMatchingType, LocalDate startDate, LocalDate endDate);

    Long getSessionsNbFOrUrlForMedium(
            String domainName, String propertyId, String urlPattern, GoogleAnalytics4PatternMatchingType patternMatchingType, GA4MediumType type, LocalDate startDate, LocalDate endDate);

    Long getSessionsNbForUrlForMediumSince(
            String domainName, String propertyId, String urlPattern, GoogleAnalytics4PatternMatchingType patternMatchingType, GA4MediumType type, GoogleAnalytics4SinceDateType sinceDate);

    Map<String, Long> getSessionsNbForUrlForMediumOverTime(
            String domainName, String propertyId, String urlPattern, GoogleAnalytics4PatternMatchingType patternMatchingType, GA4MediumType type, LocalDate startDate, LocalDate endDate);

    Map<String, Long> getSessionsNbByUrls(
            String domainName, String propertyId, List<String> urls, LocalDate startDate, LocalDate endDate, boolean getNOTResult);

    Map<String, Long> getSessionsNbByUrlsSince(
            String domainName, String propertyId, List<String> urls, GoogleAnalytics4SinceDateType sinceDate, boolean getNOTResult);

    Map<String, Map<String, Long>> getSessionsNbByUrlsOverTime(
            String domainName, String propertyId, List<String> urls, LocalDate startDate, LocalDate endDate, boolean getNOTResult);

    Map<String, Map<String, Long>> getSessionsNbByUrlsForMediumOverTime(
            String domainName, String propertyId, List<String> urls, GA4MediumType medium, LocalDate startDate, LocalDate endDate, boolean getNOTResult);

    Map<String, Map<String, Long>> getSessionsNbByUrlsForMediumOverTimeSince(
            String domainName, String propertyId, List<String> urls, GA4MediumType medium, GoogleAnalytics4SinceDateType sinceDate, boolean getNOTResult);

    Map<String, Map<String, Long>> getSessionsNbByUrlForMediumOverTime(
            String domainName, String propertyId, String urlPattern, GoogleAnalytics4PatternMatchingType patternMatchingType, GA4MediumType medium, LocalDate startDate, LocalDate endDate, boolean getNOTResult);

    Map<String, Map<String, Long>> getSessionsNbByUrlForMediumOverTimeSince(
            String domainName, String propertyId, String urlPattern, GoogleAnalytics4PatternMatchingType patternMatchingType, GA4MediumType medium, GoogleAnalytics4SinceDateType sinceDate, boolean getNOTResult);


}
