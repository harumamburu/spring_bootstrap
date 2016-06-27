package com.mylab.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.DateFormat;
import java.util.Date;

@Configuration
public class DateTimeBeanConfig {

    @Bean
    public DateFormat dateformat() {
        return DateFormat.getDateTimeInstance();
    }

    @Bean
    public Date date() {
        return new Date();
    }
}
