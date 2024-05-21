package br.demo.backend.google.config;

import br.demo.backend.model.User;
import br.demo.backend.repository.UserRepository;
import br.demo.backend.security.entity.UserDatailEntity;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Configuration
@AllArgsConstructor
public class GoogleCalendarConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleCalendarConfig.class);
    private static final String APPLICATION_NAME = "TaskFlow";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
    private static final String CREDENTIALS_FILE_PATH = "src/main/resources/credentials.json";
    private static final String REDIRECT_URI = "http://localhost:9999/callback/google"; // Ensure this matches your registered URI

    private UserRepository userRepository;

    private static GoogleAuthorizationCodeFlow buildFlow(String userId) throws IOException, GeneralSecurityException {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        File credentialsFile = new File(CREDENTIALS_FILE_PATH);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(new FileInputStream(credentialsFile)));

        return new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH + "/" + userId)))
                .setAccessType("offline")
                .build();
    }

    protected static Credential getCredentials(String userId) throws IOException {
        try {
            GoogleAuthorizationCodeFlow flow = buildFlow(userId);
            LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(9999).build(); // Match the port with your redirect URI
            return new AuthorizationCodeInstalledApp(flow, receiver).authorize(userId);
        } catch (Exception e) {
            LOGGER.error("Failed to get credentials for user: {}", userId, e);
            throw new RuntimeException("Failed to get credentials for user: " + userId, e);
        }
    }

    public static Calendar createCalendar() throws GeneralSecurityException, IOException {
        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(userDetails instanceof UserDatailEntity)) {
            throw new IllegalStateException("Authentication principal is not of type UserDatailEntity");
        }
        UserDatailEntity user = (UserDatailEntity) userDetails;
        return new Calendar.Builder(httpTransport, JSON_FACTORY, getCredentials(user.getUsername()))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public String generateUrl() {
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (!(userDetails instanceof UserDatailEntity)) {
                throw new IllegalStateException("Authentication principal is not of type UserDatailEntity");
            }
            UserDatailEntity user = (UserDatailEntity) userDetails;
            GoogleAuthorizationCodeFlow flow = buildFlow(user.getUsername());

            GoogleAuthorizationCodeRequestUrl authorizationUrl = flow.newAuthorizationUrl()
                    .setRedirectUri(REDIRECT_URI)
                    .setAccessType("offline");

            return authorizationUrl.build();
        } catch (Exception e) {
            LOGGER.error("Failed to generate authorization URL", e);
            throw new RuntimeException("Failed to generate authorization URL", e);
        }
    }

    public Credential exchangeCodeForToken(String code, UserDatailEntity userId) throws IOException, GeneralSecurityException {
        try {
            GoogleAuthorizationCodeFlow flow = buildFlow(userId.getUsername());

            GoogleAuthorizationCodeTokenRequest tokenRequest = flow.newTokenRequest(code)
                    .setRedirectUri(REDIRECT_URI);

            GoogleTokenResponse tokenResponse = tokenRequest.execute();

            return flow.createAndStoreCredential(tokenResponse, userId.getUsername());
        } catch (Exception e) {
            LOGGER.error("Failed to exchange code for token", e);
            throw new RuntimeException("Failed to exchange code for token", e);
        }
    }
}
