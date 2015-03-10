package com.dms;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Discriminated, 17.10.2014
 *
 * Identifies implementations that can be managed dynamically using a discriminated parameter.
 *
 * Copyright marius.dinu@gmail.com. All rights reserved.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Discriminated {

    /**
     * @return true if methods that return Collection should be aggregated/combined, false otherwise.
     */
    boolean isResultAggregated() default false;

}
