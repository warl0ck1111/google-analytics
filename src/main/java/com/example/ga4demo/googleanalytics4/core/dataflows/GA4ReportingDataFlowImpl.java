package com.example.ga4demo.googleanalytics4.core.dataflows;

import com.example.ga4demo.googleanalytics4.core.factories.GA4DateRangeFactory;
import com.example.ga4demo.googleanalytics4.core.types.GA4MediumType;
import com.example.ga4demo.googleanalytics4.core.types.GA4TimeIncrement;
import com.example.ga4demo.googleanalytics4.core.types.GoogleAnalytics4PatternMatchingType;
import com.example.ga4demo.googleanalytics4.core.types.GoogleAnalytics4SinceDateType;
import com.example.ga4demo.googleanalytics4.exceptions.*;
import com.example.ga4demo.googleanalytics4.utils.ThreadUtils_INC;
import com.example.ga4demo.shared.utils.DateUtils;
import com.google.analytics.data.v1beta.*;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.example.ga4demo.googleanalytics4.GoogleAuthenticationService2;
import com.example.ga4demo.googleanalytics4.core.factories.GA4RequestFactory;
import com.example.ga4demo.googleanalytics4.utils.GA4ReportingUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
@Slf4j
public class GA4ReportingDataFlowImpl implements GA4ReportingDataFlow {

    @Autowired
    private GoogleAuthenticationService2 googleAuthenticationService;
    @Autowired
    private GA4RequestExecutorService gaRequestExecutorService;

    private BetaAnalyticsDataClient analyticsData;

    private static final int PERIOD_LENGTH = 365;
    private static final String DATE_DIMENSION = "date";
    private static final String WEEK_DIMENSION = "week";

    @Override
    @Retryable(value = {GA4getFailureException.class}, maxAttempts = 1)
    public <T> T get(String domainName, String propertyId, String startDate, String endDate, String metricAlias) {
        try {
            RunReportRequest runReportRequest = GA4RequestFactory
                    .buildRequestBasic(propertyId, startDate, endDate, metricAlias);
            BatchRunReportsRequest batchRunReportsRequest = BatchRunReportsRequest.newBuilder()
                    .setProperty(propertyId)
                    .addRequests(runReportRequest).build();


            BatchRunReportsResponse batchRunReportResponse = getBatchRunReports(domainName, batchRunReportsRequest);

            return GA4ReportingUtils.parseMetricFromResponse(batchRunReportResponse);
        } catch (Exception e) {
            log.info("get/There was an exception: "+ e);
        }
        return null;
    }

    @Recover
    private BatchRunReportsResponse get_Recover(
            GA4getFailureException e, String domainName, String propertyId, String startDate, String endDate, String metricAlias) {
        log.info("GoogleGA4.get_Recover/domainName= " + domainName);
        log.info("GoogleGA4.get_Recover/propertyId= " + propertyId);
        log.info("GoogleGA4.get_Recover/startDate= " + startDate);
        log.info("GoogleGA4.get_Recover/endDate= " + endDate);
        log.info("GoogleGA4.get_Recover/metricAlias= " + metricAlias);
        log.info("GoogleGA4.get_Recover/exception= " + e.getMessage());
        return null;
    }



    // Get Known urls for given propertyId
    @Override
    @Retryable(value = {GA4getUrlsFailureException.class}, maxAttempts = 1)
    public Set<String> getUrls(String domainName, String propertyId, GA4MediumType mediumType, GoogleAnalytics4SinceDateType sinceDate) {
        try {
            if (sinceDate == null) {
                sinceDate = GoogleAnalytics4SinceDateType.LAST_12_MONTHS;
            }
            LocalDate startDate = sinceDate.getLocalDate();
            LocalDate endDate = LocalDate.now();

            BatchRunReportsRequest request = GA4RequestFactory.buildRequestKnownUrls(propertyId, mediumType, startDate.toString(), endDate.toString());
            BatchRunReportsResponse response = getBatchRunReports(domainName, request);
            return GA4ReportingUtils.parseSetDimensionsFromResponse(response);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new GA4getUrlsFailureException();
        }
    }

