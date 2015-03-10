package com.dms;

import org.springframework.aop.framework.Advised;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AspectJTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.beans.Introspector;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by Marius Dinu (marius.dinu@gmail.com) on 23/12/14.
 *
 * @param <InterfaceType> the class type of the interface implemented by the implementation beans
 *                        that will be initialized after bean construction
 */
@Named("beanManager")
class BeanManagerForSpring<InterfaceType> implements BeanManager {

    /**
     * The ClassLoader on which the implementation beans are searched for.
     */
    private static final ClassLoader CLASS_LOADER = BeanManagerForSpring.class.getClassLoader();

    /**
     * The Spring ApplicationContext to be injected in Spring container.
     */
    @Inject
    private ApplicationContext applicationContext;

    /**
     * The Aggregation flag for methods that return Collection.
     */
    private boolean resultAggregated = false;

    /**
     * All implementations with @Discriminated annotation that implements InterfaceType.
     */
    private SortedMap<String, InterfaceType> implementations;

    @PostConstruct
    void init() {
        implementations = Collections.unmodifiableSortedMap(getDependencies());
    }

    private SortedMap<String, InterfaceType> getDependencies() {
        SortedMap<String, InterfaceType> initImplementations= new TreeMap<String, InterfaceType>();
        Set<BeanDefinition> scannedImplementations = getScannedImplementations();
        for (BeanDefinition bean : scannedImplementations) {
            if (isBeanAvailable(bean)) {
                initImplementations.put(bean.getBeanClassName(), getImplementationBean(bean));
                resultAggregated = isResultAggregated() || getIsResultAggregated(bean);
            }
        }
        return initImplementations;
    }

    @SuppressWarnings({ "unchecked" })
    @Override
    public SortedMap<String, InterfaceType> getImplementationBeans() {
        return implementations;
    }

    /**
     *
     * @param proxy one implementation of the interface (reference to a proxy)
     * @param <InterfaceType> the interface of the implementations
     * @return the target implementation behind the proxy
     */
    @SuppressWarnings({ "unchecked" })
    <InterfaceType> InterfaceType getTargetObject(final InterfaceType proxy) {
        try {
            return (InterfaceType) ((Advised) proxy).getTargetSource().getTarget();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings({ "unchecked" })
    private InterfaceType getImplementationBean(final BeanDefinition beanDefinition) {
        String beanName = getBeanName(beanDefinition);
        InterfaceType beanInterfaceType = (InterfaceType) getApplicationContext().getBean(beanName);
        return getTargetObject(beanInterfaceType);
    }

    @Override
    public boolean isResultAggregated() {
        return resultAggregated;
    }

    Set<BeanDefinition> getScannedImplementations() {
        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false);
        scanner.setResourceLoader(new PathMatchingResourcePatternResolver(CLASS_LOADER));
        scanner.addIncludeFilter(new AnnotationTypeFilter(Discriminated.class));
        scanner.addExcludeFilter(new AspectJTypeFilter(DiscriminatorAspect.class.getName(), CLASS_LOADER));
        scanner.addExcludeFilter(new AssignableTypeFilter(DiscriminatorAspect.class));
        scanner.addExcludeFilter(new AssignableTypeFilter(DiscriminatorImplementation.class));
        scanner.addExcludeFilter(new AssignableTypeFilter(DiscriminatorInitializer.class));

        return scanner.findCandidateComponents("*.*");
    }

    private Boolean getIsResultAggregated(final BeanDefinition beanDefinition) {
        return getAnnotationAttribute(beanDefinition, Discriminated.class, DiscriminatorInterface.IS_RESULT_AGGREGATED);
    }

    private Boolean getAnnotationAttribute(final BeanDefinition beanDefinition,
                                           final Class clazz,
                                           final String attribute) {
        AnnotationMetadata annotationMetadata = ((ScannedGenericBeanDefinition) beanDefinition).getMetadata();
        if (annotationMetadata != null) {
            Map<String, Object> annotationAttributes = annotationMetadata.getAnnotationAttributes(clazz.getName());
            if (annotationAttributes != null) {
                return (Boolean) annotationAttributes.get(attribute);
            }
        }
        return null;
    }

    private boolean isBeanAvailable(final BeanDefinition beanDefinition) {
        return getBeanName(beanDefinition) != null;
    }

    private String getBeanName(final BeanDefinition beanDefinition) {
        try {
            String[] names = getApplicationContext().getBeanNamesForType(
                    Class.forName(beanDefinition.getBeanClassName()));
            if (names != null && names.length == 1) {
                return names[0];
            }
            String beanName = getBeanNameFromBeanDefinition(beanDefinition);
            if (getApplicationContext().containsBean(beanName)) {
                return beanName;
            }
        } catch (ClassNotFoundException e) {
            return null;
        }
        return null;
    }

    private String getBeanNameFromBeanDefinition(final BeanDefinition beanDefinition) {
        String beanClassName = beanDefinition.getBeanClassName();
        return Introspector.decapitalize(beanClassName.substring(beanClassName.lastIndexOf(".") + 1));
    }

    ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
