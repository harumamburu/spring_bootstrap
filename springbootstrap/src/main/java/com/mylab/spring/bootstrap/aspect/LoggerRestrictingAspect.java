package com.mylab.spring.bootstrap.aspect;

import com.mylab.spring.bootstrap.event.Event;
import com.mylab.spring.bootstrap.logging.ConsoleEventLogger;
import com.mylab.spring.bootstrap.logging.EventLogger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This aspect checks, how many messages were written to console during a stated period of time<br/>
 * If there are more than allowed, than the rest of events will be logged to another, injected logger.<br/>
 * Settings: how often events counter will be dropped and how many entries can be logged during the period
 * are taken from properties
 */
@Aspect
public class LoggerRestrictingAspect extends AbstractAspect {

    private final static Logger LOGGER = Logger.getLogger(LoggerRestrictingAspect.class.getSimpleName());

    private final static AtomicInteger COUNTER = new AtomicInteger();

    @Value("${logging.console.dumping.timeout.min}")
    private Integer logDumpingTimeout;

    @Value("${logging.console.dumping.maxentries}")
    private Integer maxLogs;

    @Resource(name = "fileLogger")
    private EventLogger backupLogger;


    @PostConstruct
    private void init() {
        // set a timer which will be dropping counter each 'n' minutes
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                COUNTER.set(0);
            }
        }, 0L, 1000L * 60 * logDumpingTimeout);
    }


    @Around("logEventMethods() && within(com.mylab.spring.bootstrap.logging.ConsoleEventLogger) && args(event)")
    private void checkConsoleLoggerRestriction(ProceedingJoinPoint point, Object event) {
        if (ConsoleEventLogger.class.isAssignableFrom(point.getTarget().getClass())
                && Event.class.isAssignableFrom(event.getClass())) {
            if (COUNTER.get() >= maxLogs) {
                backupLogger.logEvent((Event) event);
            } else {
                try {
                    point.proceed(new Object[]{ event });
                } catch (Throwable throwable) {
                    LOGGER.log(Level.WARNING, "FAILED TO PROCEED WITH CONSOLE EVENT LOGGING", throwable);
                }
            }
        }
    }
}
