package com.example.ga4demo.googleanalytics4.customlogic;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GoogleAnalyticsURLRequest {

    private String domainName;
    private String url;

}