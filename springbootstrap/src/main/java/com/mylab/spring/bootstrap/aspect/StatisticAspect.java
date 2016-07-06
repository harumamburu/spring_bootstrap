package com.mylab.spring.bootstrap.aspect;

import com.mylab.spring.bootstrap.logging.EventLogger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Aspect
public class StatisticAspect {

    private final static Map<Class<? extends EventLogger>, AtomicInteger> COUNTERS = new ConcurrentHashMap<>(4, 0.9f, 1);

    @Pointcut("execution(* com.mylab.spring.bootstrap.logging.*.logEvent(com.mylab.spring.bootstrap.event.Event))")
    private void logEventMethod() {};

    @AfterReturning("logEventMethod()")
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
