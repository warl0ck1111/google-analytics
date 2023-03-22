package com.example.ga4demo.googleanalytics4.dataflows;


import com.google.analytics.data.v1beta.BatchRunReportsResponse;

public interface ReportingDataFlow {

    <T>T get(String domainName, String propertyId, String startDate, String endDate, String metricAlias);
//    BatchRunReportsResponse getBatchRunReports(String domainName, String propertyId);

//    BatchRunReportsResponse getRunReport(String domainName, String propertyId, String startDate, String endDate, String metricAlias);
//
//    BatchRunReportsResponse getRunPivotReport(String domainName, String propertyId, String startDate, String endDate, String metricAlias);
}
