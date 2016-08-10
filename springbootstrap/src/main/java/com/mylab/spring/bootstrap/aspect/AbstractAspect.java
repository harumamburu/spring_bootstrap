package com.mylab.spring.bootstrap.aspect;

import org.aspectj.lang.annotation.Pointcut;

public abstract class AbstractAspect {

    @Pointcut("execution(* com.mylab.spring.bootstrap.logging.*.logEvent(com.mylab.spring.bootstrap.event.Event))")
    protected void logEventMethods() {};
}
