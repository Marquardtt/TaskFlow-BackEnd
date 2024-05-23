package br.demo.backend.security.utils;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.web.util.WebUtils;

import java.util.Arrays;
import java.util.Date;

public class CookieUtil {

    public Cookie gerarCookieJwt(UserDetails userDetails){
        String token = new JwtUtil().gerarToken(userDetails);
        Cookie cookie = new Cookie("JWT", token);
        cookie.setPath("/");
        cookie.setMaxAge(1800);
        return cookie;
    }

    public Cookie getCookie(HttpServletRequest request, String name){
        return WebUtils.getCookie(request,name);
    }

    public String getCookieValue(HttpServletRequest req, String cookieName) {
        return Arrays.stream(req.getCookies())
                .filter(c -> c.getName().equals(cookieName))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }

//    public Cookie createSecureCookie(UserDetails userDetails) {
//        long expirationTime = 1000 * 60 * 60 * 24 * 30; // 30 dias para expiração do cookie
//        String secretKey = "authenticate";
//        String token = Jwts.builder()
//                .setSubject(userDetails.getUsername())
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
//                .signWith(SignatureAlgorithm.HS512, secretKey)
//                .compact();
//
//        Cookie cookie = new Cookie("auth_token", token);
//        cookie.setHttpOnly(true);
//        cookie.setSecure(true);
//        cookie.setPath("/");
//        cookie.setMaxAge((int) expirationTime / 1000);
//        cookie.setSameSite("Strict");
//
//        return cookie;
//    }


}