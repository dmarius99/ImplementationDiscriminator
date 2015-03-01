package com.dms;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.AbstractApplicationContext;

import java.beans.Introspector;

/**
 * Created by Marius Dinu (marius.dinu@gmail.com) on 18/01/15.
 */
@Configuration
@EnableAspectJAutoProxy
public class DiscriminatorConfiguration<DiscriminatorType, InterfaceType> {

    /**
     * Spring ApplicationContext to be created in non Spring contexts.
     */
    private static AbstractApplicationContext applicationContext;
    /**
     * The class implementing DiscriminatorInterface.
     */
    private static Class<? extends DiscriminatorInterface> discriminatorClass;
    /**
     * Default implementation class.
     */
    private static Class defaultImplementation;
    /**
     * Other implementation class beside defaultImplementation.
     * Should be at least one class.
     */
    private static Class[] implementations;

    private static Class discriminatorType;
    private static Class interfaceType;

    /**
     * Activates the @Discriminator for several implementations.
     *
     * @param myDiscriminatorClass the discriminator class
     * @param myDefaultImplementation the default implementation
     * @param myImplementations the other implementations
     * @return the default implementation bean
     */
    public static Object discriminateUsing(final Class<? extends DiscriminatorInterface> myDiscriminatorClass,
                                           final Class myDefaultImplementation, final Class... myImplementations) {
        discriminatorClass = myDiscriminatorClass;
        defaultImplementation = myDefaultImplementation;
        implementations = myImplementations;
        MethodsHelper.LOG.info("Initializing Spring context.");
        applicationContext = new AnnotationConfigApplicationContext();
        initBeans();

        applicationContext.refresh();

        MethodsHelper.LOG.info("Spring context initialized.");
        String beanName = Introspector.decapitalize(defaultImplementation.getSimpleName());
        return applicationContext.getBean(beanName);
    }

    public Object discriminateDefault(final Class myDiscriminatorType,
                                      final Class myInterfaceType,
                                      final Class myDefaultImplementation,
                                      final Class... myImplementations) {
        this.discriminatorType = myDiscriminatorType;
        this.interfaceType = myInterfaceType;

        DiscriminatorImplementation<DiscriminatorType, InterfaceType> discriminatorImplementation = new DiscriminatorImplementation<DiscriminatorType, InterfaceType>(
                discriminatorType,
                interfaceType
        );
        return discriminateUsing(discriminatorImplementation.getClass(), myDefaultImplementation, myImplementations);
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
        initSpringWithAspectJ();
        initImplementations();
        initBeanManager();
        initDiscriminatorInterface();
        initDiscriminatorAspect();
    }

    private static void initSpringWithAspectJ() {
        registerMyBean(DiscriminatorConfiguration.class,
                Introspector.decapitalize(DiscriminatorConfiguration.class.getSimpleName()));
    }


    private static void initImplementations() {
        //default implementation
        registerMyBean(defaultImplementation, Introspector.decapitalize(defaultImplementation.getSimpleName()));
        //other implementations
        for (Class implementationClass:implementations) {
            String beanName = Introspector.decapitalize(implementationClass.getSimpleName());
            registerMyBean(implementationClass, beanName);
        }
        MethodsHelper.LOG.info("Found " + (implementations.length + 1) + " implementations for "
                + discriminatorClass.getName());
    }

    private static void initBeanManager() {
        registerMyBean(BeanManagerForSpring.class, Introspector.decapitalize(BeanManager.class.getSimpleName()));
    }

    private static void initDiscriminatorInterface() {
        BeanDefinition myBeanDefinition = getMyBeanDefinition(discriminatorClass);
        myBeanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(0, discriminatorType);
        myBeanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(1, interfaceType);
        registerMyBean(myBeanDefinition, Introspector.decapitalize(DiscriminatorInterface.class.getSimpleName()));
    }

    private static void initDiscriminatorAspect() {
        BeanDefinition myBeanDefinition = getMyBeanDefinition(DiscriminatorAspect.class);
        registerMyBean(myBeanDefinition, Introspector.decapitalize(DiscriminatorAspect.class.getSimpleName()));
    }

    private static void registerMyBean(final Class beanClass, final String beanName) {
        BeanDefinition beanDefinition = getMyBeanDefinition(beanClass);
        registerMyBean(beanDefinition, beanName);
    }

    private static BeanDefinition getMyBeanDefinition(final Class beanClass) {
        BeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClassName(beanClass.getName());
        return beanDefinition;
    }

    private static void registerMyBean(final BeanDefinition beanDefinition, final String beanName) {
        ((AnnotationConfigApplicationContext)applicationContext).registerBeanDefinition(beanName, beanDefinition);
    }

}