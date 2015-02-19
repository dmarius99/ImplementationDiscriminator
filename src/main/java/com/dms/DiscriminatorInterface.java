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

    /**
     * The annotation full name.
     */
    String ANNOTATION_NAME = "@Discriminator";

    /**
     * The method name in the annotation for setting the aggregation flag.
     */
    String IS_RESULT_AGGREGATED = "isResultAggregated";

    /**
     * The method name to discriminate execution.
     */
    String DISCRIMINATE_EXECUTION_POINT = "discriminateExecutionPoint";

    /**
     * The default package name for this project.
     */
    String BASE_PACKAGE = "com.dms";

    /**
     * @param joinPoint the ProceedingJoinPoint
     * @return the return object
     * @throws Throwable if exception occurs
     */
    Object defaultIntercept(ProceedingJoinPoint joinPoint) throws Throwable;

    /**
     *
     * @return true if it was invoked/used, false otherwise.
     */
    boolean isActive();

    /**
     * Marks the discriminator as active, that is was invoked.
     */
    void activate();
}
