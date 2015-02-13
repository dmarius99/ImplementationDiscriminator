package com.dms;

import java.util.Map;

/**
 * Created by Marius Dinu (marius.dinu@gmail.com) on 04/01/15.
 */
interface BeanManager {

    /**
     *
     * @param <InterfaceType> the interface of the implementations
     * @return a map of implementations
     */
    <InterfaceType> Map<String, InterfaceType> getImplementationBeans();

    /**
     *
     * @return true if methods that return Collection should be aggregated/combined, false otherwise.
     */
    boolean isResultAggregated();

}
