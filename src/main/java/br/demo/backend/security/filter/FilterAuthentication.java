package br.demo.backend.security.filter;

import br.demo.backend.security.service.AuthenticationService;
import br.demo.backend.security.utils.CookieUtil;
import br.demo.backend.security.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class  FilterAuthentication extends OncePerRequestFilter {
    private SecurityContextRepository securityContextRepository;
    private final CookieUtil cookieUtil = new CookieUtil();
    private final JwtUtil jwtUtil = new JwtUtil();
    private AuthenticationService userDatailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!publicRoute(request)) {
            Cookie cookie;
            try {
                cookie = cookieUtil.getCookie(request, "JWT");// Get the cookie from the request
            } catch (Exception e) {
                response.sendError(401);
                return;
            }
            String token = cookie.getValue();// Get the token from the cookie
            String username = jwtUtil.getUsername(token);// Validate the token

            // Create the authentication object
            UserDetails user = userDatailsService.loadUserByUsername(username);// Load the user from the token
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(
                            user,
                            user.getPassword(),
                            user.getAuthorities());// Create the authentication object

            // Create a new context and set the authentication in it
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext(); // Create a new context
            securityContext.setAuthentication(authentication); // Set the authentication in the context because the session strategy will use it
            securityContextRepository.saveContext(securityContext, request, response); // Save the context in the session

            Cookie newCookie = cookieUtil.gerarCookieJwt(user); // Generate a new cookie
            response.addCookie(newCookie); // Add the cookie to the response
        }

            filterChain.doFilter(request, response);    // Call the next filter

    }

    private boolean publicRoute(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String method = request.getMethod();

        if (method.equals("GET")) {
            return
                    uri.equals("/login/oauth2/github") || uri.equals("/favicon.ico") || uri.equals("/sendEmail") ||
                    uri.equals("/sendEmail/code") || uri.equals("/auth/login/code/github") || uri.equals("/login");
        }

        if (method.equals("POST")) {
            return uri.equals("/verify-otp") || uri.equals("/sendEmail/auth") || uri.equals("/sendEmail/forgotPassword") || uri.equals("/user")||  uri.equals("/login");
        }

        if (method.equals("PATCH")) {
            return uri.startsWith("/user/password/");
        }

        return false;
    }
}
