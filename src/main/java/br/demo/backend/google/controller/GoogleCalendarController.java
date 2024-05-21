package br.demo.backend.google.controller;

import br.demo.backend.google.config.GoogleCalendarConfig;
import br.demo.backend.google.service.GoogleCalendarService;
import br.demo.backend.security.entity.UserDatailEntity;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.util.store.FileDataStoreFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.http.HttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;

@RestController()
@AllArgsConstructor

public class GoogleCalendarController {

    private final GoogleCalendarConfig service;

    @GetMapping("/calendar/google/auth")
    public ResponseEntity<?> redirectGoogleCalendar(HttpServletResponse response ) throws IOException {
        response.sendRedirect(service.generateUrl());
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/callback/google")
    public ResponseEntity<?> callback (HttpServletResponse response, HttpServletRequest request){
        String code = request.getParameter("code");
        System.out.println(code);
        if (code != null) {
            try {
                UserDatailEntity userId = ((UserDatailEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
                Credential credential = service.exchangeCodeForToken(code, userId);

                response.sendRedirect("http://localhost:3000");

                return ResponseEntity.ok("Bem-sucedido");
            } catch (IOException | GeneralSecurityException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao autorizar: " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Código de autorização ausente.");
        }
    }



}
