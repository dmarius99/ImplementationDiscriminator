package com.dms;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.beans.Introspector;
import java.util.Map;

/**
 * Created by Marius Dinu (marius.dinu@gmail.com) on 18/01/15.
 */
@Configuration
@EnableAspectJAutoProxy
public class DiscriminatorConfiguration implements ApplicationContextAware {

    private static AbstractApplicationContext applicationContext;

    private static Class<? extends DiscriminatorInterface> discriminatorClass;

    public static Object activateDiscriminator(Class<? extends DiscriminatorInterface> myDiscriminatorClass,
                                             Class defaultImplementation) {
        MethodsHelper.LOG.info("Initializing Spring context.");
        discriminatorClass = myDiscriminatorClass;
        applicationContext = new AnnotationConfigApplicationContext();
        initBeans();

        applicationContext.refresh();

        MethodsHelper.LOG.info("Spring context initialized.");
        String beanName = Introspector.decapitalize(defaultImplementation.getSimpleName());
        return applicationContext.getBean(beanName);
    }

    /**
     * Initialization order of beans:
     *
     * 1. SpringConfiguration
     * 2. implementations of the interfaceClass
     * 3. BeanManager
     * 4. DiscriminatorInterface
     * 5. DiscriminatorAspect
     *
     */
    private static void initBeans() {
        registerMyBean(DiscriminatorConfiguration.class, Introspector.decapitalize(DiscriminatorConfiguration.class.getSimpleName()));

        initImplementations();
        initBeanManager();
        initDiscriminatorInterface();
        initDiscriminatorAspect();
    }


    private static void initImplementations() {
        DiscriminatorInterface discriminatorInterface = (DiscriminatorInterface) GenericBeanManager.createInstance(discriminatorClass);
        Class interfaceClass = ((DiscriminatorInitializer) discriminatorInterface).getInterfaceClass();
        BeanManager beanManager = new GenericBeanManager(interfaceClass);
        Map<String,Object> implementationBeans = beanManager.getImplementationBeans();
        for (Object implementationBean:implementationBeans.values()) {
            Class implClass = implementationBean.getClass();
            String beanName = Introspector.decapitalize(implClass.getSimpleName());
            registerMyBean(implClass, beanName);
        }
    }

    private static void initBeanManager() {
        BeanDefinition myBeanDefinition = getMyBeanDefinition(GenericBeanManager.class);
        ConstructorArgumentValues constructor = myBeanDefinition.getConstructorArgumentValues();

        DiscriminatorInterface discriminatorInterface = (DiscriminatorInterface) GenericBeanManager.createInstance(discriminatorClass);
        Class interfaceClass = ((DiscriminatorInitializer) discriminatorInterface).getInterfaceClass();

        constructor.addIndexedArgumentValue(0, interfaceClass);
        registerMyBean(myBeanDefinition, Introspector.decapitalize(BeanManager.class.getSimpleName()));
    }

    private static void initDiscriminatorInterface() {
        registerMyBean(discriminatorClass, Introspector.decapitalize(DiscriminatorInterface.class.getSimpleName()));
    }

    private static void initDiscriminatorAspect() {
        BeanDefinition myBeanDefinition = getMyBeanDefinition(DiscriminatorAspect.class);
        registerMyBean(myBeanDefinition, Introspector.decapitalize(DiscriminatorAspect.class.getSimpleName()));
    }

    private static void registerMyBean(Class beanClass, String beanName) {
        BeanDefinition beanDefinition = getMyBeanDefinition(beanClass);
        registerMyBean(beanDefinition, beanName);
    }

    private static BeanDefinition getMyBeanDefinition(Class beanClass) {
        BeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClassName(beanClass.getName());
        return beanDefinition;
    }

    private static void registerMyBean(BeanDefinition beanDefinition, String beanName) {
        ((AnnotationConfigApplicationContext)applicationContext).registerBeanDefinition(beanName, beanDefinition);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (applicationContext instanceof GenericApplicationContext) {
            DiscriminatorConfiguration.applicationContext = (GenericApplicationContext) applicationContext;
        } else {
            if (applicationContext instanceof AnnotationConfigApplicationContext) {
                DiscriminatorConfiguration.applicationContext = (AnnotationConfigApplicationContext) applicationContext;
            } else {
                throw new RuntimeException("Different ApplicationContext expected!");
            }
        }
    }
}