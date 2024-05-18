package br.demo.backend.security.service;

import br.demo.backend.model.User;
import br.demo.backend.model.dtos.user.UserGetDTO;
import br.demo.backend.model.dtos.user.UserPostDTO;
import br.demo.backend.repository.UserRepository;
import br.demo.backend.security.entity.UserDatailEntity;
import br.demo.backend.security.utils.CookieUtil;
import br.demo.backend.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthenticationExternalService {

    private final SecurityContextRepository securityContextRepository;
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final UserRepository userRepository;
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

    public String createUserGitHub(String username, String name) {
        try {
            UserDatailEntity userDatailEntity = new UserDatailEntity();
            if (userRepository.findByUserDetailsEntity_Username(username).isPresent()){
                username = genareteUsernameRandom();
            }
            userDatailEntity.setUsername(username);
            userDatailEntity.setPassword(username);
            userDatailEntity.setUsernameGitHub(username);
            userDatailEntity.setLinkedWithGitHub(true);
            UserPostDTO userPostDTO = new UserPostDTO(name, "", "",false, userDatailEntity);
            userService.save(userPostDTO);
            return username;
        } catch (Exception e) {
            throw new RuntimeException("Error creating user");
        }
    }

    public String genareteUsernameRandom(){
        String username;
        do {
            username = UUID.randomUUID().toString();
        }while (userRepository.findByUserDetailsEntity_Username(username).isPresent());
        return username;
    }
    public String createUserGoogle( String name, String email) {
        try {
            UserDatailEntity userDatailEntity = new UserDatailEntity();
            if (userRepository.findByUserDetailsEntity_Username(name).isPresent()){
              name = genareteUsernameRandom();
            }
            userDatailEntity.setUsername(name);
            userDatailEntity.setPassword(name);
            userDatailEntity.setLinkedWithGoogle(true);

            UserPostDTO userPostDTO = new UserPostDTO(name, "",email, false, userDatailEntity);
           UserGetDTO userGet =userService.save(userPostDTO);
            User user = userRepository.findById(userGet.getId()).get();
            user.setMail(email);
            userRepository.save(user);
            return userDatailEntity.getUsername();
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("Error creating user");
        }
    }

    public void externalLogin(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String name = oAuth2User.getAttribute("name");
        Cookie newCookie = null;
        String returnUsername = "";

        if (request.getRequestURI().contains("github")) {
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

                if (!user.isPresent()) {
                    returnUsername = createUserGitHub(username, name);
                    newCookie = loginWithExternalService(request, response, username);
                } else if (user.get().getUserDetailsEntity().getUsername().equals(username)) {
                    returnUsername = createUserGitHub(username, name);
                    newCookie = loginWithExternalService(request, response, username);
                }
                response.addCookie(newCookie); // Add the cookie to the response
                response.sendRedirect("http://localhost:3000/" + returnUsername);
            }
        } else if (request.getRequestURI().contains("google")) {
            String email = oAuth2User.getAttribute("email");
            Optional<User> userOptional = userRepository.findByMail(email);

            try {
                if (userOptional.isPresent() && userOptional.get().getUserDetailsEntity().isLinkedWithGoogle()) {
                    newCookie = loginWithGoogle(request, response, email);
                }else {
                    throw new UsernameNotFoundException("User not found");
                }
                response.addCookie(newCookie);
                response.sendRedirect("http://localhost:3000/" + userOptional.get().getUserDetailsEntity().getUsername() );
            } catch (UsernameNotFoundException e) {

                if (!userOptional.isPresent()) {
                    returnUsername = createUserGoogle( name,email);
                    newCookie = loginWithGoogle(request, response, email);
                } else if (userOptional.get().getUserDetailsEntity().getUsername().equals(name)) {
                    returnUsername = createUserGoogle(name, email);
                    newCookie = loginWithGoogle(request, response, email);
                }
                response.addCookie(newCookie); // Add the cookie to the response
                response.sendRedirect("http://localhost:3000/" + returnUsername);
            }
        }
    }
}
