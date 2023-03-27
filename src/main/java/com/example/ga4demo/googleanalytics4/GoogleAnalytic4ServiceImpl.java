package com.example.ga4demo.googleanalytics4;

import com.example.ga4demo.googleanalytics4.dataflows.GA4ManagementDataFlow;
import com.example.ga4demo.googleanalytics4.dataflows.GA4ReportingDataFlow;
import com.example.ga4demo.googleanalytics4.dto.GoogleAnalytics4Account;
import com.example.ga4demo.googleanalytics4.types.GA4MediumType;
import com.example.ga4demo.googleanalytics4.types.GoogleAnalytics4MetricType;
import com.example.ga4demo.googleanalytics4.types.GoogleAnalytics4SinceDateType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class GoogleAnalytic4ServiceImpl implements GoogleAnalytic4Service{

    @Autowired
    private GA4ManagementDataFlow managementDataFlow;

    @Autowired
    private GA4ReportingDataFlow reportingDataFlow;



    @Override
    public List<GoogleAnalytics4Account> getAccounts() {
        log.info("getAccounts/");
        return managementDataFlow.getAccounts();
    }

    /**
     * SESSIONS APIs
     */
    @Override
    public Long getSessionsNb(String domainName, String propertyId, LocalDate startDate, LocalDate endDate) {
        String startDateStr = startDate.toString();
        String endDateStr = endDate.toString();
        log.info("getSessionsNb/domainName="+ domainName);
        log.info("getSessionsNb/propertyId="+ propertyId);
        log.info("getSessionsNb/startDate="+ startDate);
        log.info("getSessionsNb/endDate="+ endDate);

        return reportingDataFlow.get(domainName,
                propertyId, startDateStr, endDateStr, GoogleAnalytics4MetricType.SESSIONS.getValue());
    }



    @Override
    public Long getSessionsNbSince(String domainName, String propertyId, GoogleAnalytics4SinceDateType sinceDate) {
        String startDate = sinceDate.getLocalDate().toString();
        String endDate = LocalDate.now().toString();
        log.info("getSessionsNbSince/domainName="+ domainName);
        log.info("getSessionsNbSince/propertyId="+ propertyId);
        log.info("getSessionsNbSince/sinceDate="+ sinceDate);

        return reportingDataFlow.get(domainName, propertyId, startDate, endDate, GoogleAnalytics4MetricType.SESSIONS.getValue());
    }


    @Override
    public Set<String> getPropertyUrlsForMedium(String domainName, String propertyId, GA4MediumType mediumType){
        log.info("getPropertyUrlsForMedium/domainName="+ domainName);
        log.info("getPropertyUrlsForMedium/propertyId="+ propertyId);
        log.info("getPropertyUrlsForMedium/mediumType="+ mediumType);
        return reportingDataFlow.getUrls(domainName, propertyId, mediumType, null);
    }

    @Override
    public Set<String> getPropertyUrlsForMediumSince(String domainName, String propertyId, GA4MediumType mediumType, GoogleAnalytics4SinceDateType sinceDate) {
        return reportingDataFlow.getUrls(domainName, propertyId, mediumType, sinceDate);
    }

    @Override
    public Set<String> getPropertyUrlsSince(String domainName, String propertyId, GoogleAnalytics4SinceDateType sinceDate) {
        return reportingDataFlow.getUrls(domainName,propertyId, null, sinceDate);
    }


}
