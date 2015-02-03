package com.dms;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Test;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DiscriminatorAspectTest {

    @Test(expected = NullPointerException.class)
    public void testInterceptThrowsNullPointerException() throws Throwable {
        DiscriminatorAspect discriminatorAspect = new DiscriminatorAspect();
        discriminatorAspect.intercept(null);
    }

    @Test
    public void testInterceptThrowsException() throws Throwable {
        final DiscriminatorInterface discriminatorInterface = mock(DiscriminatorInterface.class);
        DiscriminatorAspect discriminatorAspect = new DiscriminatorAspect() {
            @Override
            DiscriminatorInterface getDiscriminatorInterface() {
                return discriminatorInterface;
            }
        };
        ProceedingJoinPoint joinPoint = mock(MethodInvocationProceedingJoinPoint.class);
        discriminatorAspect.intercept(joinPoint);
        verify(discriminatorInterface).defaultIntercept(joinPoint);
    }
}