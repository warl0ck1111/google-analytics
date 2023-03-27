package com.example.ga4demo.googleanalytics4.factories;

import com.example.ga4demo.googleanalytics4.types.GA4MediumType;
import com.example.ga4demo.googleanalytics4.types.GoogleAnalytics4DimensionType;
import com.example.ga4demo.googleanalytics4.types.GoogleAnalytics4MetricType;
import com.google.analytics.data.v1beta.*;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.ga4demo.googleanalytics4.types.GoogleAnalytics4DimensionType.DIMENSION_MEDIUM;

@UtilityClass
@Slf4j
public class GA4RequestFactory {

    private final Integer SMALL_MAX_ROWS = 1000;
    private final Integer LARGE_MAX_ROWS = 100000;

    public RunReportRequest buildRequestBasic(
            String propertyId, String startDate, String endDate, String metricAlias) {
        DateRange dateRange = new GA4DateRangeFactory().get(startDate, endDate);

        return RunReportRequest.newBuilder()
                .setProperty("properties/" + propertyId)
//                            .addDimensions(Dimension.newBuilder().setName(dimension))
                .addMetrics(Metric.newBuilder().setName(metricAlias))
                .addDateRanges(dateRange)
                .setKeepEmptyRows(true)
                .setLimit(SMALL_MAX_ROWS)
                .build();
    }



    public BatchRunReportsRequest buildRequestKnownUrls(String propertyId, GA4MediumType mediumType, String startDate, String endDate) {
        // build Date range
        DateRange dateRange = new GA4DateRangeFactory().get(startDate, endDate);

        // Query a random metric - BUT we are only interested in URLs
//        List<Metric> defaultMetrics =
        String defaultMetrics = GoogleAnalytics4MetricType.USERS.getValue();


        List<Dimension> dimensions = new GA4DimensionsFactory(GoogleAnalytics4DimensionType.DIMENSION_HOSTNAME.getValue(),
                GoogleAnalytics4DimensionType.DIMENSION_PAGE_PATH.getValue()).getDimensions();

        // Add URL filtering
        // Remove filters with '?' in URL
        /*
        DimensionFilterClause filterUrlWithSearchParams = new DimensionFilters().buildFilterClauseValidUrl();
        filterClauses.add(filterUrlWithSearchParams);
        */

        // Add channel grouping filter
        Filter.Builder filterBycarnal = null;
        if (mediumType != null) {
            filterBycarnal = Filter.newBuilder()
                    .setFieldName(DIMENSION_MEDIUM.getValue())
                    .setInListFilter(
                            Filter.InListFilter.newBuilder()
                                    .addAllValues(List.of(mediumType.name()))
                                    .build());
        }

        if (filterBycarnal != null) {
            RunReportRequest reportRequest = RunReportRequest.newBuilder()
                    .setProperty("properties/" + propertyId)
                    .addDateRanges(dateRange)
                    .addMetrics(Metric.newBuilder().setName(defaultMetrics))
                    .addAllDimensions(dimensions)
                    .setDimensionFilter(FilterExpression.newBuilder().setFilter(filterBycarnal).build())
                    .setKeepEmptyRows(true)
                    .setLimit(LARGE_MAX_ROWS).build();
            return BatchRunReportsRequest.newBuilder().addRequests(reportRequest).build();
        }
        return null;
    }



}
