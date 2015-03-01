package any.mytestproject4;

import com.dms.Discriminator;

/**
 * IntegerMathOperations, 04.02.2015
 *
 * Copyright (c) 2014 1&1 Internet AG. All rights reserved.
 *
 * @author mdinu
 * @version $Id$
 */
@Discriminator
public class IntegerMathOperations implements MathOperations<Integer> {

    @Override
    public Integer plus(Integer number1, Integer number2) {
        return number1+number2;
    }
}