    @Recover
    private Set<String> getUrls_Recover(GA4getUrlsFailureException e, String domainName, String propertyId, GA4MediumType mediumType, GoogleAnalytics4SinceDateType sinceDate) {
        log.info("GoogleGA4.getUrls_Recover/domainName= " + domainName);
        log.info("GoogleGA4.getUrls_Recover/propertyId= " + propertyId);
        log.info("GoogleGA4.getUrls_Recover/sinceDate= " + sinceDate);
        log.info("GoogleGA4.getUrls_Recover/canalType= " + mediumType);
        log.info("GoogleGA4.getUrls_Recover/exception= " + e.getMessage());
        return new HashSet<>();
    }

    // Get total metric value variation for all URL over time
    @Override
    @Retryable(value = {GA4getOverTimeFailureException.class}, maxAttempts = 1)
    public <T> Map<String, T> getOverTime(String domainName,
                                          String propertyId, String startDate, String endDate, String metricAlias) {
        try {

            RunReportRequest request =
                    GA4RequestFactory.buildRequestOverTime(
                            propertyId, startDate, endDate, metricAlias, GA4TimeIncrement.DATE);
            BatchRunReportsRequest batchRunReportsRequest = BatchRunReportsRequest.newBuilder().addRequests(request).build();

            BatchRunReportsResponse response = getByBatchesSyncByShortPeriods(domainName, batchRunReportsRequest, startDate, endDate);

            List<String> dates = GA4ReportingUtils.buildListDatesFromDateRange(startDate, endDate);
            return GA4ReportingUtils.parseMetricMapFromResponse(response, dates);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new GA4getOverTimeFailureException();
        }
    }

    @Recover
    private <T> Map<String, T> getOverTime_Recover(
            GA4getOverTimeFailureException e, String domainName, String propertyId, String startDate, String endDate, String metricAlias) {
        log.info("GoogleGA4.getOverTime_Recover/domainName= " + domainName);
        log.info("GoogleGA4.getOverTime_Recover/propertyId= " + propertyId);
        log.info("GoogleGA4.getOverTime_Recover/startDate= " + startDate);
        log.info("GoogleGA4.getOverTime_Recover/endDate= " + endDate);
        log.info("GoogleGA4.getOverTime_Recover/metric= " + metricAlias);
        log.info("GoogleGA4.getOverTime_Recover/exception= " + e.getMessage());
        return new HashMap<>();
    }



    // Get total metric value variation for all URL over time
    @Override
    @Retryable(value = {GA4getByUrlOverTimeFailureException.class}, maxAttempts = 1)
    public <T> Map<String, T> getByUrlOverTime(
            String domainName,
            String propertyId,
            String urlPattern,
            GoogleAnalytics4PatternMatchingType patternMatchingType,
            String startDate,
            String endDate,
            String metricAlias) {
        try {
            RunReportRequest request =
                    GA4RequestFactory.buildRequestByUrlOrLandingUrlOverTime(
                            domainName, propertyId, urlPattern, patternMatchingType, startDate, endDate, metricAlias, GA4TimeIncrement.DATE);

            BatchRunReportsRequest runReportsRequest = BatchRunReportsRequest.newBuilder().addRequests(request).build();

            BatchRunReportsResponse response = getByBatchesSyncByShortPeriods(domainName, runReportsRequest, startDate, endDate);
            List<String> dates = GA4ReportingUtils.buildListDatesFromDateRange(startDate, endDate);

            return GA4ReportingUtils.parseMetricMapFromResponse(response, dates);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new GA4getByUrlOverTimeFailureException();
        }
    }

