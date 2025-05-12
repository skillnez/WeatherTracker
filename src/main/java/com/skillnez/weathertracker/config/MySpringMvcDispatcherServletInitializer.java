package com.skillnez.weathertracker.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class MySpringMvcDispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] { AppConfig.class }; // сервисы, репозитории, БД
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] { SpringConfig.class }; // Spring MVC (ViewResolver, Interceptors)
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }
}
