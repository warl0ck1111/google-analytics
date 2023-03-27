package com.example.ga4demo.googleanalytics4.dataflows;

import com.google.analytics.data.v1beta.BatchRunReportsRequest;
import com.google.analytics.data.v1beta.BatchRunReportsResponse;
import com.google.analytics.data.v1beta.BetaAnalyticsDataClient;
import com.google.api.gax.rpc.ApiException;
import com.google.api.gax.rpc.StatusCode;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
//import com.incremys.saas.shared.common.service.EmailService;
import com.example.ga4demo.googleanalytics4.exceptions.GA4ExecuteRequestFailureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Okala Bashir .O.
 */

@Component
@Slf4j
public class GA4RequestExecutorServiceImpl implements GA4RequestExecutorService{
//
//
//    @Autowired
//    private EmailService emailService;
    private volatile LoadingCache<String, Integer> propertyIdToErrorsCount;
//    @Value("#{'${spring.mail.accountmanagers}'.split(',')}")
//    private List<String> accountmanagers;

    @PostConstruct
    private void initializePostConstruct() {
        propertyIdToErrorsCount = CacheBuilder.newBuilder().expireAfterWrite(24, TimeUnit.HOURS).build(new CacheLoader<>() {
            @Override
            public Integer load(final String key) {
                return 0;
            }
        });
    }

    @Retryable(value = {GA4ExecuteRequestFailureException.class}, maxAttempts = 2, backoff = @Backoff(delay = 60000))
    public BatchRunReportsResponse executeRequest(String domainName, BetaAnalyticsDataClient analytics, BatchRunReportsRequest request) {
        try {
            log.info("GoogleGA4.executeRequest/START request to Google Analytics 4 API");
            BatchRunReportsResponse response = analytics.batchRunReports(request);

            log.info("GoogleGA4.executeRequest/END request to Google Analytics 4 API");
            return response;
        } catch (ApiException ex) {
            if (ex.getStatusCode().getCode() == StatusCode.Code.PERMISSION_DENIED) {
                log.error("GoogleGA4.executeRequest/403 exception when calling GA4 for property : " + request.getProperty());
//                checkAndSendMail(domainName, request.getProperty());
                throw ex;
            } else {
                log.error("GoogleGA.executeRequest/Exception is thrown", ex);
                throw new GA4ExecuteRequestFailureException();
            }
        } catch (Exception e) {
            log.error("GoogleGA.executeRequest/Exception is thrown", e);
            throw new GA4ExecuteRequestFailureException();
        }
    }

    private synchronized void checkAndSendMail(String domainName, String propertyId) {
        try {
            Integer errorsCount = propertyIdToErrorsCount.get(propertyId);
            if (errorsCount == null || errorsCount == 0) {
                propertyIdToErrorsCount.put(propertyId, 1);
                //todo change for prod
                log.info("checkAndSendMail/checking and sending mock mail");
//                emailService.constructAndSendEmail("GA4 403 error", "GA4 403 error for domainName/propertyId : " + domainName + "/" + propertyId, new ArrayList<>(accountmanagers));
            }
        } catch (Exception e) {

        }
    }

}
