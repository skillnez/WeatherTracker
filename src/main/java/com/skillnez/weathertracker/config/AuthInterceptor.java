package com.skillnez.weathertracker.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthInterceptor implements HandlerInterceptor {

    //TODO добавлю как буду делать авторизацию
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie c : cookies) {
//                if ("SESSIONID".equals(c.getName())) {
//                    return true;
//                }
//            }
//        }
//        response.sendRedirect("/login");
//        return false;
//    }
}
