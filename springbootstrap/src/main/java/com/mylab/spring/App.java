package com.mylab.spring;

import com.mylab.spring.client.Client;
import com.mylab.spring.event.Event;
import com.mylab.spring.event.EventType;
import com.mylab.spring.logging.EventLogger;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;

public class App {

    private final Client client;
    private final Map<EventType, EventLogger> loggers;

    private App(Client client, Map<EventType, EventLogger> loggers) {
        this.client = client;
        this.loggers = loggers;
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext appContext = new ClassPathXmlApplicationContext("spring-config.xml");

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