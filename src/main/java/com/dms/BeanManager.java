package com.dms;

import java.util.Map;

/**
 * Created by Marius Dinu (marius.dinu@gmail.com) on 04/01/15.
 */
interface BeanManager {

    <InterfaceType> Map<String, InterfaceType> getImplementationBeans();

    <InterfaceType> InterfaceType getDefaultImplementationBean();

    <InterfaceType> InterfaceType getTargetObject(InterfaceType proxy);

    boolean isResultAggregated();

}
