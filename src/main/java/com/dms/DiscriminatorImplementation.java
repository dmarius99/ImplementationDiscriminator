package com.dms;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.Collection;

/**
 * Created by Marius Dinu (marius.dinu@gmail.com) on 27/09/14.
 *
 * @param <DiscriminatorType> the wrapper type to discriminate on
 * @param <InterfaceType>     the interface for all implementations
 */
public abstract class DiscriminatorImplementation<DiscriminatorType, InterfaceType>
        extends DiscriminatorInitializer<DiscriminatorType, InterfaceType> {

    /**
     * The aspect around all methods for common interface of all implementations.
     *
     * @param joinPoint ProceedingJoinPoint
     * @return the return type
     * @throws Throwable if any exception appears
     */
    public final Object defaultIntercept(final ProceedingJoinPoint joinPoint) throws Throwable {
        if (isActive()) {
            return discriminateExecutionPoint(joinPoint);
        } else {
            return joinPoint.proceed();
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
    private Object discriminateExecutionPoint(final ProceedingJoinPoint joinPoint) throws Throwable {
        if (methodShouldNotBeIntercepted(getMethod(joinPoint))) {
            return joinPoint.proceed();
        } else {
            //this is the case when method should be intercepted
            if (isResultAggregated()) {
                if (methodShouldBeAggregated(getMethod(joinPoint))) {
                    return aggregateLists(joinPoint);
                } else {
                    return returnResultObject(joinPoint);
                }
            } else {
                return returnResultObject(joinPoint);
            }
        }
    }

    private Object returnResultObject(final ProceedingJoinPoint joinPoint) throws Throwable {
        DiscriminatorType discriminatorParameter = getDiscriminatorParameter(joinPoint);
        if (discriminatorParameter == null) {
            return joinPoint.proceed();
        }
        InterfaceType implementationLayer = getImplementationForDiscriminator(discriminatorParameter);

        if (implementationLayer == null) {
            throwError(new IllegalStateException());
        }

        return runOnImplementation(implementationLayer, joinPoint);
    }

    /**
     * @param joinPoint ProceedingJoinPoint
     * @return method name
     */
    private String getMethod(final ProceedingJoinPoint joinPoint) {
        return joinPoint.getSignature().getName();
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
    private DiscriminatorType getDiscriminatorParameter(final ProceedingJoinPoint joinPoint) {
        for (Object o : joinPoint.getArgs()) {
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
    private Object runOnImplementation(final InterfaceType implementationLayer, final ProceedingJoinPoint joinPoint) {
        Method method = getMethodFromJoinPoint(joinPoint);
        Object[] args = joinPoint.getArgs();
        try {
            //if the thread is from the implementationLayer then proceed
            //otherwise it will run into cyclic issues
            if (isImplementationLayerJoinPointImplementation(implementationLayer, joinPoint)) {
                return joinPoint.proceed();
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
    private Object aggregateLists(final ProceedingJoinPoint joinPoint) throws Throwable {
        Collection defaultList = (Collection) joinPoint.proceed();

        for (InterfaceType serviceLayer : getImplementations().values()) {
            Object list = runOnImplementation(serviceLayer, joinPoint);
            if (isCollectionNotEmpty(list)) {
                defaultList.addAll((Collection) list);
            }
        }
        return defaultList;
    }

    private boolean isCollectionNotEmpty(final Object list) {
        return list != null && list instanceof Collection && ((Collection) list).size() > 0;
    }

    private Method getMethodFromJoinPoint(final JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }

    private boolean isImplementationLayerJoinPointImplementation(final InterfaceType implementationLayer,
                                                                 final ProceedingJoinPoint joinPoint) {
        Class targetClass = implementationLayer.getClass();
        Class joinPointClass = joinPoint.getSourceLocation().getWithinType();
        return joinPointClass.isAssignableFrom(targetClass) || targetClass.isAssignableFrom(joinPointClass);
    }

}
