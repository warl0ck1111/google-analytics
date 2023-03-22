package com.example.ga4demo.googleanalytics4.types;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum MediumType {

    DIRECT,
    ORGANIC,
    PAID,
    EMAIL,
    REFERRAL,
    NOT_SET,
    OTHERS;

    @JsonIgnore
    public static MediumType getMediumTypeFromString(String stringValue) {
        switch (stringValue) {
            case "(none)":
                return DIRECT;
            case "organic":
                return ORGANIC;
            case "cpc":
                return PAID;
            case "email":
                return EMAIL;
            case "referral":
                return REFERRAL;
            case "(not set)":
                return NOT_SET;
            default:
                log.info("getMediumTypeFromString/exception=unknown medium type found=" + stringValue);
                log.info("getMediumTypeFromString/fallback=labeled as OTHERS=" + stringValue);
                return OTHERS;
        }
    }

    public static String getStringValueFromMediumType(MediumType mediumType) {
        switch (mediumType) {
            case DIRECT:
                return "(none)";
            case ORGANIC:
                return "organic";
            case PAID:
                return "cpc";
            case EMAIL:
                return "email";
            case REFERRAL:
                return "referral";
            case NOT_SET:
                return "(not set)";
            default:
                log.info("getStringValueFromMediumType/unknown medium type found=" + mediumType);
                log.info("getStringValueFromMediumType/returning others");
                return "others";
        }
    }

}
