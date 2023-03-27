package com.example.ga4demo.googleanalytics4.utils;


import com.example.ga4demo.shared.utils.DataUtils;
import com.example.ga4demo.shared.utils.DateUtils;
import com.example.ga4demo.shared.utils.SpringContext;
import com.google.analytics.data.v1beta.BatchRunReportsResponse;
import com.google.analytics.data.v1beta.MetricValue;
import com.google.analytics.data.v1beta.Row;
import com.google.analytics.data.v1beta.RunReportResponse;
import com.example.ga4demo.googleanalytics4.dto.GoogleAnalytics4MetricsDTO;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@UtilityClass
@Slf4j
public class GA4ReportingUtils {

    public <T> T parseMetricFromResponse(BatchRunReportsResponse response) throws Exception {
        List<Row> rows = getRowsFromBatchReport(response);
        if (rows.isEmpty()) {
            if (!hasTotalData(response)) {
                return null;
            }
            return getObjectFromTotalZero(extractTotalsObj(response));
        }
        List<String> metricValues = new ArrayList<>();
        rows.get(0).getMetricValuesList().forEach(metricValue->metricValues.add(metricValue.getValue()) );
        return getObjectFromMetricValues(metricValues);
    }

    public List<String> buildListDatesFromDateRange(String startDateStr, String endDateStr) {
        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);

        List<String> dates = new ArrayList<>();

