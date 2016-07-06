package com.mylab.spring.bootstrap.aspect;

import com.mylab.spring.bootstrap.logging.EventLogger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Aspect
public class StatisticAspect extends AbstractAspect {

    private final static Map<Class<? extends EventLogger>, AtomicInteger> COUNTERS = new ConcurrentHashMap<>(4, 0.9f, 1);

    @AfterReturning("logEventMethods()")
    private void incrementConsoleWritings(JoinPoint joinPoint) {
        Class joinPointObjectType = joinPoint.getTarget().getClass();
        if (EventLogger.class.isAssignableFrom(joinPointObjectType)) {
            AtomicInteger counter = Optional.ofNullable(COUNTERS.get(joinPointObjectType)).orElse(new AtomicInteger());
            counter.incrementAndGet();
            COUNTERS.put(joinPointObjectType, counter);
        }
    }

    public Map<Class<? extends EventLogger>, AtomicInteger> getCounters() {
        return COUNTERS;
    }
}
