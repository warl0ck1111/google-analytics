package com.example.ga4demo.googleanalytics4.dataflows;


import com.google.analytics.data.v1beta.BatchRunReportsRequest;
import com.google.analytics.data.v1beta.BatchRunReportsResponse;
import com.google.analytics.data.v1beta.BetaAnalyticsDataClient;

public interface GA4RequestExecutorService {

    BatchRunReportsResponse executeRequest(String domainName, BetaAnalyticsDataClient analytics, BatchRunReportsRequest request);
}
