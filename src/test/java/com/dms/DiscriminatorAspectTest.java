package com.dms;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DiscriminatorAspectTest {

    private DiscriminatorAspect discriminatorAspect = new DiscriminatorAspect();

    @Test
    public void testIntercept() throws Throwable {
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        DiscriminatorInterface discriminatorInterface = mock(DiscriminatorInterface.class);
        discriminatorAspect.setDiscriminatorInterface(discriminatorInterface);
        discriminatorAspect.intercept(joinPoint);
        verify(discriminatorInterface).defaultIntercept(joinPoint);
    }

    @Test
    public void testSetAndGetDiscriminatorInterface() throws Exception {
        DiscriminatorInterface discriminatorInterface = mock(DiscriminatorInterface.class);
        discriminatorAspect.setDiscriminatorInterface(discriminatorInterface);
        DiscriminatorInterface discriminatorInterface1 = discriminatorAspect.getDiscriminatorInterface();
        assertEquals(discriminatorInterface, discriminatorInterface1);
    }

}