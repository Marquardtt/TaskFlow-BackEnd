package br.demo.backend.security.service;

import br.demo.backend.model.User;
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

@Service
@AllArgsConstructor
public class AuthenticationGitHub {

    private final SecurityContextRepository securityContextRepository;
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final CookieUtil cookieUtil = new CookieUtil();

    public Cookie loginWithGitHub(HttpServletRequest request, HttpServletResponse response, String username) {

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

    public void createUserGitHub(String username, String name, String email) {
        try {
            UserDatailEntity userDatailEntity = new UserDatailEntity();
            userDatailEntity.setUsername(username+ name);
            userDatailEntity.setPassword(username);
            userDatailEntity.setUsernameGitHub(username);
            userDatailEntity.setLinkedWithGitHub(true);
            UserPostDTO userPostDTO = new UserPostDTO(name, "", email,  false, userDatailEntity);
            userService.save(userPostDTO);
        } catch (Exception e) {
            throw new RuntimeException("Error creating user");
        }
    }

    public void externalLogin(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        System.out.println(request.getUserPrincipal());

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println(oAuth2User);
        String username = oAuth2User.getAttribute("login");
        String email = oAuth2User.getAttribute("email");
        Optional<User> user = userRepository.findByUserDetailsEntity_UsernameGitHub(username);


        try {
            if (user.isPresent() && user.get().getUserDetailsEntity().isLinkedWithGitHub() && user.get().getUserDetailsEntity().getUsernameGitHub().equals(username)) {
                    Cookie newCookie = loginWithGitHub(request, response, username);
                    response.addCookie(newCookie); // Add the cookie to the response
                    response.sendRedirect("http://localhost:3000/" + username);
            } else {
                throw new UsernameNotFoundException("User not found");
            }
        } catch (UsernameNotFoundException e) {
            Cookie newCookie = null;
            String name = oAuth2User.getAttribute("name");
            if (!user.isPresent()) {
                createUserGitHub(username, name,email);
                newCookie = loginWithGitHub(request, response, username);
            } else if (user.get().getUserDetailsEntity().getUsername().equals(username)) {

                createUserGitHub(username, "TESTE", email);
                newCookie = loginWithGitHub(request, response, username);
            }

            response.addCookie(newCookie); // Add the cookie to the response
            response.sendRedirect("http://localhost:3000/" + username);
        }
    }
}