    @Recover
    private <T> Map<String, T> getByUrlOverTime_Recover(
            GA4getByUrlOverTimeFailureException e,
            String domainName,
            String propertyId,
            String urlPattern,
            GoogleAnalytics4PatternMatchingType patternMatchingType,
            String startDate,
            String endDate,
            String metricAlias) {
        log.info("GoogleGA4.getByUrlOverTime_Recover/domainName= " + domainName);
        log.info("GoogleGA4.getByUrlOverTime_Recover/propertyId= " + propertyId);
        log.info("GoogleGA4.getByUrlOverTime_Recover/urlPattern= " + urlPattern);
        log.info("GoogleGA4.getByUrlOverTime_Recover/patternMatchingType= " + patternMatchingType);
        log.info("GoogleGA4.getByUrlOverTime_Recover/startDate= " + startDate);
        log.info("GoogleGA4.getByUrlOverTime_Recover/endDate= " + endDate);
        log.info("GoogleGA4.getByUrlOverTime_Recover/metric= " + metricAlias);
        log.info("GoogleGA4.getByUrlOverTime_Recover/exception= " + e.getMessage());
        return new HashMap<>();
    }



    // Get total metric value for one url, between start/end dates
    @Override
    @Retryable(value = {GA4getByUrlFailureException.class}, maxAttempts = 1)
    public <T> T getByUrl(
            String domainName,
            String propertyId,
            String urlPattern,
            GoogleAnalytics4PatternMatchingType patternMatchingType,
            String startDate,
            String endDate,
            String metricAlias
    ) {
        try {
            BatchRunReportsRequest request = GA4RequestFactory.buildRequestByUrlOrLandingUrl(domainName, propertyId, urlPattern, patternMatchingType, startDate, endDate, metricAlias);
            BatchRunReportsResponse response = getBatchRunReports(domainName, request);
            return GA4ReportingUtils.parseMetricFromResponse(response);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new GA4getByUrlFailureException();
        }
    }

    @Recover
    private <T> T getByUrl_Recover(
            GA4getByUrlFailureException e,
            String domainName,
            String propertyId,
            String urlPattern,
            GoogleAnalytics4PatternMatchingType patternMatchingType,
            String startDate,
            String endDate,
            String metricAlias) {
        log.info("GoogleGA4.getByUrl_Recover/domainName= " + domainName);
        log.info("GoogleGA4.getByUrl_Recover/propertyId= " + propertyId);
        log.info("GoogleGA4.getByUrl_Recover/urlPattern= " + urlPattern);
        log.info("GoogleGA4.getByUrl_Recover/patternMatchingType= " + patternMatchingType);
        log.info("GoogleGA4.getByUrl_Recover/startDate= " + startDate);
        log.info("GoogleGA4.getByUrl_Recover/endDate= " + endDate);
        log.info("GoogleGA4.getByUrl_Recover/metric= " + metricAlias);
        log.info("GoogleGA4.getByUrl_Recover/exception= " + e.getMessage());
        return null;
    }


    @Override
    @Retryable(value = {GA4getByUrlForMediumFailureException.class}, maxAttempts = 1)
    public <T> T getByUrlForMedium(
            String domainName,
            String propertyId,
            String urlPattern,
            GoogleAnalytics4PatternMatchingType patternMatchingType,
            GA4MediumType medium,
            String startDate,
            String endDate,
            String metricAlias) {
        try {
            RunReportRequest request =
                    GA4RequestFactory.buildRequestByUrlOrLandingUrlForMedium(
                            domainName, propertyId, urlPattern, patternMatchingType, medium, startDate, endDate, metricAlias);

            BatchRunReportsRequest batchRunReportsRequest = BatchRunReportsRequest.newBuilder().addRequests(request).build();
            BatchRunReportsResponse response = getBatchRunReports(domainName, batchRunReportsRequest);
            return GA4ReportingUtils.parseMetricFromResponse(response);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new GA4getByUrlForMediumFailureException();
        }
    }

