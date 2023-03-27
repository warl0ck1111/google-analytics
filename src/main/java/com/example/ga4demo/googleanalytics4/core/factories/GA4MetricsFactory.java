package com.example.ga4demo.googleanalytics4.core.factories;

import com.google.analytics.data.v1beta.Metric;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GA4MetricsFactory {
    private final List<Metric> metrics;

    public GA4MetricsFactory(List<String> metricsAlias) {
        this.metrics = new ArrayList<>();
        buildMetrics(metricsAlias);
    }

    private void buildMetrics(List<String> metricAlias) {
        metricAlias.forEach(alias -> this.metrics.add(buildMetric(alias)));
    }

    private Metric buildMetric(String metricAlias) {
        return Metric.newBuilder().setName(metricAlias).build();
    }


}
