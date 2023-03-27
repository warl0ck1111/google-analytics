package com.example.ga4demo.googleanalytics4.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoogleAnalytics4MetricsDTO {

    private Long sessions;
    private Double userEngagementDuration;//avgTimeOnPage;
    private Double avgBounceRate;
//    private Double avgExitRate;

    public GoogleAnalytics4MetricsDTO(Long sessions) {
        this.sessions = sessions;
    }
}