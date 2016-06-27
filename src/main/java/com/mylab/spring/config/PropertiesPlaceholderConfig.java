package com.mylab.spring.config;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class PropertiesPlaceholderConfig {

    @Bean
    public PropertyPlaceholderConfigurer properties() {
        PropertyPlaceholderConfigurer configurer = new PropertyPlaceholderConfigurer();
        configurer.setSystemPropertiesMode(PropertyPlaceholderConfigurer.SYSTEM_PROPERTIES_MODE_OVERRIDE);
        configurer.setIgnoreResourceNotFound(false);
        configurer.setLocations(new ClassPathResource("properties/client.properties"),
                new ClassPathResource("properties/logging.properties"));
        return configurer;
    }
}
