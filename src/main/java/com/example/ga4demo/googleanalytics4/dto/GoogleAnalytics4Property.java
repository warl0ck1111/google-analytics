package com.example.ga4demo.googleanalytics4.dto;

import com.google.protobuf.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GoogleAnalytics4Property {
    private String name;
    private String displayName;
    private String propertyId;
    private String websiteUrl;
    private Boolean ecommerceTracking;
    private Timestamp createTime;
    private Timestamp updateTime;

}
