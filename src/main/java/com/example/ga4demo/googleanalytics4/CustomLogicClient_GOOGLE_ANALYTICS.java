package com.example.ga4demo.googleanalytics4;

import com.example.ga4demo.googleanalytics4.customlogic.CustomLogicClientConfig;
import com.example.ga4demo.googleanalytics4.customlogic.GoogleAnalyticsURLRequest;
import com.example.ga4demo.googleanalytics4.customlogic.GoogleAnalyticsURLsRequest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * Created by ashish on 10/5/17.
 */
@FeignClient(url = "${feign.customlogic.url}", name = "customlogic", configuration = CustomLogicClientConfig.class)
public interface CustomLogicClient_GOOGLE_ANALYTICS {

    @RequestMapping(value = "/googleanalytics/url", method = RequestMethod.POST)
    String findCustomGAUrl(GoogleAnalyticsURLRequest request);

    @RequestMapping(value = "/googleanalytics/urls", method = RequestMethod.POST)
    Map<String, String> findCustomGAUrls(GoogleAnalyticsURLsRequest request);

}