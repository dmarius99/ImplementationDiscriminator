package any.mytestproject4;

import com.dms.DiscriminatorImplementation;

/**
 * Created by Marius Dinu (marius.dinu@gmail.com) on 23/12/14.
 */
public class DiscriminatorExampleForNumbers extends DiscriminatorImplementation<Number, MathOperations> {

    @Override
    public MathOperations getImplementationForDiscriminator(Number parameter) {
        if (parameter.longValue() == Integer.MAX_VALUE) {
            return null;
        }
        if (parameter.getClass().equals(Integer.class)) {
            return getImplementations().get(IntegerMathOperations.class.getName());
        } else {
            if (parameter.getClass().equals(Long.class)) {
                return getImplementations().get(LongMathOperations.class.getName());
            }
        }
        return null;
    }

}
