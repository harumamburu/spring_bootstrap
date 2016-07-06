package com.mylab.spring.bootstrap.aspect;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.concurrent.atomic.AtomicInteger;

@Aspect
public class StatisticAspect {

    private final static AtomicInteger CONSOLE_WRITES_COUNTER = new AtomicInteger();
    private final static AtomicInteger FILE_WRITES_COUNTER = new AtomicInteger();
    private final static AtomicInteger CACHE_WRITES_COUNTER = new AtomicInteger();
    private final static AtomicInteger COMBINED_WRITES_COUNTER = new AtomicInteger();

    @Pointcut("execution(* *.logEvent(com.mylab.spring.bootstrap.event.Event))")
    private void logEventMethod() {};

    @AfterReturning("logEventMethod() && within(com.mylab.spring.bootstrap.logging.ConsoleEventLogger)")
    private void incrementConsoleWritings() {
        CONSOLE_WRITES_COUNTER.incrementAndGet();
    }

    @AfterReturning("logEventMethod() && within(com.mylab.spring.bootstrap.logging.FileEventLogger)")
    private void incrementFileWritings() {
        FILE_WRITES_COUNTER.incrementAndGet();
    }

    @AfterReturning("logEventMethod() && within(com.mylab.spring.bootstrap.logging.CacheEventLogger)")
    private void incrementCacheWriting() {
        CACHE_WRITES_COUNTER.incrementAndGet();
    }

    @AfterReturning("logEventMethod() && within(com.mylab.spring.bootstrap.logging.CombinedEventLogger)")
    private void incrementCombinedWriting() {
        COMBINED_WRITES_COUNTER.incrementAndGet();
    }
}
