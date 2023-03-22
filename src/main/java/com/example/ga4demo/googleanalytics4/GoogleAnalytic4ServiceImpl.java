package com.example.ga4demo.googleanalytics4;

import com.example.ga4demo.googleanalytics4.dataflows.ManagementDataFlow;
import com.example.ga4demo.googleanalytics4.dataflows.ReportingDataFlow;
import com.example.ga4demo.googleanalytics4.dto.GoogleAnalyticsAccount;
import com.example.ga4demo.googleanalytics4.types.GoogleAnalyticsMetricType;
import com.example.ga4demo.googleanalytics4.types.GoogleAnalyticsSinceDateType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class GoogleAnalytic4ServiceImpl implements GoogleAnalyticService{

    @Autowired
    private ManagementDataFlow managementDataFlow;

    @Autowired
    private ReportingDataFlow reportingDataFlow;


    @Override
    public List<GoogleAnalyticsAccount> getAccounts() {
        return managementDataFlow.getAccounts();
    }

    /**
     * SESSIONS APIs
     */
    @Override
    public Long getSessionsNb(String domainName, String viewId, LocalDate startDate, LocalDate endDate) {
        String startDateStr = startDate.toString();
        String endDateStr = endDate.toString();
        return reportingDataFlow.get(domainName,
                viewId, startDateStr, endDateStr, GoogleAnalyticsMetricType.SESSIONS.getValue());
    }


//    @Override
//    public Map<String, Long> getSessionsNbOverTime(String domainName, String propertyId, LocalDate startDate, LocalDate endDate) {
//        String startDateStr = startDate.toString();
//        String endDateStr = endDate.toString();
//        return reportingDataFlow.getOverTime(domainName,
//                propertyId, startDateStr, endDateStr, GoogleAnalyticsMetricType.SESSIONS.getValue());
//    }

    @Override
    public Long getSessionsNbSince(String domainName, String viewId, GoogleAnalyticsSinceDateType sinceDate) {
        String startDate = sinceDate.getLocalDate().toString();
        String endDate = LocalDate.now().toString();
        return reportingDataFlow.get(domainName, viewId, startDate, endDate, GoogleAnalyticsMetricType.SESSIONS.getValue());
    }


}
