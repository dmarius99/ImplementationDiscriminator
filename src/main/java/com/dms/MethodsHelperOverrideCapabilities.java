package com.dms;

import java.util.Set;

/**
 * MethodsHelperOverrideCapabilities, 13.02.2015
 *
 * Created by Marius Dinu (marius.dinu@gmail.com) on 13/02/14.
 *
 */
interface MethodsHelperOverrideCapabilities {

    /**
     * @return a set of aggregated methods
     */
    Set<String> getAggregatedMethods();

    /**
     * @return a set of un-intercepted methods
     */
    Set<String> getUnInterceptedMethods();

    /**
     *
     * @return a set of intercepted methods
     */
    Set<String> getInterceptedMethods();
}
