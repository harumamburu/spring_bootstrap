package com.mylab.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.DateFormat;

@Configuration
public class DateTimeBeanConfig {

    @Bean(name = "dateTime")
    public DateFormat dateformat() {
        return DateFormat.getDateTimeInstance();
    }
}
