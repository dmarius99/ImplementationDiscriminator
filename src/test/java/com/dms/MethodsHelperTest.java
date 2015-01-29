package com.dms;

import any.CommonInterface;
import any.WrappedParam;
import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.*;

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

}
