package com.skillnez.weathertracker.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = "com.skillnez.weathertracker")
@PropertySource("classpath:application.properties") //TODO записать проперти
//@EnableTransactionManagement //TODO надо создать бин TransactionManager с sessionFactory
public class AppConfig {
    // БД, Flyway, Hibernate и пр. конфигурация
}