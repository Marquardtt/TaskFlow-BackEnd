package br.demo.backend.security.service;

import br.demo.backend.model.dtos.user.UserGetDTO;
import br.demo.backend.model.dtos.user.UserPostDTO;
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
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationGitHubService {

    private final SecurityContextRepository securityContextRepository;
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final CookieUtil cookieUtil = new CookieUtil();

    public Cookie loginWithGitHub(HttpServletRequest request, HttpServletResponse response, String username) {
        UserDetails userDetails = authenticationService.loadUserByUsername(username);
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

    public void createUserGitHub(String username, String name) {
        try {
            UserDatailEntity userDatailEntity = new UserDatailEntity();
            userDatailEntity.setUsername(username);
            userDatailEntity.setPassword(username);
            UserPostDTO userPostDTO = new UserPostDTO(name, "", userDatailEntity);
            userService.save(userPostDTO);
        } catch (Exception e) {
            throw new RuntimeException("Error creating user");
        }
    }
}
