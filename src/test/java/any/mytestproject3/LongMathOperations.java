package any.mytestproject3;

import com.dms.Discriminator;

import javax.inject.Named;

/**
 * Created by Marius Dinu (marius.dinu@gmail.com) on 23/12/14.
 */
@Named
@Discriminator(isResultAggregated = true)
public class LongMathOperations implements MathOperations<Long> {

    public LongMathOperations() {
    }

    @Override
    public Long plus(Long number1, Long number2) {
        return number1+number2;
    }
}