    @Recover
    private <T> T getByUrlForMedium_Recover(
            GA4getByUrlForMediumFailureException e,
            String domainName,
            String propertyId,
            String urlPattern,
            GoogleAnalytics4PatternMatchingType patternMatchingType,
            GA4MediumType medium,
            String startDate,
            String endDate,
            String metricAlias) {
        log.info("GoogleGA4.getByUrlForMedium_Recover/domainName= " + domainName);
        log.info("GoogleGA4.getByUrlForMedium_Recover/propertyId= " + propertyId);
        log.info("GoogleGA4.getByUrlForMedium_Recover/urlPattern= " + urlPattern);
        log.info("GoogleGA4.getByUrlForMedium_Recover/patternMatchingType= " + patternMatchingType);
        log.info("GoogleGA4.getByUrlForMedium_Recover/Medium= " + medium);
        log.info("GoogleGA4.getByUrlForMedium_Recover/startDate= " + startDate);
        log.info("GoogleGA4.getByUrlForMedium_Recover/endDate= " + endDate);
        log.info("GoogleGA4.getByUrlForMedium_Recover/metric= " + metricAlias);
        log.info("GoogleGA4.getByUrlForMedium_Recover/exception= " + e.getMessage());
        return null;
    }


    // Get total metric value for one url for one medium, between start/end dates,
    @Override
    @Retryable(value = {GA4getByUrlByCanalOverTimeFailureException.class}, maxAttempts = 1)
    public <T> Map<String, T> getByUrlForMediumOverTime(
            String domainName,
            String propertyId,
            String urlPattern,
            GoogleAnalytics4PatternMatchingType patternMatchingType,
            GA4MediumType type,
            String startDate,
            String endDate,
            String metricAlias) {
        try {
            RunReportRequest request =
                    GA4RequestFactory.buildRequestByUrlOrLandingUrlForMediumOverTime(
                            domainName, propertyId, urlPattern, patternMatchingType, type, startDate, endDate, metricAlias, GA4TimeIncrement.DATE);


            BatchRunReportsRequest batchRunReportsRequest = BatchRunReportsRequest.newBuilder().addRequests(request).build();
            BatchRunReportsResponse response = getByBatchesSyncByShortPeriods(domainName, batchRunReportsRequest, startDate, endDate);
            List<String> dates = GA4ReportingUtils.buildListDatesFromDateRange(startDate, endDate);

            return GA4ReportingUtils.parseMetricMapFromResponse(response, dates);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new GA4getByUrlByCanalOverTimeFailureException();
        }
    }

    @Recover
    private <T> Map<String, T> getByUrlByCanalOverTime_Recover(
            GA4getByUrlByCanalOverTimeFailureException e,
            String domainName,
            String viewId,
            String urlPattern,
            GoogleAnalytics4PatternMatchingType patternMatchingType,
            GA4MediumType type,
            String startDate,
            String endDate,
            String metricAlias) {
        log.info("GoogleGA4.getByUrlByCanalOverTime_Recover/domainName= " + domainName);
        log.info("GoogleGA4.getByUrlByCanalOverTime_Recover/viewId= " + viewId);
        log.info("GoogleGA4.getByUrlByCanalOverTime_Recover/urlPattern= " + urlPattern);
        log.info("GoogleGA4.getByUrlByCanalOverTime_Recover/patternMatchingType= " + patternMatchingType);
        log.info("GoogleGA4.getByUrlByCanalOverTime_Recover/CanalType= " + type);
        log.info("GoogleGA4.getByUrlByCanalOverTime_Recover/startDate= " + startDate);
        log.info("GoogleGA4.getByUrlByCanalOverTime_Recover/endDate= " + endDate);
        log.info("GoogleGA4.getByUrlByCanalOverTime_Recover/metricAlias= " + metricAlias);
        log.info("GoogleGA4.getByUrlByCanalOverTime_Recover/exception= " + e.getMessage());
        return new HashMap<>();
    }


    // Get total metric value for list of URLs, between start/end dates
    // If getNOTResult=true, return metric value for all URLS not contained in the input list of URLs
    @Override
    @Retryable(value = {GA4getByUrlsFailureException.class}, maxAttempts = 1)
    public <T> Map<String, T> getByUrls(
            String domainName,
            String viewId,
            List<String> urls,
            String startDate,
            String endDate,
            String metricAlias,
            boolean getNOTResult) {
        try {
            RunReportRequest request =
                    GA4RequestFactory.buildRequestByUrlsOrLandingUrls(domainName, viewId, urls, startDate, endDate, metricAlias, getNOTResult);
            BatchRunReportsRequest batchRunReportsRequest = BatchRunReportsRequest.newBuilder().addRequests(request).build();
            BatchRunReportsResponse response = getBatchRunReports(domainName, batchRunReportsRequest);

            return GA4ReportingUtils.parseMetricMapByUrlsFromResponse(response, domainName, urls, getNOTResult);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new GAgetByUrlsFailureException();
        }
    }

