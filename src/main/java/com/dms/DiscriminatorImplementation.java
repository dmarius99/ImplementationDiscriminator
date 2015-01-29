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
 * @param <InterfaceType> the interface for all implementations
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
    public Object defaultIntercept(ProceedingJoinPoint joinPoint) throws Throwable {
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
     * @param jp ProceedingJoinPoint
     * @return the return type
     * @throws Throwable if any exception appears
     */
    private Object discriminateExecutionPoint(ProceedingJoinPoint jp) throws Throwable {
        if (methodShouldNotBeIntercepted(getMethod(jp))) {
            return jp.proceed();
        } else {
            //this is the case when method should be intercepted
            if (isResultAggregated()) {
                if (methodShouldBeAggregated(getMethod(jp))) {
                    return aggregateLists(jp);
                } else {
                    return returnResultObject(jp);
                }
            } else {
                return returnResultObject(jp);
            }
        }
    }

    private Object returnResultObject(ProceedingJoinPoint jp) throws Throwable {
        DiscriminatorType discriminatorParameter = getDiscriminatorParameter(jp);
        if (discriminatorParameter == null) {
            return jp.proceed();
        }
        InterfaceType implementationLayer = getImplementationForDiscriminator(discriminatorParameter);

        if (implementationLayer == null) {
            throwError(new IllegalStateException());
        }

        return runOnImplementation(implementationLayer, jp);
    }

    /**
     * @param jp ProceedingJoinPoint
     * @return method name
     */
    private String getMethod(ProceedingJoinPoint jp) {
        return jp.getSignature().getName();
    }

    /**
     * @param methodName the method name
     * @return true if method should not be intercepted
     */
    private boolean methodShouldNotBeIntercepted(String methodName) {
        return getUnInterceptedMethods().contains(methodName);
    }

    /**
     * @param methodName the method name
     * @return true if method should be aggregated
     */
    private boolean methodShouldBeAggregated(String methodName) {
        return getAggregatedMethods().contains(methodName);
    }

    /**
     * @param jp ProceedingJoinPoint
     * @return DiscriminatorType discriminator object parameter
     */
    private DiscriminatorType getDiscriminatorParameter(ProceedingJoinPoint jp) {
        for (Object o : jp.getArgs()) {
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
    private Object runOnImplementation(InterfaceType implementationLayer, ProceedingJoinPoint joinPoint) {
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
     */
    private Object aggregateLists(ProceedingJoinPoint joinPoint) throws Throwable {
        Collection defaultList = (Collection) joinPoint.proceed();

        for (InterfaceType serviceLayer : getImplementations().values()) {
            Object list = runOnImplementation(serviceLayer, joinPoint);
            if (isCollectionNotEmpty(list)) {
                defaultList.addAll((Collection) list);
            }
        }
        return defaultList;
    }

    private boolean isCollectionNotEmpty(Object list) {
        return list != null && list instanceof Collection && ((Collection) list).size() > 0;
    }

    private Method getMethodFromJoinPoint(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }

    private boolean isImplementationLayerJoinPointImplementation(InterfaceType implementationLayer,
                                                                 ProceedingJoinPoint joinPoint) {
        Class targetClass = implementationLayer.getClass();
        Class joinPointClass = joinPoint.getSourceLocation().getWithinType();
        return joinPointClass.isAssignableFrom(targetClass) || targetClass.isAssignableFrom(joinPointClass);
    }

}
