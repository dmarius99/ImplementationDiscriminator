package com.dms;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.springframework.aop.SpringProxy;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.Advised;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.Proxy;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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
        beanManagerForSpring.init();
        assertEquals(beanManagerForSpring.getImplementationBeans().size(), 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetImplementationBeans() throws Exception {
        beanManagerForSpring.init();
        assertEquals(beanManagerForSpring.getImplementationBeans().size(), 0);
        beanManagerForSpring.getImplementationBeans().put("1", new Object());
    }

    @Test
    public void testIsResultAggregated() throws Exception {
        assertFalse(beanManagerForSpring.isResultAggregated());
    }

    @Test(expected = RuntimeException.class)
    public void testGetTargetObject() throws Exception {
        Object bean = new Object();
        Object targetObject = beanManagerForSpring.getTargetObject(bean);
    }

    @Test(expected = RuntimeException.class)
    public void testGetTargetObjectNoProxy() throws Exception {
        Object bean = mock(Object.class);
        when(bean.getClass()).thenThrow(new IllegalArgumentException());
        beanManagerForSpring.getTargetObject(bean);
    }

    @Test(expected = RuntimeException.class)
    public void testGetTargetObjectNoProxyAdvised() throws Exception {
        Advised bean = mock(Advised.class);
        assertEquals(beanManagerForSpring.getTargetObject(bean), bean);
    }

    @Test(expected = RuntimeException.class)
    public void testGetTargetObjectSpringProxy() throws Exception {
        SpringProxy bean = mock(SpringProxy.class);
        when(bean.getClass()).thenThrow(new IllegalArgumentException());
        beanManagerForSpring.getTargetObject(bean);
    }

    @Test(expected = RuntimeException.class)
    public void testGetTargetObjectProxy() throws Exception {
        InvocationHandler handler = new MyInvocationHandler();

        Advised bean = (Advised) Proxy.newProxyInstance(Advised.class.getClassLoader(),
                new Class[] { Advised.class },
                handler);
        TargetSource mockTargetSource = mock(TargetSource.class);
        when(bean.getTargetSource()).thenReturn(mockTargetSource);
        when(mockTargetSource.getTarget()).thenThrow(new NullPointerException());

        beanManagerForSpring.getTargetObject(bean);
    }

    @Test
    public void testGetTargetObjectOK() throws Exception {
        Advised bean = mock(Advised.class);
        TargetSource mockTargetSource = mock(TargetSource.class);
        when(bean.getTargetSource()).thenReturn(mockTargetSource);
        when(mockTargetSource.getTarget()).thenReturn(bean);

        Object targetObject = beanManagerForSpring.getTargetObject(bean);
        assertTrue(targetObject instanceof Advised);
        assertEquals(targetObject, bean);
    }

    class MyInvocationHandler  implements InvocationHandler {

        @Override
        public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
            return null;
        }

    }
}