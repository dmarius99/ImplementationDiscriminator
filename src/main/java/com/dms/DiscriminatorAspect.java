package com.dms;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * DiscriminatorAspect, 17.10.2014
 *
 * Marius Dinu (marius.dinu@gmail.com). All rights reserved.
 */
@Aspect
@Named
class DiscriminatorAspect {

    @Inject
    private DiscriminatorInterface discriminatorInterface;

    @Pointcut("@target(com.dms.Discriminator)")
    public void getBeansAnnotatedWithDiscriminator() {
    }

    @Around("getBeansAnnotatedWithDiscriminator()")
    public Object intercept(ProceedingJoinPoint joinPoint) throws Throwable {
        return getDiscriminatorInterface().defaultIntercept(joinPoint);
    }

    DiscriminatorInterface getDiscriminatorInterface() {
        return discriminatorInterface;
    }

}
