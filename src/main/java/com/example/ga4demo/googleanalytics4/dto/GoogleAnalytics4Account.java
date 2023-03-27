package com.example.ga4demo.googleanalytics4.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


@Data
@AllArgsConstructor
public class GoogleAnalytics4Account {

    private String accountName;
    private List<GoogleAnalytics4Property> googleAnalytics4Properties;
}
