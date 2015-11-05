package com.mylab.spring;

import com.mylab.spring.client.Client;
import com.mylab.spring.logging.ConsoleEventLogger;
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

        app.logEvent("Event for user 1");
        app.logEvent("Event for user 2");
    }

    private void logEvent(String event) {
        String message = event.replaceAll(String.valueOf(client.getId()), client.getFullName());
        eventLogger.logEvent(message);
    }
}
