package com.dms;

import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * BeanManagerDefault, 11.03.2015
 *
 * Copyright (c) 2014 1&1 Internet AG. All rights reserved.
 *
 * @author mdinu
 * @version $Id$
 */

public class BeanManagerDefault<InterfaceType> implements BeanManager {

    private static final ClassLoader CLASS_LOADER = BeanManagerForSpring.class.getClassLoader();

    /**
     * The Aggregation flag for methods that return Collection.
     */
    private boolean resultAggregated = false;

    /**
     * All implementations with @Discriminated annotation that implements InterfaceType.
     */
    private SortedMap<String, InterfaceType> implementations;

    @SuppressWarnings({ "unchecked" })
    @Override
    public SortedMap<String, InterfaceType> getImplementationBeans() {
        return implementations;
    }

    public void initDefault(Class... implementationClasses) throws IllegalAccessException, InstantiationException {
        implementations = new TreeMap();
        for (Class clazz:implementationClasses) {
            implementations.put(clazz.getName(), (InterfaceType)clazz.newInstance());
        }
        implementations = Collections.unmodifiableSortedMap(implementations);
    }

    @Override
    public boolean isResultAggregated() {
        return resultAggregated;
    }
}
