package any.mytestproject3;

import com.dms.DiscriminatorImplementation;

import javax.inject.Named;

/**
 * Created by Marius Dinu (marius.dinu@gmail.com) on 23/12/14.
 */
@Named
public class DiscriminatorExampleForNumbers extends DiscriminatorImplementation<Number, Comparable> {

    public Comparable getImplementationForDiscriminator(Number parameter) {
        if (parameter.longValue() < Integer.MAX_VALUE) {
            return getImplementations().get(IntMath.class.getName());
        } else {
            return getImplementations().get(FloatMath.class.getName());
        }
    }

}
