package com.savoirstrategie.app.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.savoirstrategie.app.helpers.CODE;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CustomAuthenticationFailureHandler  implements AuthenticationFailureHandler {
    private ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        Map<String, Object> data = new HashMap<>();
        data.put(
                "code",
                CODE.UNAUTHORIZED.getId());
        data.put(
                "message",
                exception.getMessage());

        data.put(
                "success",
                false);

        response.setContentType("application/json");

        // Write the JSON string to the response output stream
        response.getOutputStream().write(objectMapper.writeValueAsBytes(data));

    }
}
