package any.mytestproject3;

import com.dms.DiscriminatorImplementation;

import javax.inject.Named;

/**
 * Created by Marius Dinu (marius.dinu@gmail.com) on 23/12/14.
 */
@Named
public class DiscriminatorExampleForNumbers extends DiscriminatorImplementation<Number, MathOperations> {

    @Override
    public MathOperations getImplementationForDiscriminator(Number parameter) {
        if (parameter.longValue() < Integer.MAX_VALUE) {
            return getImplementations().get(IntegerMathOperations.class.getName());
        } else {
            if (parameter.longValue() > Integer.MAX_VALUE) {
                return getImplementations().get(LongMathOperations.class.getName());
            } else {
                return null;
            }
        }
    }

}
