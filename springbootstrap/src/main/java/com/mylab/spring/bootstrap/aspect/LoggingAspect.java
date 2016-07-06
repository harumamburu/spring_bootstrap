package com.mylab.spring.bootstrap.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import javax.annotation.Resource;
import java.io.File;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

@Aspect
public class LoggingAspect {

    @Resource(name = "logger")
    private Logger LOG;

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
        LOG.log(Level.WARNING, "!!! an error occurred while accessing log file location info !!!", exc);
    }
}
