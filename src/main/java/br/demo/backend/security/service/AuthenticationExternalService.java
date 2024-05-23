package br.demo.backend.security.service;

import br.demo.backend.model.Archive;
import br.demo.backend.model.User;
import br.demo.backend.model.dtos.user.UserGetDTO;
import br.demo.backend.model.dtos.user.UserPostDTO;
import br.demo.backend.repository.UserRepository;
import br.demo.backend.security.entity.UserDatailEntity;
import br.demo.backend.security.utils.CookieUtil;
import br.demo.backend.service.EmailService;
import br.demo.backend.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.http.client.methods.HttpGet;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthenticationExternalService {

    private final SecurityContextRepository securityContextRepository;
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final CookieUtil cookieUtil = new CookieUtil();

    public Cookie loginWithExternalService(HttpServletRequest request, HttpServletResponse response, String username) {

        UserDetails userDetails = authenticationService.loadUserByUsernameGitHub(username);
        Authentication authentication1 =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        userDetails.getPassword(),
                        userDetails.getAuthorities());// Create the authentication object

        // Create a new context and set the authentication in it
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext(); // Create a new context
        securityContext.setAuthentication(authentication1); // Set the authentication in the context because the session strategy will use it
        securityContextRepository.saveContext(securityContext, request, response); // Save the context in the session
        Cookie newCookie = cookieUtil.gerarCookieJwt(userDetails); // Generate a new cookie
        return newCookie;
    }

    public Cookie loginWithGoogle(HttpServletRequest request, HttpServletResponse response, String email) {

        UserDetails userDetails = authenticationService.loadByEmail(email);
        Authentication authentication1 =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        userDetails.getPassword(),
                        userDetails.getAuthorities());// Create the authentication object

        // Create a new context and set the authentication in it
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext(); // Create a new context
        securityContext.setAuthentication(authentication1); // Set the authentication in the context because the session strategy will use it
        securityContextRepository.saveContext(securityContext, request, response); // Save the context in the session
        Cookie newCookie = cookieUtil.gerarCookieJwt(userDetails); // Generate a new cookie
        return newCookie;
    }

    public String createUserGitHub(String username, String name, String picture) {
        try {
            UserDatailEntity userDatailEntity = new UserDatailEntity();
            if (userRepository.findByUserDetailsEntity_Username(username).isPresent()) {
                username = genareteUsernameRandom();
            }
            userDatailEntity.setUsername(username);
            userDatailEntity.setPassword(username);
            userDatailEntity.setUsernameGitHub(username);
            userDatailEntity.setLinkedWithGitHub(true);
            UserPostDTO userPostDTO = new UserPostDTO(name, "", "", false, userDatailEntity);
            UserGetDTO save = userService.save(userPostDTO);
            if (!picture.isEmpty()) {
                User user = userRepository.findByUserDetailsEntity_Username(save.getUsername()).get();
                user.setPicture(getPicture(picture));
                userRepository.save(user);

            }
            return username;
        } catch (Exception e) {
            throw new RuntimeException("Error creating user");
        }
    }

    public String genareteUsernameRandom() {
        String username;
        do {
            username = UUID.randomUUID().toString();
        } while (userRepository.findByUserDetailsEntity_Username(username).isPresent());
        return username;
    }

    public String createUserGoogle(String name, String email, String picture) {
        try {
            Archive pictureProfile;
            UserDatailEntity userDatailEntity = new UserDatailEntity();
            if (userRepository.findByUserDetailsEntity_Username(name).isPresent()) {
                name = genareteUsernameRandom();
            }
            userDatailEntity.setUsername(name);

            userDatailEntity.setPassword(name);
            userDatailEntity.setLinkedWithGoogle(true);

            UserPostDTO userPostDTO = new UserPostDTO(name, "", email, false, userDatailEntity);
            UserGetDTO save = userService.save(userPostDTO);
            if (!picture.isEmpty()) {
                User user = userRepository.findByUserDetailsEntity_Username(save.getUsername()).get();
                user.setPicture(getPicture(picture));
                userRepository.save(user);

            }
//            emailService.sendUsernameAndPasswordGoogle(name, email, name);
            return userDatailEntity.getUsername();
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("Error creating user");
        }
    }

    private static Archive getPicture(String picture) throws IOException {
        RestTemplate restTemplate = getRestTemplate();
        ResponseEntity<byte[]> response = restTemplate.getForEntity(picture, byte[].class);
        Archive archive = new Archive();
        archive.setName("ProfilePicture");
        archive.setData(response.getBody());
        archive.setType("image/png");
        return archive;
    }

    private static RestTemplate getRestTemplate() {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
        };

        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

            ClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory() {
                protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
                    if (connection instanceof HttpsURLConnection) {
                        ((HttpsURLConnection) connection).setSSLSocketFactory(sslContext.getSocketFactory());
                    }
                    super.prepareConnection(connection, httpMethod);
                }
            };

            return new RestTemplate(requestFactory);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void externalLogin(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String name = oAuth2User.getAttribute("name");
        Cookie newCookie = null;
        String returnUsername = "";

        if (request.getRequestURI().contains("github")) {
            verifyGithub(request, response, oAuth2User, newCookie, returnUsername, name);
        } else if (request.getRequestURI().contains("google")) {
            verifyGoogle(request, response, oAuth2User, newCookie, returnUsername, name);
        }
    }

    private void verifyGithub(HttpServletRequest request, HttpServletResponse response, OAuth2User oAuth2User, Cookie newCookie, String returnUsername, String name) throws IOException {
        String username = oAuth2User.getAttribute("login");
        Optional<User> user = userRepository.findByUserDetailsEntity_UsernameGitHub(username);
        try {
            if (user.isPresent() && user.get().getUserDetailsEntity().isLinkedWithGitHub() && user.get().getUserDetailsEntity().getUsernameGitHub().equals(username)) {
                newCookie = loginWithExternalService(request, response, username);
                response.addCookie(newCookie); // Add the cookie to the response
                response.sendRedirect("http://localhost:3000/" + user.get().getUserDetailsEntity().getUsername());
            } else {
                throw new UsernameNotFoundException("User not found");
            }
        } catch (UsernameNotFoundException e) {

            String picture = oAuth2User.getAttribute("avatar_url");
            if (!user.isPresent()) {
                returnUsername = createUserGitHub(username, name,picture);
                newCookie = loginWithExternalService(request, response, username);
            } else if (user.get().getUserDetailsEntity().getUsername().equals(username)) {
                returnUsername = createUserGitHub(username, name,picture);
                newCookie = loginWithExternalService(request, response, username);
            }
            response.addCookie(newCookie); // Add the cookie to the response
            response.sendRedirect("http://localhost:3000/" + returnUsername);
        }
    }

    private void verifyGoogle(HttpServletRequest request, HttpServletResponse response, OAuth2User oAuth2User, Cookie newCookie, String returnUsername, String name) throws IOException {
        String email = oAuth2User.getAttribute("email");
        Optional<User> userOptional = userRepository.findByMail(email);

        try {
            if (userOptional.isPresent() && userOptional.get().getUserDetailsEntity().isLinkedWithGoogle()) {
                newCookie = loginWithGoogle(request, response, email);
            } else {
                throw new UsernameNotFoundException("User not found");
            }
            response.addCookie(newCookie);
            response.sendRedirect("http://localhost:3000/" + userOptional.get().getUserDetailsEntity().getUsername());
        } catch (UsernameNotFoundException e) {
            String picture = oAuth2User.getAttribute("picture");
            if (!userOptional.isPresent()) {
                returnUsername = createUserGoogle(name, email, picture);
                newCookie = loginWithGoogle(request, response, email);
            } else if (userOptional.get().getUserDetailsEntity().getUsername().equals(name)) {
                returnUsername = createUserGoogle(name, email, picture);
                newCookie = loginWithGoogle(request, response, email);
            }
            response.addCookie(newCookie); // Add the cookie to the response
            System.out.println("RESPONSE");
            response.sendRedirect("http://localhost:3000/" + returnUsername);
        }
    }
}
