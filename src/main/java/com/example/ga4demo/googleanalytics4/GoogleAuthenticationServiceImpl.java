package com.example.ga4demo.googleanalytics4;

import com.example.ga4demo.googleanalytics4.GoogleAuthenticationService;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@Slf4j
public class GoogleAuthenticationServiceImpl implements GoogleAuthenticationService {
    public static final String CREDENTIALS_JSON = "/Users/warl0ck/Downloads/service-account-file.json";
    public static final String TEST_SERVICE_ACCOUNT = "experts@incremys.com";


    @Override
    public GoogleCredentials getGACredentials() {
        return getCredentials(TEST_SERVICE_ACCOUNT);
    }

    @Override
    public GoogleCredentials getAccorGACredentials() {
        return getCredentials("martin.durand@incremys.com");
    }

    @Override
    public GoogleCredentials getGSCCredentials() {
        return getCredentials("experts@incremys.com");
    }

    @Override
    public GoogleCredentials getGDriveCredentials(String accountUser) {
        return getCredentials(accountUser);
    }

    private GoogleCredentials getCredentials(String accountUser) {

        Path path = Paths.get(CREDENTIALS_JSON);
        try {
            return GoogleCredentials.fromStream(Files.newInputStream(path));
        } catch (IOException e) {

            throw new RuntimeException(e);
        }

    }

}
