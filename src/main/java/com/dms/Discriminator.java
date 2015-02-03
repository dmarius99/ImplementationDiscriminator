package com.dms;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Discriminator, 17.10.2014
 *
 * Identifies implementations that can be managed dynamically using a discriminator parameter.
 *
 * Copyright marius.dinu@gmail.com. All rights reserved.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Discriminator {

    boolean isDefault() default false;

    boolean isResultAggregated() default false;

}
