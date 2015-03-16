package com.dms;

import any.mytestproject3.IntegerMathOperations;
import any.mytestproject3.LongMathOperations;
import any.mytestproject3.MathOperations;
import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;

/**
 * TestWithoutSpringAndAspectJ, 11.03.2015
 *
 * Copyright (c) 2014 1&1 Internet AG. All rights reserved.
 *
 * @author mdinu
 * @version $Id$
 */

public class TestWithoutSpringAndAspectJ {

    @Test
    public void testDefaultImplementation() throws Throwable {
        DiscriminatorImplementation discriminatorImplementation = new DiscriminatorImplementation(
                Number.class,
                MathOperations.class);
        MathOperations implementation1 = new IntegerMathOperations();

        Method method = IntegerMathOperations.class.getMethod("plus", Integer.class, Integer.class);
        Object[] args = {new Integer(1), new Integer(2)};
        Execution execution = new Execution(implementation1, method, args);

        BeanManagerDefault beanManager = new BeanManagerDefault<MathOperations>();
        beanManager.initDefault(IntegerMathOperations.class, LongMathOperations.class);

        discriminatorImplementation.setBeanManager(beanManager);
        Object result = discriminatorImplementation.defaultIntercept(execution);
        assertEquals(result, new Integer(3));

/*        Object[] args2 = {new Long(1), new Long(2)};
        execution = new Execution(implementation1, method, args2);
        result = discriminatorImplementation.defaultIntercept(execution);
        assertEquals(result, new Long(3));*/
    }
}
