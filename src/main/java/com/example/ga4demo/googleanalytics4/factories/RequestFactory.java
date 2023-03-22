//package com.example.ga4demo.googleanalytics4.factories;
//
//import com.google.analytics.data.v1beta.DateRange;
//import com.google.analytics.data.v1beta.Metric;
//import com.google.analytics.data.v1beta.RunReportRequest;
//import com.google.common.collect.ImmutableList;
//
//import lombok.experimental.UtilityClass;
//import lombok.extern.slf4j.Slf4j;
//
//import java.util.Collections;
//import java.util.List;
//
//@UtilityClass
//@Slf4j
//public class RequestFactory {
//
//    private final Integer SMALL_MAX_ROWS = 1000;
//    private final Integer LARGE_MAX_ROWS = 100000;
//
//    public RunReportRequest buildRequestBasic(
//            String viewId, String startDate, String endDate, String metricAlias) {
//        DateRange dateRange = new DateRangeFactory().get(startDate, endDate);
//        List<Metric> metrics = new MetricsFactory(Collections.singletonList(metricAlias)).getMetrics();
//
//        return new ReportRequest()
//                .setViewId(viewId)
//                .setDateRanges(ImmutableList.of(dateRange))
//                .setMetrics(metrics)
//                .setIncludeEmptyRows(true)
//                .setPageSize(SMALL_MAX_ROWS);
//    }
//}