    // If fetching Data for all Urls at once failed.
    // Instead: fetch Data Url by Url
    @Recover
    private <T> Map<String, T> getByUrls_Recover(
            GAgetByUrlsFailureException e,
            String domainName,
            String viewId,
            List<String> urls,
            String startDate,
            String endDate,
            String metricAlias,
            boolean getNOTResult) {
        log.info("GoogleGA.getByUrls_Recover/domainName= " + domainName);
        log.info("GoogleGA.getByUrls_Recover/viewId= " + viewId);
        log.info("GoogleGA.getByUrls_Recover/urls.size= " + urls.size());
        log.info("GoogleGA.getByUrls_Recover/getNOTResult= " + getNOTResult);
        log.info("GoogleGA.getByUrls_Recover/startDate= " + startDate);
        log.info("GoogleGA.getByUrls_Recover/endDate= " + endDate);
        log.info("GoogleGA.getByUrls_Recover/metric= " + metricAlias);
        log.info("GoogleGA.getByUrls_Recover/exception= " + e.getMessage());
        log.info("GoogleGA.getByUrls_Recover/ATTEMPTING FETCH URL by URL=");

        return new HashMap<>();

        /*
        Map<String, T> valuesMap = new HashMap<>();
        for (String url : urls) {
            try {
                T value = getByUrl(domainName, viewId, url, GoogleAnalyticsPatternMatchingType.EXACT, startDate, endDate, metricsAlias);
                valuesMap.put(url, value);
            } catch (Exception ex) {
                log.info("GoogleGA.getByUrls_Recover/EXCEPTION when fetching data ByUrl");
                log.info("GoogleGA.getByUrls_Recover/url=" + url);
                log.error(ex.getMessage(), ex);
            }
            ThreadUtils_INC.sleep(200);
        }

        return valuesMap;
         */

    }










    /**
     * private methods
     */
    // Fetch data by batch of 100 000 rows
    private BatchRunReportsResponse getBatchRunReports(String domainName, BatchRunReportsRequest reportRequests) {
        try {

            analyticsData = initializeAnalytics(domainName);
            BatchRunReportsResponse fullResponse = gaRequestExecutorService.executeRequest(domainName, analyticsData, reportRequests);
            //todo figure how to make multiple request

//            long limit = reportRequests.getRequests(0).getLimit();
//            long offset = reportRequests.getRequests(0).getOffset();

//            if (limit != 0)
//            BatchRunReportsResponse batchRunReportsResponse = null;
//            while(fullResponse.getReportsCount() >0 ) {
//                offset +=limit;
//                BatchRunReportsRequest reportsRequest = reportRequests.toBuilder().setRequests(0, reportRequests.getRequests(0).toBuilder().setOffset(offset).build()).build();
//
//
//                batchRunReportsResponse = gaRequestExecutorService.executeRequest(domainName, analyticsData, reportsRequest);
//                ReportingUtils.combineResponses(fullResponse, batchRunReportsResponse);
//            }
            if (!fullResponse.getReportsList().isEmpty() && fullResponse.getReportsList().get(0) != null) {
                log.info("getBatchRunReports/The number of rows for this batch : " + fullResponse.getReportsList().get(0).getRowsList().size());
            } else {
                log.info("getBatchRunReports/The number of rows for this batch : " + 0);
            }
            return fullResponse;
        } catch (Exception e) {
            log.error("getBatchRunReports/There was an error:" + e.getMessage(), e);
            throw new GA4getFailureException();
        }
    }


