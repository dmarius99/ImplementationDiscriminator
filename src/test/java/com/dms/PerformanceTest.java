package com.dms;

import any.CommonInterface;
import any.WrappedParam;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.logging.Logger;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Marius Dinu (marius.dinu@gmail.com) on 27/09/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/context2.xml"})
public class PerformanceTest {

    //the amount of calls for the test
    public static final int NO_OF_CALLS = 1000;
    //the discriminator implementation should not add more than 1 millisecond delay to a call
    public static final int PERFORMANCE_LOAD_IN_MILLISECONDS_PER_CALL = 1;


    @Inject
    @Named(value = "implThree")
    private CommonInterface commonInterface;

    private static Logger LOG = Logger.getAnonymousLogger();

    @Inject
    private DiscriminatorInterface discriminatorInterface;

    @Test
    public synchronized void testAddedWorkloadAndPerformance() {
        assertTrue(discriminatorInterface.isActive());
        discriminatorInterface.activate();
        assertTrue(discriminatorInterface.isActive());
        final long startMillis = System.currentTimeMillis();
        for (int i = 0; i < NO_OF_CALLS; i++)
            commonInterface.show(new WrappedParam(i + 1L, "name1"));
        final long activatedAnnotationTime = System.currentTimeMillis() - startMillis;
        LOG.info("Call with activated @Discriminator took " + activatedAnnotationTime + " ms");

        discriminatorInterface.deactivate();
        assertFalse(discriminatorInterface.isActive());
        final long startMillis2 = System.currentTimeMillis();
        for (int i = 0; i < NO_OF_CALLS; i++)
            commonInterface.show(new WrappedParam(i + 1L, "name1"));
        final long deactivatedAnnotationTime = System.currentTimeMillis() - startMillis2;
        LOG.info("Call with deactivated @Discriminator took " + deactivatedAnnotationTime + " ms");

        LOG.info(NO_OF_CALLS + " calls took " + (activatedAnnotationTime - deactivatedAnnotationTime) + " ms ");

        assertTrue((activatedAnnotationTime - deactivatedAnnotationTime) / NO_OF_CALLS < PERFORMANCE_LOAD_IN_MILLISECONDS_PER_CALL);
        //less than one millisecond extra time for a intercepted call

        LOG.info("Additional method time(ms):" + ((float) activatedAnnotationTime - deactivatedAnnotationTime) / NO_OF_CALLS);
        discriminatorInterface.activate();
    }
}
