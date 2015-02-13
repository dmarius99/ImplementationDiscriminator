package any.mytestproject3;

import com.dms.Discriminator;

import javax.inject.Named;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Marius Dinu (marius.dinu@gmail.com) on 23/12/14.
 */
@Named
@Discriminator(isResultAggregated = true)
public class FloatMath extends AtomicLong implements Comparable<FloatMath> {

    public FloatMath(long initialValue) {
        super(initialValue);
    }

    public FloatMath() {
    }

    @Override
    public int compareTo(FloatMath that) {
        if (that == null) {
            return -1;
        }
        if (this.longValue() == that.longValue()) {
            return 0;
        } else {
            if (this.longValue() > that.longValue()) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}
