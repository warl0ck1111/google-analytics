package com.example.ga4demo.googleanalytics4.exceptions;

import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class GAgetFailureException extends RuntimeException {
    String message = "Google Analytics provider is unavailable. Unable to complete operation.";
}
