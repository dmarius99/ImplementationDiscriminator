package com.dms;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * DiscriminatorInterface, 23.10.2014
 *
 * Created by Marius Dinu (marius.dinu@gmail.com) on 27/09/14.
 */

/**
 *
 * @param <DiscriminatorType> the class type of the parameter used in choosing the implementation
 * @param <InterfaceType> the class type of the interface implemented by the implementation beans
 */
interface DiscriminatorInterface<DiscriminatorType, InterfaceType>
        extends Discriminate<DiscriminatorType, InterfaceType> {

    String ANNOTATION_NAME = "@Discriminator";
    String IS_DEFAULT = "isDefault";
    String IS_RESULT_AGGREGATED = "isResultAggregated";
    String DISCRIMINATE_EXECUTION_POINT = "discriminateExecutionPoint";
    String BASE_PACKAGE = "com.dms";

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
