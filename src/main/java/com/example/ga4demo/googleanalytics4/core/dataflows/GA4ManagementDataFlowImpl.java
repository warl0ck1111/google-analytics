package com.example.ga4demo.googleanalytics4.core.dataflows;

import com.google.analytics.admin.v1beta.*;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.example.ga4demo.googleanalytics4.GoogleAuthenticationService2;
import com.example.ga4demo.googleanalytics4.dto.GoogleAnalytics4Account;
import com.example.ga4demo.googleanalytics4.dto.GoogleAnalytics4Property;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.google.analytics.admin.v1beta.AnalyticsAdminServiceClient.*;

@Component
@Slf4j
public class GA4ManagementDataFlowImpl implements GA4ManagementDataFlow {

    @Autowired
    private GoogleAuthenticationService2 googleAuthenticationService;

    @Override
    public List<GoogleAnalytics4Account> getAccounts() {
        return getGoogleAnalytics4Accounts();
    }

    private List<GoogleAnalytics4Account> getGoogleAnalytics4Accounts() {
        List<GoogleAnalytics4Account> googleAnalytics4AccountList = new ArrayList<>();
        try {
            ListAccountsPagedResponse response = listAccount();
            response.iteratePages().forEach(listAccountsPage -> listAccountsPage.getResponse().getAccountsList().forEach(account -> {
                List<GoogleAnalytics4Property> googleAnalytics4Properties = null;
                try {
                    googleAnalytics4Properties = listGA4AccountProperties(account.getName());
                    googleAnalytics4AccountList.add(new GoogleAnalytics4Account(account.getName(), googleAnalytics4Properties));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }));


            return googleAnalytics4AccountList;
        } catch (Exception e) {
            log.error("getGoogleAnalytics4Accounts/there was an error:" + e);
            throw new RuntimeException(e);
        }
    }

    private ListAccountsPagedResponse listAccount() {
        try (AnalyticsAdminServiceClient analyticsAdmin = create()) {
            ListAccountsPagedResponse response = analyticsAdmin.listAccounts(ListAccountsRequest.newBuilder().build());
            log.info("listAccount/"+response);
            return response;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<GoogleAnalytics4Property> listGA4AccountProperties(String accountName) {
        try (AnalyticsAdminServiceClient analyticsAdmin = create()) {
            //account name --> "accounts/accountId"
            List<GoogleAnalytics4Property> googleAnalytics4Properties = new ArrayList<>();
            log.info("listGA4AccountProperties/");
            if (analyticsAdmin != null && Strings.isNotBlank(accountName)) {
                ListPropertiesPagedResponse response =
                        analyticsAdmin.listProperties(ListPropertiesRequest.newBuilder().setFilter("parent:" + accountName).build());
                log.info("listGA4AccountProperties/the response for account ID:" + accountName);

                for (ListPropertiesPage page : response.iteratePages()) {
                    for (Property property : page.iterateAll()) {
                        String propertyId = property.getName().replaceAll("properties/", "");
                        googleAnalytics4Properties.add(new GoogleAnalytics4Property(property.getName(), property.getDisplayName(), propertyId, null, null, property.getCreateTime(), property.getUpdateTime()));
                    }
                }
                return googleAnalytics4Properties;
            }
        } catch (Exception e) {
            log.error("listGA4AccountProperties/there was an error: "+e.getMessage());
            throw new RuntimeException(e);
        }
        return new ArrayList<>();
    }

    @Override
    public AnalyticsAdminServiceClient initializeAnalytics(String domainName) {
        GoogleCredentials gaCredentials = googleAuthenticationService.getGACredentials();
        if (gaCredentials != null) {
            AnalyticsAdminServiceSettings analyticsSettings = getAnalyticsSettings(gaCredentials);
            try (AnalyticsAdminServiceClient analyticsAdmin = create(analyticsSettings)) {
                return analyticsAdmin;
            } catch (IOException e) {
                log.error("There was an error getting credentials");
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    private static AnalyticsAdminServiceSettings getAnalyticsSettings(GoogleCredentials gaCredentials) {
        try {
            return AnalyticsAdminServiceSettings.newBuilder()
                            .setCredentialsProvider(FixedCredentialsProvider.create(gaCredentials))
                            .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
