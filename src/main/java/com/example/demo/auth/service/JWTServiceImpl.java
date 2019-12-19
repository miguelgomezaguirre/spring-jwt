package com.example.demo.auth.service;

import com.example.demo.auth.SimpleGrantedAuthorityMixin;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

@Component
public class JWTServiceImpl implements JWTService {

    public static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    public static final String AUTHORITIES = "authorities";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final Long EXPIRATION_DATE = 3600000L * 4;

    @Override
    public String create(Authentication authentication) throws JsonProcessingException {
        String username = authentication.getName();
        Collection<? extends GrantedAuthority> roles = authentication.getAuthorities();

        Claims claims = Jwts.claims();
        claims.put(AUTHORITIES, new ObjectMapper().writeValueAsString(roles));

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .signWith(SECRET_KEY)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_DATE))
                .compact();
    }

    @Override
    public boolean validate(String token) {
        try {
            getClaims(token);
           return true;
        } catch (JwtException e) {
            return false;
        }
    }

    @Override
    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(resolve(token))
                .getBody();
    }

    @Override
    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    @Override
    public Collection<? extends GrantedAuthority> getRoles(String token) throws JsonProcessingException {
        Object roles = getClaims(token).get(AUTHORITIES);

        return Arrays.asList(new ObjectMapper()
                .addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityMixin.class)
                .readValue(roles.toString(), SimpleGrantedAuthority[].class));
    }

    @Override
    public String resolve(String token) {
        if (token != null && token.startsWith(TOKEN_PREFIX)){
            return token.replace(TOKEN_PREFIX, "");
        }
        return null;
    }
}
