package com.example.demo.auth.filter;

import com.example.demo.auth.service.JWTService;
import com.example.demo.auth.service.JWTServiceImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private JWTService jwtService;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTService jwtService) {
        super(authenticationManager);
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader("Authorization");

        if (!requiresAuthentication(header)) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authenticatiom = null;
        if (jwtService.validate(header)) {
            authenticatiom = new UsernamePasswordAuthenticationToken(jwtService.getUsername(header),null, jwtService.getRoles(header));
        }

        SecurityContextHolder.getContext().setAuthentication(authenticatiom);
        chain.doFilter(request,response);
    }

    protected boolean requiresAuthentication(String header) {
        return header != null || header.startsWith(JWTServiceImpl.TOKEN_PREFIX);
    }
}
