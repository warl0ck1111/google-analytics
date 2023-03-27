package com.example.ga4demo.googleanalytics4.customlogic;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GoogleAnalyticsURLsRequest {

    private String domainName;
    private List<String> urls;

}