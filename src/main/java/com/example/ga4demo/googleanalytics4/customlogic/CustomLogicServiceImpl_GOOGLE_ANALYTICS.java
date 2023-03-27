package com.example.ga4demo.googleanalytics4.customlogic;

import com.example.ga4demo.googleanalytics4.CustomLogicClient_GOOGLE_ANALYTICS;
import com.example.ga4demo.googleanalytics4.CustomLogicService_GOOGLE_ANALYTICS;
import com.example.ga4demo.googleanalytics4.exceptions.GoogleAnalytics4CustomUrlFailureException;
import com.example.ga4demo.googleanalytics4.exceptions.GoogleAnalytics4CustomUrlsFailureException;
import com.example.ga4demo.googleanalytics4.utils.DomainUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class CustomLogicServiceImpl_GOOGLE_ANALYTICS implements CustomLogicService_GOOGLE_ANALYTICS {

    @Autowired
    private CustomLogicClient_GOOGLE_ANALYTICS customLogicClient;

    @Override
    @Retryable(value = {GoogleAnalytics4CustomUrlFailureException.class}, backoff = @Backoff(delay = 10000))
    public String findCustomGAUrl(String url, String domainName) {
        log.info("findCustomGAUrl/");
        try {
            return customLogicClient.findCustomGAUrl(new GoogleAnalyticsURLRequest(domainName, url));
        } catch (Exception e) {
            log.info("findCustomGAUrl/EXCEPTION e = " + e);
            throw new GoogleAnalytics4CustomUrlFailureException();
        }
    }

    @Recover
    public String findCustomGAUrl_RECOVER(GoogleAnalytics4CustomUrlFailureException e, String url, String domainName) {
        log.info("findCustomGAUrl_RECOVER/");
        return DomainUtils.getUrlPath(url, domainName);
    }

    @Override
    @Retryable(value = {GoogleAnalytics4CustomUrlsFailureException.class}, backoff = @Backoff(delay = 10000))
    public Map<String, String> findCustomGAUrls(List<String> urls, String domainName) {
        log.info("findCustomGAUrls/");
        try {
            return customLogicClient.findCustomGAUrls(new GoogleAnalyticsURLsRequest(domainName, urls));
        } catch (Exception e) {
            log.info("findCustomGAUrl/EXCEPTION e = " + e);
            throw new GoogleAnalytics4CustomUrlsFailureException();
        }
    }

    @Recover
    public Map<String, String> findCustomGAUrls_RECOVER(GoogleAnalytics4CustomUrlsFailureException e, List<String> urls, String domainName) {
        log.info("findCustomGAUrls_RECOVER/");
        Map<String, String> result = new HashMap<>();
        for (String url : urls) {
            result.put(url, DomainUtils.getUrlPath(url, domainName));
        }
        return result;
    }

}
