package com.example.ga4demo.googleanalytics4.core.filters;

import com.example.ga4demo.googleanalytics4.core.types.GoogleAnalytics4PatternMatchingType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Okala Bashir .O.
 */
public class DimensionFilters {

//    public DimensionFilterClause buildFilterClauseByUrlOrLandingUrl(
//            String urlPattern, GoogleAnalytics4PatternMatchingType patternMatchingType, boolean isLandingUrlMetric) {
//        List<DimensionFilter> filters = new ArrayList<>();
//        DimensionFilter filter;
//        if (isLandingUrlMetric) {
//            filter = buildFilter(
//                    GoogleAnalyticsDimensionType.DIMENSION_LANDING_PAGE_PATH.getValue(), patternMatchingType.toString(), urlPattern);
//        } else {
//            filter = buildFilter(
//                    GoogleAnalyticsDimensionType.DIMENSION_PAGE_PATH.getValue(), patternMatchingType.toString(), urlPattern);
//        }
//        filters.add(filter);
//        return new DimensionFilterClause().setOperator(CLAUSE_OPERATOR_OR).setFilters(filters);
//    }

}
