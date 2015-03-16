package com.dms;

import java.lang.reflect.Method;
import java.util.Collection;

/**
 * Created by Marius Dinu (marius.dinu@gmail.com) on 27/09/14.
 *
 * @param <DiscriminatorType> the wrapper type to discriminate on
 * @param <InterfaceType>     the interface for all implementations
 */
public class DiscriminatorImplementation<DiscriminatorType, InterfaceType>
        extends DiscriminatorInitializer<DiscriminatorType, InterfaceType> {

    public DiscriminatorImplementation(Class discriminatorClass, Class interfaceClass) {
        super(discriminatorClass, interfaceClass);
    }

    public DiscriminatorImplementation() {
    }

    /**
     * The aspect around all methods for common interface of all implementations.
     *
     * @param joinPoint ProceedingJoinPoint
     * @return the return type
     * @throws Throwable if any exception appears
     */
    public final Object defaultIntercept(final Execution execution) throws Throwable {
        //TODO
        activate();
        if (isActive()) {
            return discriminateExecutionPoint(execution);
        } else {
            return execution.proceed();
        }
    }

    //******************** private methods ********************

    /**
     * The aspect around all methods for common interface of all implementations.
     *
     * @param joinPoint ProceedingJoinPoint
     * @return the return type
     * @throws Throwable if any exception appears
     */
    private Object discriminateExecutionPoint(final Execution execution) throws Throwable {
        if (methodShouldNotBeIntercepted(execution.getMethodAsString())) {
            return execution.proceed();
        } else {
            //this is the case when method should be intercepted
            if (isResultAggregated()) {
                if (methodShouldBeAggregated(execution.getMethodAsString())) {
                    return aggregateLists(execution);
                } else {
                    return returnResultObject(execution);
                }
            } else {
                return returnResultObject(execution);
            }
        }
    }

    private Object returnResultObject(final Execution execution) throws Throwable {
        DiscriminatorType discriminatorParameter = getDiscriminatorParameter(execution);
        if (discriminatorParameter == null) {
            return execution.proceed();
        }
        InterfaceType implementationLayer = getImplementationForDiscriminator(discriminatorParameter);

        if (implementationLayer == null) {
            throwError(new IllegalStateException());
        }

        return runOnImplementation(implementationLayer, execution);
    }

    /**
     * @param methodName the method name
     * @return true if method should not be intercepted
     */
    private boolean methodShouldNotBeIntercepted(final String methodName) {
        return getUnInterceptedMethods().contains(methodName);
    }

    /**
     * @param methodName the method name
     * @return true if method should be aggregated
     */
    private boolean methodShouldBeAggregated(final String methodName) {
        return getAggregatedMethods().contains(methodName);
    }

    /**
     * @param joinPoint ProceedingJoinPoint
     * @return DiscriminatorType discriminator object parameter
     */
    private DiscriminatorType getDiscriminatorParameter(final Execution execution) {
        for (Object o : execution.getArgs()) {
            if (getDiscriminatorClass().isAssignableFrom(o.getClass())) {
                return (DiscriminatorType) o;
            }
        }
        return null;
    }

    /**
     * @param implementationLayer Object implementation on which to execute the method
     * @param joinPoint           ProceedingJoinPoint
     * @return the return object
     */
    private Object runOnImplementation(final InterfaceType implementationLayer, final Execution execution) {
        Method method = execution.getMethodFromJoinPoint();
        Object[] args = execution.getArgs();
        try {
            //if the thread is from the implementationLayer then proceed
            //otherwise it will run into cyclic issues
            if (isImplementationLayerJoinPointImplementation(implementationLayer, execution)) {
                return execution.proceed();
            }
            //if implementationLayer is the desired path return it's result
            return method.invoke(implementationLayer, args);
        } catch (Throwable e) {
            throwError(e);
        }
        return null;
    }

    /**
     * Aggregate lists from different Implementations.
     *
     * @param joinPoint ProceedingJoinPoint
     * @return the return method object
     * @throws Throwable if RuntimeExceptions occurs
     */
    private Object aggregateLists(final Execution execution) throws Throwable {
        Collection defaultList = (Collection) execution.proceed();

        for (InterfaceType serviceLayer : getImplementations().values()) {
            Object list = runOnImplementation(serviceLayer, execution);
            if (isCollectionNotEmpty(list)) {
                defaultList.addAll((Collection) list);
            }
        }
        return defaultList;
    }

    private boolean isCollectionNotEmpty(final Object list) {
        return list != null && list instanceof Collection && ((Collection) list).size() > 0;
    }

    private boolean isImplementationLayerJoinPointImplementation(final InterfaceType implementationLayer,
                                                                 final Execution execution) {
        Class targetClass = implementationLayer.getClass();
        Class joinPointClass = execution.getSourceClass();
        return joinPointClass.isAssignableFrom(targetClass) || targetClass.isAssignableFrom(joinPointClass);
    }

    @Override
    public InterfaceType getImplementationForDiscriminator(DiscriminatorType parameter) {
        Collection<InterfaceType> implementationTypes = getImplementations().values();
        for (InterfaceType implementation:implementationTypes) {
            if (getTypeArgumentForImplementationClass(implementation.getClass(), 0).equals(parameter.getClass())) {
                return implementation;
            }
        }
        throw new IllegalStateException("No implementation found for parameter.");
    }
}
