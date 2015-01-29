package com.dms;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * DiscriminatorInterface, 23.10.2014
 * <p/>
 * Created by Marius Dinu (marius.dinu@gmail.com) on 27/09/14.
 */

interface DiscriminatorInterface<DiscriminatorType, InterfaceType> extends Discriminate<DiscriminatorType, InterfaceType> {

    static final String ANNOTATION_NAME = "@Discriminator";
    static final String IS_DEFAULT = "isDefault";
    static final String IS_RESULT_AGGREGATED = "isResultAggregated";
    static final String BASE_PACKAGE = "com.dms";

    /**
     * @param joinPoint the ProceedingJoinPoint
     * @return the return object
     * @throws Throwable if exception occurs
     */
    Object defaultIntercept(ProceedingJoinPoint joinPoint) throws Throwable;

    void activate();

    void deactivate();

    boolean isActive();
}
