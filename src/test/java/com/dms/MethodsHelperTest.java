package com.dms;

import any.CommonInterface;
import any.WrappedParam;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anySet;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Marius Dinu (marius.dinu@gmail.com) on 27/09/14.
 */
public class MethodsHelperTest {

    @Test
    public void testConstructorWithNoOverride() {
        MethodsHelper methodsHelper = new MethodsHelper(CommonInterface.class, WrappedParam.class);
        assertNotNull(methodsHelper.getInterfaceClass());
        assertNotNull(methodsHelper.getDiscriminatorClass());
        assertEquals(methodsHelper.getAggregatedMethods().size(), 1);
        assertTrue(methodsHelper.getAggregatedMethods().contains("getListWithNoParam"));
        assertEquals(methodsHelper.getUnInterceptedMethods().size(), 1);
        assertTrue(methodsHelper.getUnInterceptedMethods().contains("getText2"));
    }

    @Test
    public void testConstructorWithOverride1() {
        MethodsHelper methodsHelper = new MethodsHelper(CommonInterface.class, WrappedParam.class);
        methodsHelper.setAggregatedMethods(new HashSet<String>());
        methodsHelper.setUnInterceptedMethods(new HashSet<String>());

        assertNotNull(methodsHelper.getInterfaceClass());
        assertNotNull(methodsHelper.getDiscriminatorClass());
        assertEquals(methodsHelper.getAggregatedMethods().size(), 0);
        assertEquals(methodsHelper.getUnInterceptedMethods().size(), 0);
    }

    @Test
    public void testConstructorWithOverride2() {
        MethodsHelper methodsHelper = new MethodsHelper(CommonInterface.class, WrappedParam.class);
        methodsHelper.getAggregatedMethods().add("getList");
        methodsHelper.getUnInterceptedMethods().add("getText1");

        assertNotNull(methodsHelper.getInterfaceClass());
        assertNotNull(methodsHelper.getDiscriminatorClass());
        assertEquals(methodsHelper.getAggregatedMethods().size(), 2);
        assertEquals(methodsHelper.getUnInterceptedMethods().size(), 2);
    }

    @Test(expected = RuntimeException.class)
    public void testWithNullInterface() {
        new MethodsHelper(null, WrappedParam.class);
    }

    @Test(expected = RuntimeException.class)
    public void testWithNullDiscriminator() {
        new MethodsHelper(CommonInterface.class, null);
    }

    @Test(expected = RuntimeException.class)
    public void testWithNullInterfaceAndDiscriminator() {
        new MethodsHelper(null, null);
    }

    @Test
    public void testConstructorWithNoDiscriminator() {
        MethodsHelper methodsHelper = new MethodsHelper(CommonInterface.class, StringBuffer.class);
        assertNotNull(methodsHelper.getInterfaceClass());
        assertNotNull(methodsHelper.getDiscriminatorClass());

        assertEquals(methodsHelper.getAggregatedMethods().size(), 2);
        assertTrue(methodsHelper.getAggregatedMethods().contains("getListWithNoParam"));
        assertTrue(methodsHelper.getAggregatedMethods().contains("getList"));

        assertEquals(methodsHelper.getUnInterceptedMethods().size(), 3);
        assertTrue(methodsHelper.getUnInterceptedMethods().contains("getText1"));
        assertTrue(methodsHelper.getUnInterceptedMethods().contains("getText2"));
        assertTrue(methodsHelper.getUnInterceptedMethods().contains("show"));
    }

    @Test(expected = RuntimeException.class)
    public void testThrowMethodsNotUnique() {
        MethodsHelper methodsHelper = new MethodsHelper();
        methodsHelper.throwMethodsNotUnique("abc");
    }

    @Test
    public void testGetInterceptedMethods() {
        Set<String> mockMethods = new TreeSet<String>();
        MethodsHelper methodsHelper = mock(MethodsHelper.class);
        Set<String> interceptedMethods = methodsHelper.getInterceptedMethods();
        when(methodsHelper.getMethods(anySet(), anySet())).thenReturn(mockMethods);
        assertEquals(interceptedMethods, mockMethods);
    }

    @Test(expected = RuntimeException.class)
    public void testGetMethods() {
        Set<String> mockMethodStrings = null;
        Set<Method> mockMethods = new HashSet<Method>();
        Collections.addAll(mockMethods, ClassWithSameMethodNames.class.getMethods());
        MethodsHelper methodsHelper = new MethodsHelper();
        methodsHelper.getMethods(mockMethodStrings, mockMethods);
    }

    @Test(expected = RuntimeException.class)
    public void testInitMethodsByRoleNotUnique() {
        new MethodsHelper(List.class, Object.class);
    }


    @Test(expected = RuntimeException.class)
    public void testInitMethodsByRoleNotUnique2() {
        new MethodsHelper(List.class, Collection.class);
    }

    @Test(expected = RuntimeException.class)
    public void testInitMethodsByRoleNotUnique3() {
        new MethodsHelper(Set.class, Collection.class);
    }

    @Test
    public void testInitMethodsByRoleUnique() {
        MethodsHelper methodsHelper = new MethodsHelper(Set.class, Object.class);
        assertTrue(methodsHelper.getInterceptedMethods().size() >= 9);
        assertTrue(methodsHelper.getAggregatedMethods().size() >= 0);
        assertTrue(methodsHelper.getUnInterceptedMethods().size() >= 6);
    }

    @Test
    public void testInitMethodsByRole() {
        MethodsHelper methodsHelper = new MethodsHelper();
        methodsHelper.setInterfaceClass(Collection.class);
        methodsHelper.setDiscriminatorClass(Object.class);
        methodsHelper.init();
        assertTrue(methodsHelper.getInterceptedMethods().size() >= 9);
        assertTrue(methodsHelper.getAggregatedMethods().size() >= 0);
        assertTrue(methodsHelper.getUnInterceptedMethods().size() >= 6);
    }

    @Test
    public void testShouldMethodBeIntercepted() {
        MethodsHelper methodsHelper = new MethodsHelper();
        Method method = null;
        assertFalse(methodsHelper.shouldMethodBeIntercepted(method));
    }

}