package com.dms;

import java.util.Map;

/**
 * Created by Marius Dinu (marius.dinu@gmail.com) on 19/12/14.
 */
/**
 *
 * @param <DiscriminatorType> the class type of the parameter used in choosing the implementation
 * @param <InterfaceType> the class type of the interface implemented by the implementation beans
 */
interface Discriminate<DiscriminatorType, InterfaceType> {

    /**
     * @param parameter the object value from the intercepted method
     * @return one of the implementation objects
     */
    InterfaceType getImplementationForDiscriminator(DiscriminatorType parameter);

    /**
     * @return a map with all implementations of InterfaceType
     */
    Map<String, InterfaceType> getImplementations();
}
