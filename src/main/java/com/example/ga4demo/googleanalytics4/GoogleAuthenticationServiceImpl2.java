package com.example.ga4demo.googleanalytics4;

import com.google.auth.oauth2.GoogleCredentials;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
@Slf4j
public class GoogleAuthenticationServiceImpl2 implements GoogleAuthenticationService2 {


    @Override
    public GoogleCredentials getGACredentials() {
        return getCredentials("experts@incremys.com");
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
        InputStream inputStream = null;

        try {
            inputStream = GoogleAuthenticationServiceImpl2.class.getResourceAsStream("credentials.json");
            if (inputStream !=null) {
                return GoogleCredentials.fromStream(inputStream);
            }
            return null;
        } catch (IOException e) {

            throw new RuntimeException(e);
        }

    }

}
