package com.example.ga4demo.googleanalytics4;

import com.google.auth.oauth2.GoogleCredentials;


public interface GoogleAuthenticationService2 {

    GoogleCredentials getGACredentials();

    GoogleCredentials getAccorGACredentials();

    GoogleCredentials getGSCCredentials();

    GoogleCredentials getGDriveCredentials(String accountUser);

}
