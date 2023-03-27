package com.example.ga4demo.googleanalytics4.utils;


import com.google.analytics.data.v1beta.BatchRunReportsResponse;
import com.google.analytics.data.v1beta.Row;
import com.google.analytics.data.v1beta.RunReportResponse;
import com.example.ga4demo.googleanalytics4.dto.GoogleAnalytics4MetricsDTO;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
@Slf4j
public class ReportingUtils {

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
        //TOdo: verify if logic is correct with achraf
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
        //todo check with Achraf
//        if (response == null || response.getAllFields().isEmpty()) {
//            return false;
//        }
//        List<RunReportResponse> reports = response.getReportsList();
//        if (reports == null || reports.isEmpty()) {
//            return false;
//        }
//        RunReportResponse report = reports.get(0);
//        if (report == null) {
//            return false;
//        }
//        if (report.getTotalsList() == null || report.getTotalsList().isEmpty()) {
//            return false;
//        }
//        return report.getTotalsList().get(0).getDimensionValuesList() != null;
        return true;
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



}
