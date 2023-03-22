package com.example.ga4demo.googleanalytics4.dataflows;

import com.example.ga4demo.googleanalytics4.dto.GoogleAnalyticsAccount;
import com.google.analytics.admin.v1beta.Account;
import com.google.analytics.admin.v1beta.AnalyticsAdminServiceClient;
import com.google.analytics.admin.v1beta.ListAccountsRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Component
public class ManagementDataFlowImpl implements ManagementDataFlow {

    @Override
    public List<GoogleAnalyticsAccount> getAccounts() {
        List<GoogleAnalyticsAccount> googleAnalyticsAccounts = new ArrayList<>();
        try {
            AnalyticsAdminServiceClient analyticsAdmin = initializeAnalytics(""); //todo: get credentials for domainNAme
            if (analyticsAdmin != null) {
                AnalyticsAdminServiceClient.ListAccountsPagedResponse response =
                        analyticsAdmin.listAccounts(ListAccountsRequest.newBuilder().build());
                System.out.println("the response");
                System.out.println(response);
                for (AnalyticsAdminServiceClient.ListAccountsPage page : response.iteratePages()) {
                    for (Account account : page.iterateAll()) {
                        googleAnalyticsAccounts.add(new GoogleAnalyticsAccount(account.getName(), account.getDisplayName(),
                                account.getRegionCode(), account.getCreateTime(), account.getUpdateTime()));

                        //todo: change to use log.info(...)
                        System.out.printf("Account name: %s%n", account.getName());
                        System.out.printf("Display name: %s%n", account.getDisplayName());
                        System.out.printf("Country code: %s%n", account.getRegionCode());
                        System.out.printf("Create time: %s%n", account.getCreateTime().getSeconds());
                        System.out.printf("Update time: %s%n", account.getUpdateTime().getSeconds());
                        System.out.println();
                    }
                }
            }
            return googleAnalyticsAccounts;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public AnalyticsAdminServiceClient initializeAnalytics(String domainName) {

        //todo: figure out how to impersonate account and use the domainName variable passed
        try (AnalyticsAdminServiceClient analyticsAdmin = AnalyticsAdminServiceClient.create()) {
            // Calls listAccounts() method of the Google Analytics Admin API and prints
            // the response for each account.
            return analyticsAdmin;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
