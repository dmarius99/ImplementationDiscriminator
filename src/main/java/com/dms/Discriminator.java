package com.dms;

import java.lang.annotation.*;

/**
 * Discriminator, 17.10.2014
 * <p/>
 * Identifies implementations that can be managed dynamically using a discriminator parameter.
 * <p/>
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
