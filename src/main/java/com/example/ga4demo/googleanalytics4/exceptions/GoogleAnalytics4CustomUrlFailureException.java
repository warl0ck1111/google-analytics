package com.example.ga4demo.googleanalytics4.exceptions;

import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class GoogleAnalytics4CustomUrlFailureException extends RuntimeException {
    String message = "Google Analytics 4 custom logic is unavailable. Unable to complete operation.";
}
