package com.example.ga4demo.googleanalytics4.customlogic;

import feign.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomLogicClientConfig {
    @Bean
    public Request.Options requestOptions() {
        return new Request.Options(3000, 3600000);
    }
}
