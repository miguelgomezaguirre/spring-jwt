package com.example.demo.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface JWTService {

    String create(Authentication authentication) throws JsonProcessingException;
    boolean validate(String token);
    Claims getClaims(String token);
    String getUsername(String token);
    Collection<? extends GrantedAuthority> getRoles(String token) throws JsonProcessingException;
    String resolve(String token);

}