    private BetaAnalyticsDataClient initializeAnalytics(String domainName) {
        GoogleCredentials credentials = googleAuthenticationService.getGACredentials();

        try {
            BetaAnalyticsDataSettings betaAnalyticsDataSettings =
                    BetaAnalyticsDataSettings.newBuilder()
                            .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                            .build();
            return BetaAnalyticsDataClient.create(betaAnalyticsDataSettings);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private BatchRunReportsResponse getByBatchesSyncByShortPeriods(String domainName,
                                                              BatchRunReportsRequest request, String startSimpleDate, String endSimpleDate) {
        // Must be LOW, otherwise request timeout

        if (isOverTimeRequest(request) == Boolean.FALSE) {
            return getBatchRunReports(domainName, request);
        }
        LocalDate startDate = DateUtils.parseLocalDate(startSimpleDate);
        LocalDate endDate = DateUtils.parseLocalDate(endSimpleDate);
        log.info("getByBatchesSyncByShortPeriods/START fetching GA4 data by batches");

        // No batching by time - if period is too short
        if (ChronoUnit.DAYS.between(startDate, endDate) <= PERIOD_LENGTH) {
            return getBatchRunReports(domainName, request);
        }

        BatchRunReportsResponse finalResponse = null;
        LocalDate currentStartDate = LocalDate.from(startDate);
        LocalDate currentEndDate = LocalDate.from(startDate.plusDays(PERIOD_LENGTH - 1));

        while (ChronoUnit.DAYS.between(currentEndDate, endDate) >= 0) {
            log.info("getByBatchesSyncByShortPeriods/Iteration date range : " + currentStartDate + " ==> " + currentEndDate);
            // Execute request
            DateRange newDateRange =
                    new GA4DateRangeFactory().get(currentStartDate.toString(), currentEndDate.toString());
            RunReportRequest runReportRequest = request.getRequests(0).toBuilder().setDateRanges(0, newDateRange).build();
            BatchRunReportsRequest newRequest = BatchRunReportsRequest.newBuilder().addRequests(runReportRequest).build();
            BatchRunReportsResponse newResponse = getBatchRunReports(domainName, newRequest);

            // Save response
            if (finalResponse == null) {
                finalResponse = newResponse;
            } else {
                GA4ReportingUtils.combineResponses(finalResponse, newResponse);
            }

            // Stop if has reach end of date range
            if (ChronoUnit.DAYS.between(currentEndDate, endDate) == 0) {
                break;
            }

            // Find remaining days
            long newTimeDiffStart = ChronoUnit.DAYS.between(currentStartDate, endDate);
            long daysToAddStart = newTimeDiffStart >= 1 ? 1 : 0;
            currentStartDate = LocalDate.from(currentEndDate.plusDays(daysToAddStart));

            long newTimeDiffEnd = ChronoUnit.DAYS.between(currentEndDate, endDate);
            long daysToAddEnd = newTimeDiffEnd >= PERIOD_LENGTH ? PERIOD_LENGTH : newTimeDiffEnd;
            currentEndDate = LocalDate.from(currentEndDate.plusDays(daysToAddEnd));
            ThreadUtils_INC.sleep(1000, 2000);
        }

        log.info("getByBatchesSyncByShortPeriods/END fetching GA4 data by batches");
        if (finalResponse != null && !finalResponse.getReportsList().isEmpty() && finalResponse.getReports(0) != null
                && !finalResponse.getReports(0).getRowsList().isEmpty()) {
            log.info("getByBatchesSyncByShortPeriods/The number of total rows : " + finalResponse.getReports(0).getRowsList().size());
        } else {
            log.info("getByBatchesSyncByShortPeriods/The number of total rows : " + 0);
        }
        return finalResponse;
    }

    private boolean isOverTimeRequest(BatchRunReportsRequest request) {
        List<Dimension> dimensions = request.getRequests(0).getDimensionsList();
        if (dimensions == null || dimensions.isEmpty()) {
            return true;
        } else {
            for (Dimension dimension : dimensions) {
                if (dimension.getName().equals(DATE_DIMENSION) || dimension.getName().equals(WEEK_DIMENSION)) {
                    return true;
                }
            }
        }
        return false;
    }



}
