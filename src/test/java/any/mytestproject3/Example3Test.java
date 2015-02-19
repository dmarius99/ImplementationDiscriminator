package any.mytestproject3;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import javax.inject.Named;
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
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/context3.xml"})
public class Example3Test {

    @Inject
    @Named(value = "integerMathOperations")
    private MathOperations mathOperations;

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
