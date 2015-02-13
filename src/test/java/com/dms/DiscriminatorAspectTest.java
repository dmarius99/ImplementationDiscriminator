package com.dms;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.Test;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;

import javax.inject.Named;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DiscriminatorAspectTest {

    @Test(expected = NullPointerException.class)
    public void testInterceptThrowsNullPointerException() throws Throwable {
        DiscriminatorAspect discriminatorAspect = new DiscriminatorAspect(null);
        discriminatorAspect.intercept(null);
    }

    @Test
    public void testInterceptThrowsException() throws Throwable {
        final DiscriminatorInterface discriminatorInterface = mock(DiscriminatorInterface.class);
        DiscriminatorAspect discriminatorAspect = new DiscriminatorAspect(discriminatorInterface);
        ProceedingJoinPoint joinPoint = mock(MethodInvocationProceedingJoinPoint.class);
        discriminatorAspect.intercept(joinPoint);
        verify(discriminatorInterface).defaultIntercept(joinPoint);
    }

    @Test
    public void testGetBeansAnnotatedWithDiscriminator() throws NoSuchMethodException {
        DiscriminatorAspect discriminatorAspect = new DiscriminatorAspect(null);
        discriminatorAspect.getBeansAnnotatedWithDiscriminator();
        Annotation[] annotations = DiscriminatorAspect.class.getAnnotations();
        Class<? extends Annotation> class1 = annotations[0].annotationType();
        Class<? extends Annotation> class2 = annotations[1].annotationType();
        assertTrue(class1.equals(Aspect.class) || class2.equals(Aspect.class));
        assertTrue(class1.equals(Named.class) || class2.equals(Named.class));

        Method method = DiscriminatorAspect.class.getMethod("getBeansAnnotatedWithDiscriminator");
        method.getAnnotations();
        class1 = method.getDeclaredAnnotations()[0].annotationType();
        assertTrue(class1.equals(Pointcut.class));
    }

}
