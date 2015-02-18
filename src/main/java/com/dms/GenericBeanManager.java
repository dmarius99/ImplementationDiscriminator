package com.dms;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

import javax.annotation.PostConstruct;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * GenericBeanManager, 16.02.2015
 *
 * Marius Dinu (marius.dinu@gmail.com). All rights reserved.
 */
class GenericBeanManager<InterfaceType> implements BeanManager {

    /**
     * The Aggregation flag for methods that return Collection.
     */
    private boolean resultAggregated = false;

    /**
     * All implementations with @Discriminator annotation that implements InterfaceType.
     */
    private final Map<String, InterfaceType> implementations = new HashMap<String, InterfaceType>();

    private final Class interfaceClass;

    public GenericBeanManager(Class myInterfaceClass) {
        this.interfaceClass = myInterfaceClass;
        initDependencies();
    }

    @PostConstruct
    private void initDependencies() {
        Set<Class<?>> myImplementations = getImplementations(interfaceClass);
        for (Class clazz:myImplementations) {
            implementations.put(clazz.getName(), (InterfaceType)createInstance(clazz));
            resultAggregated = isResultAggregated() || getIsResultAggregated(clazz);
        }
    }

    @Override
    public Map<String, InterfaceType> getImplementationBeans() {
        return implementations;
    }

    @Override
    public boolean isResultAggregated() {
        return resultAggregated;
    }

    private Set<Class<?>> getImplementations(Class clazz) {
        Reflections reflections = new Reflections(new SubTypesScanner(), new TypeAnnotationsScanner());
        Set<Class<?>> classesAnnotated = reflections.getTypesAnnotatedWith(Discriminator.class);
        Set<Class<?>> classesImplementingInterface = reflections.getSubTypesOf(clazz);
        classesAnnotated.retainAll(classesImplementingInterface);
        MethodsHelper.LOG.info(
                "Found " + classesAnnotated.size() + " classes implementing same interface and classes annotated " +
                        "with " + DiscriminatorInterface.ANNOTATION_NAME + " : ");
        for (Class implClass: classesAnnotated) {
            MethodsHelper.LOG.info(implClass + ", ");
        }
        return classesAnnotated;
    }

    private Boolean getIsResultAggregated(Class clazz) {
        Discriminator annotation = (Discriminator) clazz.getAnnotation(Discriminator.class);
        return annotation.isResultAggregated();
    }

    static Object createInstance(Class clazz) {
        Constructor<?> constructor;
        Object object;
        try {
            constructor = clazz.getConstructor();
            object = constructor.newInstance();
        } catch (NoSuchMethodException | InstantiationException |
                IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("No newInstance for " + clazz);
        }
        return object;
    }

}
