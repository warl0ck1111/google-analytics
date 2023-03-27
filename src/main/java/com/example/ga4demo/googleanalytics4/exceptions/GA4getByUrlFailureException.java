package com.example.ga4demo.googleanalytics4.exceptions;

import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class GA4getByUrlFailureException extends RuntimeException {
    String message = "Google Analytics 4 provider is unavailable. Unable to complete operation.";
}
