package com.dms;

import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Marius Dinu (marius.dinu@gmail.com) on 23/12/14.
 */

/**
 *
 * @param <InterfaceType> the class type of the interface implemented by the implementation beans
 */
@Named("beanManager")
class BeanManagerForSpring<InterfaceType> implements BeanManager {

    private static final ClassLoader CLASS_LOADER = BeanManagerForSpring.class.getClassLoader();

    @Inject
    private ApplicationContext applicationContext;

    private boolean resultAggregated = false;

    private final Map<String, InterfaceType> implementations = new HashMap<String, InterfaceType>();

    @PostConstruct
    void initDependencies() {
        Set<BeanDefinition> scannedImplementations = getScannedImplementations();
        for (BeanDefinition bean : scannedImplementations) {
            if (isBeanAvailable(bean)) {
                implementations.put(bean.getBeanClassName(), getImplementationBean(bean));
                if (!resultAggregated) {
                    resultAggregated = getIsResultAggregated(bean);
                }
            }
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

    @Override
    @SuppressWarnings({ "unchecked" })
    public <InterfaceType> InterfaceType getTargetObject(InterfaceType proxy) {
        if (AopUtils.isJdkDynamicProxy(proxy)) {
            try {
                return (InterfaceType) ((Advised) proxy).getTargetSource().getTarget();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return proxy;
    }

    Set<BeanDefinition> getScannedImplementations() {
        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false);
        scanner.setResourceLoader(new PathMatchingResourcePatternResolver(CLASS_LOADER));
        scanner.addIncludeFilter(new AnnotationTypeFilter(Discriminator.class));
        scanner.addExcludeFilter(new AspectJTypeFilter(DiscriminatorAspect.class.getName(), CLASS_LOADER));
        scanner.addExcludeFilter(new AssignableTypeFilter(DiscriminatorAspect.class));
        scanner.addExcludeFilter(new AssignableTypeFilter(DiscriminatorImplementation.class));
        scanner.addExcludeFilter(new AssignableTypeFilter(DiscriminatorInitializer.class));

        return scanner.findCandidateComponents("*.*");
    }

    private InterfaceType getImplementationBean(BeanDefinition beanDefinition) {
        String beanName = getBeanName(beanDefinition);
        InterfaceType beanInterfaceType = (InterfaceType) getApplicationContext().getBean(beanName);
        return getTargetObject(beanInterfaceType);
    }

    private Boolean getIsResultAggregated(BeanDefinition beanDefinition) {
        return getAnnotationAttribute(beanDefinition, Discriminator.class, DiscriminatorInterface.IS_RESULT_AGGREGATED);
    }

    private Boolean getAnnotationAttribute(BeanDefinition beanDefinition, Class clazz, String attribute) {
        AnnotationMetadata annotationMetadata = ((ScannedGenericBeanDefinition) beanDefinition).getMetadata();
        if (annotationMetadata != null) {
            Map<String, Object> annotationAttributes = annotationMetadata.getAnnotationAttributes(clazz.getName());
            if (annotationAttributes != null) {
                return (Boolean) annotationAttributes.get(attribute);
            }
        }
        return false;
    }

    private boolean isBeanAvailable(BeanDefinition beanDefinition) {
        return getBeanName(beanDefinition) != null;
    }

    private String getBeanName(BeanDefinition beanDefinition) {
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

    private String getBeanNameFromBeanDefinition(BeanDefinition beanDefinition) {
        String beanClassName = beanDefinition.getBeanClassName();
        return Introspector.decapitalize(beanClassName.substring(beanClassName.lastIndexOf(".") + 1));
    }

    ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
