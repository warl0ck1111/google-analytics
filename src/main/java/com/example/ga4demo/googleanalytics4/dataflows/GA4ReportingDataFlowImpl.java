package com.example.ga4demo.googleanalytics4.dataflows;

import com.example.ga4demo.googleanalytics4.exceptions.GA4getOverTimeFailureException;
import com.example.ga4demo.googleanalytics4.exceptions.GA4getUrlsFailureException;
import com.example.ga4demo.googleanalytics4.types.GA4MediumType;
import com.example.ga4demo.googleanalytics4.types.GoogleAnalytics4SinceDateType;
import com.google.analytics.data.v1beta.*;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.example.ga4demo.googleanalytics4.GoogleAuthenticationService2;
import com.example.ga4demo.googleanalytics4.exceptions.GA4getFailureException;
import com.example.ga4demo.googleanalytics4.factories.GA4RequestFactory;
import com.example.ga4demo.googleanalytics4.utils.ReportingUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Component
@Slf4j
public class GA4ReportingDataFlowImpl implements GA4ReportingDataFlow {

    @Autowired
    private GoogleAuthenticationService2 googleAuthenticationService;
    @Autowired
    private GA4RequestExecutorService gaRequestExecutorService;

    private BetaAnalyticsDataClient analyticsData;


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

            return ReportingUtils.parseMetricFromResponse(batchRunReportResponse);
        } catch (Exception e) {
            log.info("get/There was an exception: {}", e);
        }
        return null;
    }

    @Recover
    private BatchRunReportsResponse get_Recover(
            GA4getFailureException e, String domainName, String propertyId, String startDate, String endDate, String metricAlias) {
        log.info("GoogleGA.get_Recover/domainName= " + domainName);
        log.info("GoogleGA.get_Recover/propertyId= " + propertyId);
        log.info("GoogleGA.get_Recover/startDate= " + startDate);
        log.info("GoogleGA.get_Recover/endDate= " + endDate);
        log.info("GoogleGA.get_Recover/metricAlias= " + metricAlias);
        log.info("GoogleGA.get_Recover/exception= " + e.getMessage());
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
            return ReportingUtils.parseSetDimensionsFromResponse(response);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new GA4getUrlsFailureException();
        }
    }

    @Recover
    private Set<String> getUrls_Recover(GA4getUrlsFailureException e, String domainName, String propertyId, GA4MediumType mediumType, GoogleAnalytics4SinceDateType sinceDate) {
        log.info("GoogleGA.getUrls_Recover/domainName= " + domainName);
        log.info("GoogleGA.getUrls_Recover/propertyId= " + propertyId);
        log.info("GoogleGA.getUrls_Recover/sinceDate= " + sinceDate);
        log.info("GoogleGA.getUrls_Recover/canalType= " + mediumType);
        log.info("GoogleGA.getUrls_Recover/exception= " + e.getMessage());
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
                            propertyId, startDate, endDate, metricAlias, TimeIncrement.DATE);

            GetReportsResponse response = getByBatchesSyncByShortPeriods(domainName, request, startDate, endDate);

            List<String> dates = ReportingUtils.buildListDatesFromDateRange(startDate, endDate);
            return ReportingUtils.parseMetricMapFromResponse(response, dates);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new GAgetOverTimeFailureException();
        }
    }

    @Recover
    private <T> Map<String, T> getOverTime_Recover(
            GA4getOverTimeFailureException e, String domainName, String propertyId, String startDate, String endDate, String metricAlias) {
        log.info("GoogleGA.getOverTime_Recover/domainName= " + domainName);
        log.info("GoogleGA.getOverTime_Recover/propertyId= " + propertyId);
        log.info("GoogleGA.getOverTime_Recover/startDate= " + startDate);
        log.info("GoogleGA.getOverTime_Recover/endDate= " + endDate);
        log.info("GoogleGA.getOverTime_Recover/metric= " + metricAlias);
        log.info("GoogleGA.getOverTime_Recover/exception= " + e.getMessage());
        return new HashMap<>();
    }






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




    //todo:
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

}
