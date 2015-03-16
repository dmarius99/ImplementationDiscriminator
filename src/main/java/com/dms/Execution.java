package com.dms;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * Execution, 11.03.2015
 *
 * Copyright (c) 2014 1&1 Internet AG. All rights reserved.
 *
 * @author mdinu
 * @version $Id$
 */

public class Execution {

    private ProceedingJoinPoint joinPoint;
    private Object myObject;
    private Object[] args;
    private Method method;

    public Execution(ProceedingJoinPoint joinPoint) {
        this.joinPoint = joinPoint;
    }

    public Execution(Object myObject, Method method, Object[] args) {
        this.myObject = myObject;
        this.args = args;
        this.method = method;
    }

    /********* custom methods from both AspectJ, Java Proxy and Java Reflection ************/

    public Object proceed() throws Throwable {
        if (joinPoint!=null) {
            return joinPoint.proceed();
        }
        return method.invoke(myObject, args);
    }

    /**
     * @return method name as String
     */
    public String getMethodAsString() {
        if (joinPoint!=null) {
            return joinPoint.getSignature().getName();
        }
        return method.getName();
    }

    public Class getSourceClass() {
        if (joinPoint!=null) {
            return joinPoint.getSourceLocation().getWithinType();
        }
        return myObject.getClass();
    }

    public Method getMethodFromJoinPoint() {
        if (joinPoint!=null) {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            return signature.getMethod();
        }
        return method;
    }

    public Object[] getArgs() {
        if (joinPoint!=null) {
            return joinPoint.getArgs();
        }
        return args;
    }
}