        while (startDate.isBefore(endDate)) {
            dates.add(DateUtils.getSimpleDateFromLocalDate(startDate));
            startDate = startDate.plusDays(1);
        }
        dates.add(DateUtils.getSimpleDateFromLocalDate(startDate));
        return dates;
    }


    // Dimensions is what split the data (ex: overTime > date, or: byUrls > url)
    public <T> Map<String, T> parseMetricMapFromResponse(BatchRunReportsResponse response, List<String> dimensions) throws Exception {
        Map<String, T> valuesMap = new HashMap<>();

        // Check if has no data
        List<Row> rows = getRowsFromBatchReport(response);
        if (rows.isEmpty() && hasTotalData(response) && isTotalZero(response)) {
            dimensions.forEach(dimension ->
                    valuesMap.put(dimension, getObjectFromTotalZero(extractTotalsObj(response))));
            return valuesMap;
        }

        rows.forEach(
                row -> {
                    List<String> metricsValues = getMetricsValueFromMetricList(row.getMetricValuesList());
                    String dimension = row.getDimensionValues(0).getValue();
                    valuesMap.put(dimension, getObjectFromMetricValues(metricsValues));
                });
        return valuesMap;
    }

    private List<String> getMetricsValueFromMetricList(List<MetricValue> metricsValueList){
        List<String> metricValues = new ArrayList<>();
        metricsValueList.forEach(metricValue -> metricValues.add(metricValue.getValue()));
        return metricValues;
    }


    private boolean isTotalZero(BatchRunReportsResponse response) {
        RunReportResponse report = response.getReports(0);
        List<String> totals = getMetricsValueFromMetricList(report.getTotals(0).
                getMetricValuesList());

        for (String totalValue : totals) {
            if (!totalValue.equals("0") && !totalValue.equals("0.0")) {
                return false;
            }
        }
        return true;
    }
    private <T> T getObjectFromMetricValues(List<String> values) {
        if (values == null || values.isEmpty()) {
            return null;
        }
        if (values.size() == 1) {
            return convertToLongOrDouble(values.get(0));
        }
        return (T) GoogleAnalytics4MetricsDTO.builder()
                .avgBounceRate(convertToLongOrDouble(values.get(0)))
                .userEngagementDuration(convertToLongOrDouble(values.get(1)))
                .sessions(convertToLongOrDouble(values.get(3)))
                .build();
    }

    private List<String> extractTotalsObj(BatchRunReportsResponse response) {
        List<String> metricValues = new ArrayList<>();
        if (response != null)
            response.getReportsList().get(0).getTotalsList().get(0).getMetricValuesList().forEach(metricValue->metricValues.add(metricValue.getValue()) );
        return metricValues;
    }

    private List<Row> getRowsFromBatchReport(BatchRunReportsResponse response){

        List<Row> rowList = new ArrayList<>();
        if (response !=null && !response.getReportsList().isEmpty()) {
            response.getReportsList().forEach(report -> {
                List<Row> rowsFromReport = getRowsFromReport(report);
                rowList.addAll(rowsFromReport);
            });
            return rowList;
        }
        return new ArrayList<>();
    }
    private List<Row> getRowsFromReport(RunReportResponse reportResponse) {
        if (reportResponse == null || reportResponse.getRowsList().isEmpty()) {
            log.info("GoogleGA4.getRowsFromReport/No data found in the report response");
            return new ArrayList<>();
        }
        return reportResponse.getRowsList();
    }

    private boolean hasTotalData(BatchRunReportsResponse response) {
        if (response == null || response.getReportsList().isEmpty()) {
            return false;
        }
        List<RunReportResponse> reports = response.getReportsList();
        RunReportResponse report = reports.get(0);
        if (report == null) {
            return false;
        }
        if (report.getTotalsList().isEmpty()) {
            return false;
        }
        return report.getTotalsList().get(0) != null;
    }



    private <T> T getObjectFromTotalZero(List<String> values) {
        if (values == null || values.isEmpty()) {
            return null;
        }
        if (values.size() == 1) {
            return (T) convertToLongOrDouble(values.get(0));
        }
        return (T) GoogleAnalytics4MetricsDTO.builder()
                .avgBounceRate(null)
                .userEngagementDuration(null)
                .sessions(0L)
                .build();
    }

    private <T> T convertToLongOrDouble(String valueStr) {
        if (valueStr.contains(".")) {
            return (T) DataUtils.getSimpleDoubleValue(Double.parseDouble(valueStr));
        }
        return (T) Long.valueOf(valueStr);
    }





    public void combineResponses(
            BatchRunReportsResponse currentResponse, BatchRunReportsResponse newResponse) {
        List<Row> currentRows = getRowsFromReport(currentResponse.getReports(0));
        List<Row> newRows = getRowsFromReport(newResponse.getReports(0));
        currentRows.addAll(newRows);
        currentResponse.getReportsList().get(0).toBuilder().getRowsList().addAll(currentRows);
    }

    public static Set<String> parseSetDimensionsFromResponse(BatchRunReportsResponse response) {

        List<Row> currentRows = getRowsFromBatchReport(response);
        return currentRows
                .stream()
                .map(
                        row -> {
                            String host = row.getDimensionValues(0).getValue();//todo verify
                            String path = row.getDimensionValues(1).getValue();//todo verify
                            return buildFullUrlName(host, path);
                        })
                .collect(Collectors.toSet());
    }

    private String buildFullUrlName(String hostname, String path) {
        return Constants.URL_PREFIX_HTTPS + hostname + path;
    }


    public <T> Map<String, T> parseMetricMapByUrlsFromResponse(
            BatchRunReportsResponse response, String domainName, List<String> urls, boolean getNOTResult) throws Exception {
        Map<String, T> valuesMap = new HashMap<>();
        // Check if has no data
        List<Row> rows = getRowsFromBatchReport(response);
        if (rows.isEmpty() && hasTotalData(response) && isTotalZero(response)) {
            urls.forEach(url -> valuesMap.put(url, getObjectFromTotalZero(extractTotalsObj(response))));
            return valuesMap;
        }
        Map<String, String> gaPathsToReal = prepareGAPathsToReal(domainName, urls);

        log.info("GoogleGA4.parseMetricMapByUrlsFromResponse/rows.size=" + rows.size());
        rows.forEach(
                row -> {
                    List<String> metricsValues = row.getMetricValues(0).getValues();
                    String path = row.getDimensions().get(0);
                    String realUrl = "";
                    if (getNOTResult) {
                        realUrl = path;
                    } else {
                        realUrl = gaPathsToReal.get(path);
                    }
                    if (!StringUtils.isBlank(realUrl) && shouldIncludeUrl(gaPathsToReal.keySet(), path, getNOTResult)) {
                        valuesMap.put(realUrl, getObjectFromMetricValues(metricsValues));
                    }
                });
        log.info("GoogleGA.parseMetricMapByUrlsFromResponse/valuesMap.size=" + valuesMap.size());
        return valuesMap;
    }

    private Map<String, String> prepareGAPathsToReal(String domainName, List<String> urls) {
        CustomLogicService_GOOGLE_ANALYTICS customClientLogicService = SpringContext.getBean(CustomLogicService_GOOGLE_ANALYTICS.class);
        Map<String, String> realPathToGAPath = customClientLogicService.findCustomGAUrls(urls, domainName);
        Map<String, String> gaPathToReal = new HashMap<>();
        for(Map.Entry<String, String> entry : realPathToGAPath.entrySet()){
            gaPathToReal.put(entry.getValue(), entry.getKey());
        }
        return gaPathToReal;
    }



}
