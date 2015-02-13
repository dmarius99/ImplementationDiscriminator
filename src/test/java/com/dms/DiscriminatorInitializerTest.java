package com.dms;

import any.mytestproject3.DiscriminatorExampleForNumbers;
import any.mytestproject3.LongMathOperations;
import any.mytestproject3.MathOperations;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DiscriminatorInitializerTest {

    private DiscriminatorInitializer discriminatorInitializer;
    private DiscriminatorInitializer mockDiscriminator;
    private BeanManagerForSpring mockBeanManagerForSpring;

    @Before
    public void setUp() throws Exception {
        discriminatorInitializer = new DiscriminatorExampleForNumbers();
        mockDiscriminator = mock(DiscriminatorInitializer.class);
        mockBeanManagerForSpring = mock(BeanManagerForSpring.class);

        MathOperations defaultImpl = new LongMathOperations();
        Map<String, MathOperations> implementations = new HashMap<String, MathOperations>();
        implementations.put(LongMathOperations.class.getName(), defaultImpl);
        discriminatorInitializer.setBeanManager(mockBeanManagerForSpring);

        when(mockDiscriminator.getBeanManager()).thenReturn(mockBeanManagerForSpring);
        when(mockBeanManagerForSpring.getImplementationBeans()).thenReturn(implementations);
    }

    @Test
    public void testInitDependenciesWithOneImplementationNotAggregated() throws Exception {
        assertEquals(discriminatorInitializer.getInterfaceClass(), MathOperations.class);
        assertEquals(discriminatorInitializer.getDiscriminatorClass(), Number.class);
        assertEquals(discriminatorInitializer.getImplementations().size(), 1);
        assertTrue(discriminatorInitializer.isActive());
        assertFalse(discriminatorInitializer.isResultAggregated());
    }

    @Test
    public void testImplementationClassName() throws Exception {
        String beanClassName = discriminatorInitializer.getImplementations().keySet().toArray()[0].toString();
        assertEquals(LongMathOperations.class.getName(), beanClassName);
    }

    @Test
    public void testInitDependenciesWithOneImplementationAggregated() throws Exception {
        when(mockBeanManagerForSpring.isResultAggregated()).thenReturn(true);
        mockBeanManagerForSpring.initDependencies();
        assertTrue(discriminatorInitializer.isResultAggregated());
    }

    @Test
    public void testValidate() throws Exception {
        discriminatorInitializer.validate();
    }

    @Test(expected = RuntimeException.class)
    public void testValidateThrowsForImplementations() throws Exception {
        discriminatorInitializer.setBeanManager(mockBeanManagerForSpring);
        when(mockBeanManagerForSpring.getImplementationBeans()).thenReturn(new HashMap());
        discriminatorInitializer.validate();
    }

    @Test(expected = RuntimeException.class)
    public void testValidateThrowsForImplementations2() throws Exception {
        discriminatorInitializer.setBeanManager(mockBeanManagerForSpring);
        when(mockBeanManagerForSpring.getImplementationBeans()).thenReturn(null);
        discriminatorInitializer.validate();
    }

    @Test(expected = RuntimeException.class)
    public void testThrowError() throws Exception {
        discriminatorInitializer.throwError(new NullPointerException());
    }

}