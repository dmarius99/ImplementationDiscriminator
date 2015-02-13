package com.dms;

import java.util.Set;

/**
 * MethodsHelperOverrideCapabilities, 13.02.2015
 *
 * Copyright (c) 2014 1&1 Internet AG. All rights reserved.
 *
 * @author mdinu
 * @version $Id$
 */

public interface MethodsHelperOverrideCapabilities {

    Set<String> getAggregatedMethods();

    Set<String> getUnInterceptedMethods();

    Set<String> getInterceptedMethods();
}
