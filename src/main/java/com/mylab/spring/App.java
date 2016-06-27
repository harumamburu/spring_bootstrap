package com.mylab.spring;

import com.mylab.spring.client.Client;
import com.mylab.spring.config.DateTimeBeanConfig;
import com.mylab.spring.config.LoggingBeansConfig;
import com.mylab.spring.config.PropertiesPlaceholderBeanConfig;
import com.mylab.spring.event.Event;
import com.mylab.spring.event.EventType;
import com.mylab.spring.logging.EventLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class App {

    @Autowired
    private Client client;
    @Resource
    private Map<EventType, EventLogger> loggers;

    private App() {}

    private App(Client client, Map<EventType, EventLogger> loggers) {
        this.client = client;
        this.loggers = loggers;
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext();
        appContext.register(DateTimeBeanConfig.class, PropertiesPlaceholderBeanConfig.class, LoggingBeansConfig.class);
        appContext.scan("com.mylab.spring.client", "com.mylab.spring.event", "com.mylab.spring.logging");
        appContext.register(LoggingBeansConfig.class);
        appContext.scan("com.mylab.spring");
        appContext.refresh();

        App app = appContext.getBean("app", App.class);

        Event event = appContext.getBean("event", Event.class);
        event.setStringEvent("Event for user " + app.client.getId());
        app.logEvent(event);

        appContext.close();
    }

    private void logEvent(Event event) {
        event.setStringEvent(event.getStringEvent().replaceAll(String.valueOf(client.getId()), client.getFullName()));
        loggers.get(event.getType()).logEvent(event);
    }
}
