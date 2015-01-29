package com.dms;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Created by Marius Dinu (marius.dinu@gmail.com) on 27/09/14.
 */
class MethodsHelper {

    static final Logger LOG = Logger.getAnonymousLogger();

    private Class interfaceClass;

    private Class discriminatorClass;

    /**
     * This type of methods should return the result from only one of the implementations
     */
    private Set<Method> methodsNotToBeIntercepted = new HashSet<Method>();

    /**
     * This type of methods should return the same result for any implementation.
     * Use only methods that return instances of Collection.
     */
    private Set<Method> methodsToBeAggregated = new HashSet<Method>();

    /**
     * Methods that will be intercepted by the aspect in order to be discriminated
     */
    private Set<Method> methodsToBeIntercepted = new HashSet<Method>();


    private Set<String> aggregatedMethods = null;

    private Set<String> unInterceptedMethods = null;

    private Set<String> interceptedMethods = null;

    /**
     * @param myInterfaceClass     the interface class
     * @param myDiscriminatorClass the discriminator class
     */
    MethodsHelper(Class myInterfaceClass, Class myDiscriminatorClass) {
        this.setInterfaceClass(myInterfaceClass);
        this.setDiscriminatorClass(myDiscriminatorClass);
        init();
    }

    MethodsHelper() {
    }

    Class getDiscriminatorClass() {
        return discriminatorClass;
    }

    void setDiscriminatorClass(Class discriminatorClass) {
        this.discriminatorClass = discriminatorClass;
    }

    Class getInterfaceClass() {
        return interfaceClass;
    }

    void setInterfaceClass(Class interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    Set<Method> getMethodsNotToBeIntercepted() {
        return methodsNotToBeIntercepted;
    }

    void setMethodsNotToBeIntercepted(Set<Method> methodsNotToBeIntercepted) {
        this.methodsNotToBeIntercepted = methodsNotToBeIntercepted;
    }

    Set<Method> getMethodsToBeAggregated() {
        return methodsToBeAggregated;
    }

    void setMethodsToBeAggregated(Set<Method> methodsToBeAggregated) {
        this.methodsToBeAggregated = methodsToBeAggregated;
    }

    public Set<String> getAggregatedMethods() {
        if (aggregatedMethods == null) {
            aggregatedMethods = new HashSet<String>();
            for (Method method : methodsToBeAggregated) {
                aggregatedMethods.add(method.getName());
            }
        }
        return aggregatedMethods;
    }

    public void setAggregatedMethods(Set<String> aggregatedMethods) {
        this.aggregatedMethods = aggregatedMethods;
    }

    public Set<String> getUnInterceptedMethods() {
        if (unInterceptedMethods == null) {
            unInterceptedMethods = new HashSet<String>();
            for (Method method : methodsNotToBeIntercepted) {
                unInterceptedMethods.add(method.getName());
            }
        }
        return unInterceptedMethods;
    }

    public void setUnInterceptedMethods(Set<String> unInterceptedMethods) {
        this.unInterceptedMethods = unInterceptedMethods;
    }

    /**
     * Initializes 3 sets on methods(interceptedMethods, unInterceptedMethods, aggregatedMethods)
     */
    private void initMethodsByRole() {
        Method methods[] = getInterfaceClass().getDeclaredMethods();

        for (Method method : methods) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes != null) {
                if (shouldMethodBeIntercepted(parameterTypes)) {
                    methodsToBeIntercepted.add(method);
                } else {
                    if (shouldMethodBeAggregated(method)) {
                        methodsToBeAggregated.add(method);
                    } else {
                        methodsNotToBeIntercepted.add(method);
                    }
                }
            } else {
                if (shouldMethodBeAggregated(method)) {
                    methodsToBeAggregated.add(method);
                } else {
                    methodsNotToBeIntercepted.add(method);
                }
            }
        }
    }

    private boolean shouldMethodBeIntercepted(Class<?>[] parameterTypes) {
        boolean isIntercepted = false;
        for (Class clazz : parameterTypes) {
            if (getDiscriminatorClass().isAssignableFrom(clazz)) {
                isIntercepted = true;
            }
        }
        return isIntercepted;
    }

    @SuppressWarnings( { "unchecked" } )
    private boolean shouldMethodBeAggregated(Method method) {
        return Collection.class.isAssignableFrom(method.getReturnType());
    }

    /**
     * Initializes the method sets
     */
    void init() {
        initMethodsByRole();
        logInitialization();
    }

    private void logInitialization() {
        LOG.info("******************************************");
        LOG.info("Methods to be intercepted " + methodsToBeIntercepted.size() + ": ");
        for (Method method : methodsToBeIntercepted) {
            LOG.info("> " + method.getName() + ":" + method.toString());
        }
        LOG.info("******************************************");
        LOG.info("Methods to be aggregated(return type is a Collection) " + methodsToBeAggregated.size() + ": ");
        for (Method method : methodsToBeAggregated) {
            LOG.info("> " + method.getName() + ":" + method.toString());
        }
        LOG.info("******************************************");
        LOG.info("Methods not to be intercepted " + methodsNotToBeIntercepted.size() + ": ");
        for (Method method : methodsNotToBeIntercepted) {
            LOG.info("> " + method.getName() + ":" + method.toString());
        }
        LOG.info("******************************************");
    }

    public Set<String> getInterceptedMethods() {
        if (interceptedMethods == null) {
            interceptedMethods = new HashSet<String>();
            for (Method method : methodsToBeIntercepted) {
                interceptedMethods.add(method.getName());
            }
        }
        return interceptedMethods;
    }

    public void setInterceptedMethods(Set<String> interceptedMethods) {
        this.interceptedMethods = interceptedMethods;
    }
}
