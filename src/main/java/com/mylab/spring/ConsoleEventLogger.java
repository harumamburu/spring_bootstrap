package com.mylab.spring;

public class ConsoleEventLogger implements EventLogger {

    public void logEvent(String event) {
        System.out.println(event);
    }
}
