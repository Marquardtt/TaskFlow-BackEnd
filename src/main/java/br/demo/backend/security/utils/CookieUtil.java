package br.demo.backend.security.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.util.WebUtils;

public class CookieUtil {

    public Cookie gerarCookieJwt(UserDetails userDetails){
        String token = new JwtUtils().gerarToken(userDetails);
        Cookie cookie = new Cookie("JWT", token);
        cookie.setPath("/");
        cookie.setMaxAge(300);
        return cookie;
    }

    public Cookie getCookie(HttpServletRequest request, String name){
        return WebUtils.getCookie(request,name);
    }
}