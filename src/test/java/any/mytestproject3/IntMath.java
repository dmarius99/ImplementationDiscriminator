package any.mytestproject3;

import com.dms.Discriminator;

import javax.inject.Named;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * IntMath, 04.02.2015
 *
 * Copyright (c) 2014 1&1 Internet AG. All rights reserved.
 *
 * @author mdinu
 * @version $Id$
 */
@Named
@Discriminator
public class IntMath extends AtomicInteger implements Comparable<IntMath> {

    @Override
    public int compareTo(IntMath that) {
        if (that == null) {
            return -1;
        }
        if (this.intValue() == that.intValue()) {
            return 0;
        } else {
            if (this.intValue() > that.intValue()) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}
