package br.demo.backend.security.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

public class JwtUtil {

    public String gerarToken(UserDetails userDetails){
        return JWT.create().withIssuer("WEG")
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(new Date().getTime() + 30*60*1000))
                .withSubject(userDetails.getUsername())
                .sign(Algorithm.HMAC256(userDetails.getPassword()));// Necessita ser a senha do usuário, pois o decode será a partir da mesma
    }

    public String getUsername (String token){
        return JWT.decode(token).getSubject();
    }
}
