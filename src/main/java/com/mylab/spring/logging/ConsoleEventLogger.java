package com.mylab.spring.logging;

public class ConsoleEventLogger implements EventLogger {

    public void logEvent(String event) {
        System.out.println(event);
    }
}
