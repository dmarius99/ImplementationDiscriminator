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

    /**
     * DiscriminatorInterface injected here in order to delegate the defaultIntercept.
     */
    @Inject
    private DiscriminatorInterface discriminatorInterface;

    /**
     * Default constructor.
     */
    DiscriminatorAspect() {
    }

    /**
     * Constructor with injected DiscriminatorInterface.
     * *
     * @param myDiscriminatorInterface the discriminator default implementation
     */
    DiscriminatorAspect(final DiscriminatorInterface myDiscriminatorInterface) {
        this.discriminatorInterface = myDiscriminatorInterface;
    }

    /**
     * This is Spring style Pointcut: @target(com.dms.Discriminator).
     * The other choice would have been using AspectJ style Pointcut:
     *     execution(@com.dms.Discriminator * *(..))
     */
    @Pointcut("@target(com.dms.Discriminator)")
    public void getBeansAnnotatedWithDiscriminator() {
    }

    /**
     * Use the previously defined aspect.
     *
     * @param joinPoint ProceedingJoinPoint
     * @return Object the return object
     * @throws Throwable if exception is thrown
     */
    @Around("getBeansAnnotatedWithDiscriminator()")
    public Object intercept(final ProceedingJoinPoint joinPoint) throws Throwable {
        discriminatorInterface.activate();
        return discriminatorInterface.defaultIntercept(joinPoint);
    }

}
