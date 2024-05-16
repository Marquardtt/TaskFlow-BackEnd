package br.demo.backend.security.controller;


import br.demo.backend.model.Code;
import br.demo.backend.model.OtpVerificationRequest;
import br.demo.backend.model.User;
import br.demo.backend.repository.UserRepository;
import br.demo.backend.security.entity.UserDatailEntity;
import br.demo.backend.security.model.UserLogin;
import br.demo.backend.security.service.AuthenticationService;
import br.demo.backend.security.utils.CookieUtil;
import br.demo.backend.service.EmailService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@RestController
@AllArgsConstructor
public class AuthenticateController {

    private AuthenticationManager authenticationManager;
    private final CookieUtil cookieUtil = new CookieUtil();
    private final UserRepository repository;
    private final AuthenticationService authenticationService;
    private final EmailService emailService;

    @PostMapping("/login")
    public ResponseEntity<String> authenticate(@RequestBody UserLogin userLogin, HttpServletResponse response) {
        try {
            User user = repository.findByUserDetailsEntity_Username(userLogin.getUsername()).get();

            if (!user.isAuthenticate()) {
                UsernamePasswordAuthenticationToken token =
                        new UsernamePasswordAuthenticationToken(userLogin.getUsername(), userLogin.getPassword());
                Authentication authentication = authenticationManager.authenticate(token);

                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                Cookie cookie = cookieUtil.gerarCookieJwt(userDetails);
                response.addCookie(cookie);
                return ResponseEntity.ok("User authenticated");
            } else {
                emailService.sendEmailAuth(user.getUserDetailsEntity().getUsername(), user.getMail());
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        } catch (CredentialsExpiredException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Credentials Expired - 403");
        } catch (AuthenticationException e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials - 401");
        }
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            for (Cookie cookie : authenticationService.removeCookies(request)) {
                response.addCookie(cookie);
            }
        } catch (Exception e) {
            response.setStatus(401);
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpVerificationRequest otpRequest, HttpServletResponse response) {

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(otpRequest.getUsername(), otpRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(token);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Cookie cookie = cookieUtil.gerarCookieJwt(userDetails);
        cookie.setMaxAge(3 * 30 * 24 * 3600);
        response.addCookie(cookie);
        System.out.println(cookie.getValue());

        return ResponseEntity.ok("OTP verified, user authenticated successfully");
    }

}