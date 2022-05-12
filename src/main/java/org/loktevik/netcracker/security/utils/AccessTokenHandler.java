package org.loktevik.netcracker.security.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.Cookie;

public class AccessTokenHandler {

    public static String getAccessToken(Cookie[] cookies){
        if (cookies != null){
            for (Cookie cookie : cookies){
                if (cookie.getName().equals("access_token"))
                    return cookie.getValue();
            }
        }

        return "";
    }

    public static String getRefreshToken(Cookie[] cookies){
        for (Cookie cookie : cookies){
            if (cookie.getName().equals("refresh_token"))
                return cookie.getValue();
        }
        return "";
    }

    public static HttpHeaders getHttpHeaders(Cookie[] cookies){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(getAccessToken(cookies));
        return httpHeaders;
    }

    public static <T> HttpEntity<T> getHttpEntity(T object, Cookie [] cookies){
        return new HttpEntity<>(object, getHttpHeaders(cookies));
    }
}
