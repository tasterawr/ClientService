package org.loktevik.netcracker.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.loktevik.netcracker.security.utils.AccessTokenHandler;
import org.loktevik.netcracker.security.utils.AuthorizationTokenProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class AuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals("/login") || request.getServletPath().equals("/sign-in") ||
                request.getServletPath().equals("/sign-in/success")){
            Arrays.stream(request.getCookies()).forEach(cookie -> {
                if (cookie.getName().equals("access_token") || cookie.getName().equals("refresh_token")){
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            });
            filterChain.doFilter(request, response);
        } else {
            String authToken = AccessTokenHandler.getAccessToken(request.getCookies());
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ") || (authToken != null)){
                try{
                    String token;
                    if (authorizationHeader != null){
                        token = authorizationHeader.substring("Bearer ".length());
                    } else {
                        token = authToken;
                    }
                    UsernamePasswordAuthenticationToken authenticationToken = AuthorizationTokenProvider.getToken(token);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                } catch (Exception e){
                    response.setStatus(FORBIDDEN.value());
                    Map<String, String> error = new HashMap<>();
                    error.put("error_message", e.getMessage());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), error);
                }
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }
}
