package com.example.demo.auth.handler;


import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.support.SessionFlashMapManager;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        SessionFlashMapManager flashMapManager  = new SessionFlashMapManager();
        FlashMap flashMap = new FlashMap();
        flashMap.put("success","has iniciado sesion con exito");
        flashMapManager.saveOutputFlashMap(flashMap, request, response);
        super.onAuthenticationSuccess(request,response,authentication);
    }
}
