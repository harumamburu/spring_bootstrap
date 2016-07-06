package com.mylab.spring.bootstrap.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@Aspect
public class LoggingAspect {

    private Logger LOG;
    @Value("${logging.error.pattern}")
    private String handlerPattern;

    @PostConstruct
    private void init() {
        LOG = Logger.getLogger(this.getClass().getSimpleName());
        try {
            FileHandler handler = new FileHandler(handlerPattern, true);
            handler.setLevel(Level.WARNING);
            handler.setFormatter(new SimpleFormatter());
            LOG.addHandler(handler);
        } catch (IOException exc) {
            LOG.log(Level.WARNING, "FAILED TO ACCESS ERROR LOG FILE", exc);
        }
    }

    @Pointcut("execution(* com.mylab.spring.bootstrap.logging.*.logEvent(com.mylab.spring.bootstrap.event.Event))")
    private void allLogEventMethods() {};

    @Pointcut("allLogEventMethods() && (within(com.mylab.spring.bootstrap.logging.*File*Logger)" +
            "|| within(com.mylab.spring.bootstrap.logging.*Cache*Logger))")
    private void allFileLogEventMethods() {};

    @Before("allLogEventMethods()")
    private void logBeforeEventLogging(JoinPoint joinPoint) {
        LOG.info("LOGGING EVENT AT: " +
                joinPoint.getTarget().getClass().getSimpleName() + " " +
                joinPoint.getSignature().getName() + "()");
    }

    @AfterReturning("allFileLogEventMethods()")
    private void logAfterEventLoggingToFile(JoinPoint joinPoint) {
        try {
            Field logFileField = joinPoint.getTarget().getClass().getDeclaredField("logFile");
            if (!logFileField.isAccessible()) {
                logFileField.setAccessible(true);
            }
            File log = (File) logFileField.get(joinPoint.getTarget());
            LOG.info("LOGGED EVENT TO: " + log.getAbsolutePath());
        } catch (NoSuchFieldException | IllegalAccessException exc) {
            throw new RuntimeException(exc);
        }
    }

    @AfterThrowing(pointcut = "this(com.mylab.spring.bootstrap.logging.EventLogger)", throwing = "exc")
    private void logAfterLoggingException(RuntimeException exc) {
        LOG.log(Level.WARNING, "!!! an error occurred while accessing event log file location info !!!", exc);
    }
}
