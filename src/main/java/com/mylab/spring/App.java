package com.mylab.spring;

import com.mylab.spring.client.Client;
import com.mylab.spring.event.Event;
import com.mylab.spring.logging.EventLogger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {

    private Client client;
    private EventLogger eventLogger;

    private App(Client client, EventLogger logger) {
        this.client = client;
        this.eventLogger = logger;
    }

    public static void main(String[] args) {
        ApplicationContext appContext = new ClassPathXmlApplicationContext("spring-config.xml");

        App app = appContext.getBean("app", App.class);

        Event event = appContext.getBean("event", Event.class);
        event.setStringEvent("Event for user " + app.client.getId());

        app.logEvent(event);

    }

    private void logEvent(Event event) {
        event.setStringEvent(event.getStringEvent().replaceAll(String.valueOf(client.getId()), client.getFullName()));
        eventLogger.logEvent(event);
    }
}
