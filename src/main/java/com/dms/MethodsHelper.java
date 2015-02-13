package com.dms;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Marius Dinu (marius.dinu@gmail.com) on 27/09/14.
 */
class MethodsHelper implements MethodsHelperOverrideCapabilities {

    /**
     * The Logger for this annotation usage and initialization.
     */

    static final Logger LOG = Logger.getAnonymousLogger();

    /**
     * The class type of the interface implemented by the implementation beans.
     */

    private Class interfaceClass;

    /**
     * The class type of the parameter used in choosing the implementation.
     */
    private Class discriminatorClass;

    /**
     * This type of methods should return the result from only one of the implementations.
     */
    private final Set<Method> methodsNotToBeIntercepted = new HashSet<Method>();

    /**
     * This type of methods should return the same result for any implementation.
     * Use only methods that return instances of Collection.
     */
    private final Set<Method> methodsToBeAggregated = new HashSet<Method>();

    /**
     * Methods that will be intercepted by the aspect in order to be discriminated.
     */
    private final Set<Method> methodsToBeIntercepted = new HashSet<Method>();

    /**
     * The set of aggregated methods to be overridden by user.
     */
    private Set<String> aggregatedMethods = null;

    /**
     * The set of un-intercepted methods to be overridden by user.
     */
    private Set<String> unInterceptedMethods = null;

    /**
     * The set of intercepted methods to be overridden by user.
     */
    private Set<String> interceptedMethods = null;

    /**
     * @param myInterfaceClass     the interface class
     * @param myDiscriminatorClass the discriminator class
     */
    MethodsHelper(final Class myInterfaceClass, final Class myDiscriminatorClass) {
        this.setInterfaceClass(myInterfaceClass);
        this.setDiscriminatorClass(myDiscriminatorClass);
        init();
    }

    /**
     * Default constructor for MethodsHelper.
     */
    MethodsHelper() {
        LOG.info("Loading methods...");
    }

    Class getDiscriminatorClass() {
        return discriminatorClass;
    }

    void setDiscriminatorClass(final Class discriminatorClass) {
        this.discriminatorClass = discriminatorClass;
    }

    Class getInterfaceClass() {
        return interfaceClass;
    }

    void setInterfaceClass(final Class interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    @Override
    public Set<String> getAggregatedMethods() {
        return getMethods(aggregatedMethods, methodsToBeAggregated);
    }

    final void setAggregatedMethods(Set<String> aggregatedMethods) {
        this.aggregatedMethods = aggregatedMethods;
    }

    @Override
    public Set<String> getUnInterceptedMethods() {
        return getMethods(unInterceptedMethods, methodsNotToBeIntercepted);
    }

    final void setUnInterceptedMethods(Set<String> unInterceptedMethods) {
        this.unInterceptedMethods = unInterceptedMethods;
    }

    @Override
    public Set<String> getInterceptedMethods() {
        return getMethods(interceptedMethods, methodsToBeIntercepted);
    }

    final void setInterceptedMethods(Set<String> interceptedMethods) {
        this.interceptedMethods = interceptedMethods;
    }

    Set<String> getMethods(Set<String> methodsAsStrings, Set<Method> methodsAsObjects) {
        if (methodsAsStrings == null) {
            methodsAsStrings = new HashSet<String>();
            for (Method method : methodsAsObjects) {
                if (!methodsAsStrings.add(method.getName())) {
                    throwMethodsNotUnique(method.getName());
                }
            }
        }
        return methodsAsStrings;
    }

    /**
     * Initializes 3 sets on methods(interceptedMethods, unInterceptedMethods, aggregatedMethods).
     */
    private void initMethodsByRole() {
        Method[] methods = getInterfaceClass().getDeclaredMethods();

        for (Method method : methods) {
            if (shouldMethodBeIntercepted(method)) {
                methodsToBeIntercepted.add(method);
            } else {
                if (shouldMethodBeAggregated(method)) {
                    methodsToBeAggregated.add(method);
                } else {
                    methodsNotToBeIntercepted.add(method);
                }
            }
        }
    }

    @SuppressWarnings({ "unchecked" })
    boolean shouldMethodBeIntercepted(Method method) {
        if (method==null || method.getParameterTypes()==null || method.getParameterTypes().length==0) {
            return false;
        }
        for (Class clazz : method.getParameterTypes()) {
            if (getDiscriminatorClass().isAssignableFrom(clazz)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings({ "unchecked" })
    boolean shouldMethodBeAggregated(Method method) {
        return Collection.class.isAssignableFrom(method.getReturnType());
    }

    /**
     * Initializes the 3 method type sets(aggregated, intercepted and not-intercepted).
     */
    void init() {
        initMethodsByRole();
        setInterceptedMethods(getMethods(interceptedMethods, methodsToBeIntercepted));
        setUnInterceptedMethods(getMethods(unInterceptedMethods, methodsNotToBeIntercepted));
        setAggregatedMethods(getMethods(aggregatedMethods, methodsToBeAggregated));
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

    /**
     * Throws an RuntimeException.
     *
     * @param t Exception
     */
    void throwError(Throwable t) {
        LOG.log(Level.SEVERE, "Critical error in DiscriminatorImplementation: ", t);
        throw new RuntimeException("Critical error in DiscriminatorImplementation: ", t);
    }

    void throwMethodsNotUnique(String methodName) {
        throw new RuntimeException("Method name not unique: " + methodName);
    }
}
