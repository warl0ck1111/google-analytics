package com.example.ga4demo.googleanalytics4.customlogic;


import java.util.List;
import java.util.Map;

public interface CustomLogicService_GOOGLE_ANALYTICS {

    String findCustomGAUrl(String url, String domainName);

    Map<String, String> findCustomGAUrls(List<String> urls, String domainName);

}