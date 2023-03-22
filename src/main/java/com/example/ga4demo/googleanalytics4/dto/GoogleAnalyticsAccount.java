package com.example.ga4demo.googleanalytics4.dto;

import com.google.protobuf.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class GoogleAnalyticsAccount {
    private String name;
    private String displayName;
    private String regionalCode;
    private Timestamp createdTime;
    private Timestamp updatedTime;
}
