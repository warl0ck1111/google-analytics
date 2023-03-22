package com.example.ga4demo.googleanalytics4;


import com.example.ga4demo.googleanalytics4.dto.GoogleAnalyticsAccount;
import com.example.ga4demo.googleanalytics4.types.GoogleAnalyticsSinceDateType;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface GoogleAnalyticService {

    /**
     * MANAGEMENT APIs
     */
    List<GoogleAnalyticsAccount> getAccounts();


    /**
     * SESSIONS APIs
     */
    Long getSessionsNb(String domainName, String propertyId, LocalDate startDate, LocalDate endDate);

    Long getSessionsNbSince(String domainName, String propertyId, GoogleAnalyticsSinceDateType sinceDate);

//    Map<String, Long> getSessionsNbOverTime(String domainName, String viewId, LocalDate startDate, LocalDate endDate);
//
//    Long getSessionsNbSince(String domainName, String viewId, GoogleAnalyticsSinceDateType sinceDate);
//
//    Long getSessionsNbForUrl(String domainName, String viewId, String urlPattern, GoogleAnalyticsPatternMatchingType patternMatchingType, LocalDate startDate, LocalDate endDate);
//
//    Long getSessionsNbForUrlSince(String domainName, String viewId, String urlPattern, GoogleAnalyticsPatternMatchingType patternMatchingType, GoogleAnalyticsSinceDateType sinceDate);
//
//    Map<String, Long> getSessionsNbForUrlOverTime(
//            String domainName, String viewId, String urlPattern, GoogleAnalyticsPatternMatchingType patternMatchingType, LocalDate startDate, LocalDate endDate);
}
