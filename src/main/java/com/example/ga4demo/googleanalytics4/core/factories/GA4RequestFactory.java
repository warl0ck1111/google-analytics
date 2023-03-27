package com.example.ga4demo.googleanalytics4.core.factories;

import com.example.ga4demo.googleanalytics4.CustomLogicService_GOOGLE_ANALYTICS;
import com.example.ga4demo.googleanalytics4.core.filters.DimensionFilters;
import com.example.ga4demo.googleanalytics4.core.types.*;
import com.example.ga4demo.shared.utils.SpringContext;
import com.google.analytics.data.v1beta.*;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@UtilityClass
@Slf4j
public class GA4RequestFactory {

    private final Integer SMALL_MAX_ROWS = 1000;
    private final Integer LARGE_MAX_ROWS = 100000;

    public RunReportRequest buildRequestBasic(
            String propertyId, String startDate, String endDate, String metricAlias, List<Dimension> dimensions) {
        DateRange dateRange = new GA4DateRangeFactory().get(startDate, endDate);

        return RunReportRequest.newBuilder()
                .setProperty("properties/" + propertyId)
                .addAllDimensions(dimensions)
                .addMetrics(Metric.newBuilder().setName(metricAlias))
                .addDateRanges(dateRange)
                .setKeepEmptyRows(true)
                .setLimit(SMALL_MAX_ROWS)
                .build();
    }

 public RunReportRequest buildRequestBasic(
            String propertyId, String startDate, String endDate, String metricAlias) {
        DateRange dateRange = new GA4DateRangeFactory().get(startDate, endDate);

        return RunReportRequest.newBuilder()
                .setProperty("properties/" + propertyId)
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
                    .setFieldName(GoogleAnalytics4DimensionType.DIMENSION_MEDIUM.getValue())
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

    public RunReportRequest buildRequestOverTime(
            String propertyId,
            String startDate,
            String endDate,
            String metricAlias,
            GA4TimeIncrement increment) {

        List<Dimension> dimensions = new GA4DimensionsFactory(increment.getValue()).getDimensions();
        return buildRequestBasic(propertyId, startDate, endDate, metricAlias, dimensions);
    }


    public RunReportRequest buildRequestByUrlOrLandingUrlOverTimeForMedium(
            String domainName, String propertyId, String urlPattern,
            GoogleAnalytics4PatternMatchingType patternMatchingType, GA4MediumType medium, String startDate, String endDate, String metricAlias, GA4TimeIncrement timeIncrement) {


        RunReportRequest request = buildRequestByUrlOrLandingUrlOverTime(domainName, propertyId, urlPattern, patternMatchingType, startDate, endDate, metricAlias, timeIncrement);
//        if (request.getDimensionFilterClauses() == null) { todo: revisit
//            request.setDimensionFilterClauses(new ArrayList<>());
//        }
//        if (medium != null) {
//            DimensionFilterClause filterByCanal = new DimensionFilters().buildFilterClauseByMedium(medium);
//            request.getDimensionFilterClauses().add(filterByCanal);
//        }
        return request.toBuilder().setLimit(LARGE_MAX_ROWS).build();
    }


    public RunReportRequest buildRequestByUrlOrLandingUrlOverTime(
            String domainName,
            String propertyId,
            String urlPattern,
            GoogleAnalytics4PatternMatchingType patternMatchingType,
            String startDate,
            String endDate,
            String metricAlias,
            GA4TimeIncrement increment) {
        RunReportRequest request =
                buildRequestOverTime(propertyId, startDate, endDate, metricAlias, increment);
        CustomLogicService_GOOGLE_ANALYTICS customClientLogicService = SpringContext.getBean(CustomLogicService_GOOGLE_ANALYTICS.class);
        String path = customClientLogicService.findCustomGAUrl(urlPattern, domainName);
        boolean isLandingUrlMetric = GoogleAnalytics4MetricType.LANDING_URL_METRICS_VALUES.contains(metricAlias);
//        DimensionFilterClause filterByUrl = todo: revisit
//                new DimensionFilters().buildFilterClauseByUrlOrLandingUrl(path, patternMatchingType, isLandingUrlMetric);
//        return request.setDimensionFilterClauses(new ArrayList<>(Collections.singletonList(filterByUrl)));
        return request;
    }


    public BatchRunReportsRequest buildRequestByUrlOrLandingUrl(
            String domainName,
            String propertyId,
            String urlPattern,
            GoogleAnalytics4PatternMatchingType patternMatchingType,
            String startDate,
            String endDate,
            String metricAlias
    ) {
        RunReportRequest baseRequest = buildRequestBasic(propertyId, startDate, endDate, metricAlias);
        CustomLogicService_GOOGLE_ANALYTICS customClientLogicService = SpringContext.getBean(CustomLogicService_GOOGLE_ANALYTICS.class);
        String path = customClientLogicService.findCustomGAUrl(urlPattern, domainName);
        boolean isLandingUrlMetric = GoogleAnalytics4MetricType.LANDING_URL_METRICS_VALUES.contains(metricAlias);
//        DimensionFilterClause filterByUrl = todo: revisit
//                new GA4DimensionFilters().buildFilterClauseByUrlOrLandingUrl(path, patternMatchingType, isLandingUrlMetric);
//        return baseRequest.setDimensionFilterClauses(new ArrayList<>(Collections.singletonList(filterByUrl)));

        BatchRunReportsRequest batchRunReportsRequest = BatchRunReportsRequest.newBuilder().addRequests(baseRequest).build();
        return batchRunReportsRequest;
    }

    public RunReportRequest buildRequestByUrlOrLandingUrlForMedium(String domainName, String propertyId, String urlPattern, GoogleAnalytics4PatternMatchingType patternMatchingType, GA4MediumType medium, String startDate, String endDate, String metricAlias) {
        return null;
    }

    public static RunReportRequest buildRequestByUrlOrLandingUrlForMediumOverTime(String domainName, String propertyId, String urlPattern, GoogleAnalytics4PatternMatchingType patternMatchingType, GA4MediumType type, String startDate, String endDate, String metricAlias, GA4TimeIncrement date) {
        return null;
    }

    public static RunReportRequest buildRequestByUrlsOrLandingUrls(String domainName, String viewId, List<String> urls, String startDate, String endDate, String metricAlias, boolean getNOTResult) {
        return null;
    }
}
