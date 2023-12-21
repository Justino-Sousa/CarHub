package br.com.js.carhub.exception;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import br.com.js.carhub.controller.AuthenticationController;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class UnauthorizedException implements AuthenticationEntryPoint {
  
	@Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        if (AuthenticationController.tokenExpired) {
        	response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{ \"message\": \"Unauthorized - invalid session\", \"errorCode\": 401 }");
            AuthenticationController.tokenExpired = false;
        } else {
        	response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{ \"message\": \"Unauthorized\", \"errorCode:\": 401 }");
        }
    }
}