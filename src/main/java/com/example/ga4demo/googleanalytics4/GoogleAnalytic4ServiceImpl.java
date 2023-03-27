package com.example.ga4demo.googleanalytics4;

import com.example.ga4demo.googleanalytics4.core.dataflows.GA4ManagementDataFlow;
import com.example.ga4demo.googleanalytics4.core.dataflows.GA4ReportingDataFlow;
import com.example.ga4demo.googleanalytics4.core.types.GoogleAnalytics4PatternMatchingType;
import com.example.ga4demo.googleanalytics4.dto.GoogleAnalytics4Account;
import com.example.ga4demo.googleanalytics4.core.types.GA4MediumType;
import com.example.ga4demo.googleanalytics4.core.types.GoogleAnalytics4MetricType;
import com.example.ga4demo.googleanalytics4.core.types.GoogleAnalytics4SinceDateType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
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
    public Map<String, Long> getSessionsNbOverTime(String domainName, String propertyId, LocalDate startDate, LocalDate endDate) {
        String startDateStr = startDate.toString();
        String endDateStr = endDate.toString();
        return reportingDataFlow.getOverTime(domainName,
                propertyId, startDateStr, endDateStr, GoogleAnalytics4MetricType.SESSIONS.getValue());
    }



    @Override
    public Long getSessionsNbForUrl(String domainName, String propertyId, String urlPattern, GoogleAnalytics4PatternMatchingType patternMatchingType, LocalDate startDate, LocalDate endDate) {
        String startDateStr = startDate.toString();
        String endDateStr = endDate.toString();
        return reportingDataFlow.getByUrl(
                domainName, propertyId, urlPattern, patternMatchingType, startDateStr, endDateStr, GoogleAnalytics4MetricType.SESSIONS.getValue());
    }

    @Override
    public Long getSessionsNbForUrlSince(String domainName, String propertyId, String urlPattern, GoogleAnalytics4PatternMatchingType patternMatchingType, GoogleAnalytics4SinceDateType sinceDate) {
        String startDate = sinceDate.getLocalDate().toString();
        String endDate = LocalDate.now().toString();
        return reportingDataFlow.getByUrl(
                domainName, propertyId, urlPattern, patternMatchingType, startDate, endDate, GoogleAnalytics4MetricType.SESSIONS.getValue());
    }

    @Override
    public Map<String, Long> getSessionsNbForUrlOverTime(
            String domainName, String propertyId, String urlPattern, GoogleAnalytics4PatternMatchingType patternMatchingType, LocalDate startDate, LocalDate endDate) {
        String startDateStr = startDate.toString();
        String endDateStr = endDate.toString();
        return reportingDataFlow.getByUrlOverTime(
                domainName, propertyId, urlPattern, patternMatchingType, startDateStr, endDateStr, GoogleAnalytics4MetricType.SESSIONS.getValue());
    }


    @Override
    public Long getSessionsNbFOrUrlForMedium(
            String domainName, String propertyId, String urlPattern, GoogleAnalytics4PatternMatchingType patternMatchingType, GA4MediumType type, LocalDate startDate, LocalDate endDate) {
        String startDateStr = startDate.toString();
        String endDateStr = endDate.toString();
        return reportingDataFlow.getByUrlForMedium(
                domainName, propertyId, urlPattern, patternMatchingType, type, startDateStr, endDateStr, GoogleAnalytics4MetricType.SESSIONS.getValue());
    }

    @Override
    public Long getSessionsNbForUrlForMediumSince(
            String domainName, String propertyId, String urlPattern, GoogleAnalytics4PatternMatchingType patternMatchingType, GA4MediumType type, GoogleAnalytics4SinceDateType sinceDate) {
        String startDate = sinceDate.getLocalDate().toString();
        String endDate = LocalDate.now().toString();
        return reportingDataFlow.getByUrlForMedium(
                domainName, propertyId, urlPattern, patternMatchingType, type, startDate, endDate, GoogleAnalytics4MetricType.SESSIONS.getValue());
    }

    @Override
    public Map<String, Long> getSessionsNbForUrlForMediumOverTime(
            String domainName, String propertyId, String urlPattern, GoogleAnalytics4PatternMatchingType patternMatchingType, GA4MediumType mediumType, LocalDate startDate, LocalDate endDate) {
        String startDateStr = startDate.toString();
        String endDateStr = endDate.toString();
        return reportingDataFlow.getByUrlForMediumOverTime(
                domainName, propertyId, urlPattern, patternMatchingType, mediumType, startDateStr, endDateStr, GoogleAnalytics4MetricType.SESSIONS.getValue());
    }

    @Override
    public Map<String, Long> getSessionsNbByUrls(
            String domainName, String propertyId, List<String> urls, LocalDate startDate, LocalDate endDate, boolean getNOTResult) {
        String startDateStr = startDate.toString();
        String endDateStr = endDate.toString();
        return reportingDataFlow.getByUrls(
                domainName, propertyId, urls, startDateStr, endDateStr, GoogleAnalytics4MetricType.SESSIONS.getValue(), getNOTResult);
    }

    @Override
    public Map<String, Long> getSessionsNbByUrlsSince(
            String domainName, String propertyId, List<String> urls, GoogleAnalytics4SinceDateType sinceDate, boolean getNOTResult) {
        String startDate = sinceDate.getLocalDate().toString();
        String endDate = LocalDate.now().toString();
        return reportingDataFlow.getByUrls(
                domainName, propertyId, urls, startDate, endDate, GoogleAnalytics4MetricType.SESSIONS.getValue(), getNOTResult);
    }

    @Override
    public Map<String, Map<String, Long>> getSessionsNbByUrlsOverTime(
            String domainName, String propertyId, List<String> urls, LocalDate startDate, LocalDate endDate, boolean getNOTResult) {
        String startDateStr = startDate.toString();
        String endDateStr = endDate.toString();
        return reportingDataFlow.getByUrlsOverTime(
                domainName, propertyId, urls, startDateStr, endDateStr, GoogleAnalytics4MetricType.SESSIONS.getValue(), getNOTResult);
    }

    @Override
    public Map<String, Map<String, Long>> getSessionsNbByUrlsForMediumOverTime(
            String domainName, String propertyId, List<String> urls, GA4MediumType medium, LocalDate startDate, LocalDate endDate, boolean getNOTResult) {
        String startDateStr = startDate.toString();
        String endDateStr = endDate.toString();
        return reportingDataFlow.getByUrlsOverTimeForMedium(
                domainName, propertyId, urls, medium, startDateStr, endDateStr, GoogleAnalytics4MetricType.SESSIONS.getValue(), getNOTResult);
    }

    @Override
    public Map<String, Map<String, Long>> getSessionsNbByUrlsForMediumOverTimeSince(
            String domainName, String propertyId, List<String> urls, GA4MediumType medium, GoogleAnalytics4SinceDateType sinceDate, boolean getNOTResult) {
        String startDate = sinceDate.getLocalDate().toString();
        String endDate = LocalDate.now().toString();
        return reportingDataFlow.getByUrlsOverTimeForMedium(
                domainName, propertyId, urls, medium, startDate, endDate, GoogleAnalytics4MetricType.SESSIONS.getValue(), getNOTResult);
    }

    @Override
    public Map<String, Map<String, Long>> getSessionsNbByUrlForMediumOverTime(
            String domainName, String propertyId, String urlPattern, GoogleAnalytics4PatternMatchingType patternMatchingType, GA4MediumType medium, LocalDate startDate, LocalDate endDate, boolean getNOTResult) {
        String startDateStr = startDate.toString();
        String endDateStr = endDate.toString();
        return reportingDataFlow.getByUrlOverTimeForMedium(
                domainName, propertyId, urlPattern, patternMatchingType, medium, startDateStr, endDateStr, GoogleAnalytics4MetricType.SESSIONS.getValue());
    }

    @Override
    public Map<String, Map<String, Long>> getSessionsNbByUrlForMediumOverTimeSince(
            String domainName, String propertyId, String urlPattern, GoogleAnalytics4PatternMatchingType patternMatchingType, GA4MediumType medium, GoogleAnalytics4SinceDateType sinceDate, boolean getNOTResult) {
        String startDate = sinceDate.getLocalDate().toString();
        String endDate = LocalDate.now().toString();
        return reportingDataFlow.getByUrlOverTimeForMedium(
                domainName, propertyId, urlPattern, patternMatchingType, medium, startDate, endDate, GoogleAnalytics4MetricType.SESSIONS.getValue());
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
