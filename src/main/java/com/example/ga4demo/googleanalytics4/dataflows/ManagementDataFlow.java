package com.example.ga4demo.googleanalytics4.dataflows;

import com.example.ga4demo.googleanalytics4.dto.GoogleAnalyticsAccount;
import com.google.analytics.admin.v1beta.AnalyticsAdminServiceClient;

import java.util.List;

public interface ManagementDataFlow {

    List<GoogleAnalyticsAccount> getAccounts();

    AnalyticsAdminServiceClient initializeAnalytics(String domainName);

}
