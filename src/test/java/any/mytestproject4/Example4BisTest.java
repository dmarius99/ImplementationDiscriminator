package any.mytestproject4;

import com.dms.DiscriminatorConfiguration;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Example3Test, 13.02.2015
 *
 * Copyright (c) 2014 1&1 Internet AG. All rights reserved.
 *
 * @author mdinu
 * @version $Id$
 */
public class Example4BisTest {

    private MathOperations mathOperations;

    @Before
    public void init() throws InterruptedException {
        mathOperations = (MathOperations)DiscriminatorConfiguration.discriminateDefault(
                Number.class, MathOperations.class,
                IntegerMathOperations.class, LongMathOperations.class, AtomicIntegerMathOperations.class);
    }

    @Test
    public void testInit() {
        MathOperations m = (MathOperations)DiscriminatorConfiguration.discriminateDefault(
                Number.class, MathOperations.class,
                IntegerMathOperations.class, LongMathOperations.class, AtomicIntegerMathOperations.class);
    }

    @Test
    public void testInterceptedOk() {
        Number plus = mathOperations.plus(new Long(1L + Integer.MAX_VALUE), new Long(1L + Integer.MAX_VALUE));
        assertTrue(plus instanceof Long);
        assertEquals(plus, new Long(2L + 2L * Integer.MAX_VALUE));
    }

    @Test
    public void testInterceptedOk2() {
        Number plus = mathOperations.plus(new Integer(1), new Integer(2));
        assertTrue(plus instanceof Integer);
        assertEquals(plus, new Integer(3));

        plus = mathOperations.plus(new Long(1), new Long(2));
        assertTrue(plus instanceof Long);
        assertEquals(plus, new Long(3));
    }

    @Test
    public void testInterceptedOk3() {
        Number plus = mathOperations.plus(new AtomicInteger(1), new AtomicInteger(2));
        assertTrue(plus instanceof AtomicInteger);
        assertEquals(plus.getClass(), AtomicInteger.class);
        assertEquals(plus.longValue(), 3);

        plus = mathOperations.plus(new Long(1), new Long(2));
        assertTrue(plus instanceof Long);
        assertEquals(plus, new Long(3));

        plus = mathOperations.plus(new Integer(1), new Integer(2));
        assertTrue(plus instanceof Integer);
        assertEquals(plus, new Integer(3));
    }
}
