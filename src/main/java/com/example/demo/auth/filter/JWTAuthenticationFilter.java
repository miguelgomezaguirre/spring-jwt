package com.example.demo.auth.filter;

import com.example.demo.auth.service.JWTService;
import com.example.demo.auth.service.JWTServiceImpl;
import com.example.demo.modelos.entidad.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private JWTService jwtService;
    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/login", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String username = obtainUsername(request);
        String password = obtainPassword(request);

        if (username != null && password != null) {
            logger.info("form-data");
            logger.info("username: " + username);
            logger.info("password: " + password);
        } else {
            Usuario usuario = null;
            try {
                usuario = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);
                username = usuario.getUsername();
                password = usuario.getPassword();
                logger.info("raw");
                logger.info("username: " + username);
                logger.info("password: " + password);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        username = username.trim();

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);

        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        String token = jwtService.create(authResult);

        response.addHeader("Authorization", JWTServiceImpl.TOKEN_PREFIX + token);

        Map<String, Object> body = new HashMap<>();
        body.put("token", token);
        body.put("user", (User) authResult.getPrincipal());
        body.put("mensaje", String.format("%s, has iniciado sesion", authResult.getName()));

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(200);
        response.setContentType("application/json");

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        Map<String, Object> body = new HashMap<>();
        body.put("mensaje", "Error al autenticar, usuario o contraseña incorrecta");
        body.put("error", failed.getMessage());

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(401);
        response.setContentType("application/json");
    }
}
