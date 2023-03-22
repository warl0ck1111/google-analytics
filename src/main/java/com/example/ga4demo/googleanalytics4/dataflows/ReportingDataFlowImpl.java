package com.example.ga4demo.googleanalytics4.dataflows;

import com.example.ga4demo.googleanalytics4.GoogleAuthenticationService;
import com.example.ga4demo.googleanalytics4.exceptions.GAgetFailureException;
import com.google.analytics.data.v1beta.*;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;

@Component
@Slf4j
public class ReportingDataFlowImpl implements ReportingDataFlow {

    @Autowired
    private GoogleAuthenticationService googleAuthenticationService;



    @Override
    @Retryable(value = {GAgetFailureException.class}, maxAttempts = 1)
    public <T>T get(String domainName, String propertyId, String startDate, String endDate, String metricAlias) {
        try {
            RunReportRequest runReportRequest =
                    RunReportRequest.newBuilder()
                            .setProperty("properties/" + propertyId)
//                            .addDimensions(Dimension.newBuilder().setName(dimension))
                            .addMetrics(Metric.newBuilder().setName(metricAlias))
                            .addDateRanges(DateRange.newBuilder().setStartDate(startDate).setEndDate(endDate))
                            .build();


            BetaAnalyticsDataClient betaAnalyticsDataClient = initializeAnalytics(domainName);
            RunReportResponse runReportResponse = getRunReport(betaAnalyticsDataClient, runReportRequest);

            return (T) runReportResponse;
        }catch (Exception e){
            log.info("There was an exception: {}", e);
        }
        return null;
    }


    private BatchRunReportsResponse getBatchRunReport(String domainName, BatchRunReportsRequest request) {
        try {

            BetaAnalyticsDataClient analyticsData = initializeAnalytics(domainName);

            BatchRunReportsResponse batchRunReportsResponse = getBatchRunReports(analyticsData, request);

            return batchRunReportsResponse; //todo: make generic
        } catch (Exception e) {
            log.error("getBatchRunReports/There was an error:"+e.getMessage(), e);
            throw new GAgetFailureException();
        }
    }



    @Recover
    private BatchRunReportsResponse getRunReport_Recover(
            GAgetFailureException e, String domainName, String propertyId,String startDate, String endDate, String metricAlias) {
        log.info("GoogleGA.getRunReport_Recover/domainName= " + domainName);
        log.info("GoogleGA.getRunReport_Recover/propertyId= " + propertyId);
        log.info("GoogleGA.getRunReport_Recover/startDate= " + startDate);
        log.info("GoogleGA.getRunReport_Recover/endDate= " + endDate);
        log.info("GoogleGA.getRunReport_Recover/metricAlias= " + metricAlias);
        log.info("GoogleGA.getRunReport_Recover/exception= " + e.getMessage());
        return null;
    }


    private BatchRunReportsResponse getBatchRunReports(BetaAnalyticsDataClient analyticsDataClient, BatchRunReportsRequest request){
        return analyticsDataClient.batchRunReports(request);
    }
private RunReportResponse getRunReport(BetaAnalyticsDataClient analyticsDataClient, RunReportRequest request){
        return analyticsDataClient.runReport(request);
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
