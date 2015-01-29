package com.dms;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.springframework.context.ApplicationContext;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BeanManagerForSpringTest {

    BeanManagerForSpring beanManagerForSpring = new BeanManagerForSpring();

    @Before
    public void setUp() throws Exception {
        ApplicationContext mockApplicationContext = mock(ApplicationContext.class);
        beanManagerForSpring.setApplicationContext(mockApplicationContext);
        when(mockApplicationContext.getBeanNamesForType(Matchers.any(Class.class))).thenReturn(new String[0]);
    }

    @Test
    public void testInitDependencies() throws Exception {
        beanManagerForSpring.initDependencies();
        assertNull(beanManagerForSpring.getDefaultImplementationBean());
        assertEquals(beanManagerForSpring.getImplementationBeans().size(), 0);
    }

    @Test
    public void testGetImplementationBeans() throws Exception {
        beanManagerForSpring.getImplementationBeans().put("1", new Object());
        assertEquals(beanManagerForSpring.getImplementationBeans().size(), 1);
    }

    @Test
    public void testGetDefaultImplementationBean() throws Exception {
        assertNull(beanManagerForSpring.getDefaultImplementationBean());
    }

    @Test
    public void testIsResultAggregated() throws Exception {
        assertFalse(beanManagerForSpring.isResultAggregated());
    }

    @Test
    public void testGetTargetObject() throws Exception {
        Object bean = new Object();
        Object targetObject = beanManagerForSpring.getTargetObject(bean);
        assertEquals(targetObject, bean);
    }
}