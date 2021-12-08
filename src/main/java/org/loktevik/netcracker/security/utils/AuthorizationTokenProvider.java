package org.loktevik.netcracker.security.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class AuthorizationTokenProvider {

    public static UsernamePasswordAuthenticationToken getToken(String token){
        String secret = SecretProvider.getSecret();
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        String username = decodedJWT.getSubject();
        String [] roles = decodedJWT.getClaim("roles").asArray(String.class);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        Arrays.stream(roles)
                .forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }
}
