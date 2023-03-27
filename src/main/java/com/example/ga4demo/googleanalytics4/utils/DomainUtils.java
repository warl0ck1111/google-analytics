package com.example.ga4demo.googleanalytics4.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;


@UtilityClass
@Slf4j
public class DomainUtils {
    public String getUrlPath(String url, String domainName) {
        if (StringUtils.isBlank(url)) {
            return url;
        } else {
            int indexOfDomain = url.indexOf(domainName);
            if (indexOfDomain > 0) {
                String result = url.substring(indexOfDomain + domainName.length());
                if (StringUtils.isBlank(result)) {
                    return "/";
                } else {
                    return result;
                }
            } else {
                return url;
            }
        }
    }}
