package com.example.ga4demo.googleanalytics4.dataflows;

import com.google.analytics.admin.v1beta.AnalyticsAdminServiceClient;
import com.example.ga4demo.googleanalytics4.dto.GoogleAnalytics4Account;

import java.util.List;

public interface GA4ManagementDataFlow {

    List<GoogleAnalytics4Account> getAccounts();

    AnalyticsAdminServiceClient initializeAnalytics(String domainName);

}
