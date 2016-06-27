package com.mylab.spring.config;

import com.mylab.spring.event.EventType;
import com.mylab.spring.logging.EventLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class LoggingBeansConfig {

    @Bean
    @Autowired
    public Map<EventType, EventLogger> loggers(@Qualifier("consoleLogger")EventLogger consoleLogger,
                                               @Qualifier("cachedLogger")EventLogger cacheLogger,
                                               @Qualifier("combinedLogger")EventLogger combinedLogger) {
        Map<EventType, EventLogger> loggers = new HashMap<>(3);
        loggers.put(EventType.INFO, consoleLogger);
        loggers.put(EventType.ERROR, combinedLogger);
        loggers.put(null, cacheLogger);
        return loggers;
    }

    @Bean
    public List<EventLogger> combinedLogger(@Qualifier("consoleLogger")EventLogger consoleLogger,
                                            @Qualifier("fileLogger")EventLogger fileLogger) {
        return Arrays.asList(consoleLogger, fileLogger);
    }
}
