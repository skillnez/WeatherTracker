package com.skillnez.weathertracker.config;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.SessionTrackingMode;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import java.util.Collections;

public class MySpringMvcDispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{AppConfig.class}; // сервисы, репозитории, БД
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{SpringConfig.class}; // Spring MVC (ViewResolver, Interceptors)
    }

    //был специфичный баг, когда в url попадала сессия для сохранения аттрибутов между редиректами
    //redirectAttributes.addFlashAttribute, это не опасно, потому что я использую свои сессии
    //но на всякий случай я решил это отсечь тут:
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
        servletContext.setSessionTrackingModes(Collections.singleton(SessionTrackingMode.COOKIE));
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}
