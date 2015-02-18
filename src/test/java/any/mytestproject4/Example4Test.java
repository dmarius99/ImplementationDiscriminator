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
public class Example4Test {

    private MathOperations mathOperations;

    @Before
    public void init() {
        mathOperations = (MathOperations)DiscriminatorConfiguration.activateDiscriminator(
                DiscriminatorExampleForNumbers.class,
                IntegerMathOperations.class);
    }

    @Test(expected = RuntimeException.class)
    public void testIntercepted() {
        Number plus = mathOperations.plus(new AtomicInteger(Integer.MAX_VALUE), new AtomicInteger(Integer.MAX_VALUE));
    }

    @Test
    public void testInterceptedOk() {
        Number plus = mathOperations.plus(new Long(1L + Integer.MAX_VALUE), new Long(1L + Integer.MAX_VALUE));
        assertTrue(plus instanceof Long);
        assertEquals(plus, new Long(2L + 2L * Integer.MAX_VALUE));
    }

}
