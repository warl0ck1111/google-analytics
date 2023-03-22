package com.example.ga4demo.googleanalytics4;

import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.stereotype.Service;


public interface GoogleAuthenticationService {

    GoogleCredentials getGACredentials();

    GoogleCredentials getAccorGACredentials();

    GoogleCredentials getGSCCredentials();

    GoogleCredentials getGDriveCredentials(String accountUser);

}
