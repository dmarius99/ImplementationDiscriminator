package com.dms;

import any.mytestproject3.DiscriminatorExampleForNumbers;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.Test;
import org.mockito.internal.verification.Times;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by Marius Dinu (marius.dinu@gmail.com) on 25/12/14.
 */
public class DiscriminatorImplementationTest {

    @Test
    public void testDefaultIntercept() throws Throwable {
        DiscriminatorExampleForNumbers discriminatorImplementation = new DiscriminatorExampleForNumbers();
        ProceedingJoinPoint proceedingJoinPoint = mock(ProceedingJoinPoint.class);
        Signature signature = mock(Signature.class);
        BeanManagerForSpring beanManagerForSpring = mock(BeanManagerForSpring.class);
        when(proceedingJoinPoint.getSignature()).thenReturn(signature);
        when(proceedingJoinPoint.getArgs()).thenReturn(new Object[0]);
        discriminatorImplementation.setBeanManager(beanManagerForSpring);
        when(beanManagerForSpring.isResultAggregated()).thenReturn(true);

        Object result1 = discriminatorImplementation.defaultIntercept(proceedingJoinPoint);
        verify(proceedingJoinPoint).proceed();

        Method method = DiscriminatorImplementation.class.getDeclaredMethod(
                DiscriminatorInterface.DISCRIMINATE_EXECUTION_POINT,
                ProceedingJoinPoint.class);
        method.setAccessible(true);
        Object result2 = method.invoke(discriminatorImplementation, proceedingJoinPoint);
        verify(proceedingJoinPoint, new Times(2)).proceed();
        assertEquals(result1, result2);
    }

}
