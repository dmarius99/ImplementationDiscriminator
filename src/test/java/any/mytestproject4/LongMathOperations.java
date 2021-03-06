package any.mytestproject4;

import com.dms.Discriminated;

/**
 * Created by Marius Dinu (marius.dinu@gmail.com) on 23/12/14.
 */
@Discriminated(isResultAggregated = true)
public class LongMathOperations implements MathOperations<Long> {

    public LongMathOperations() {
    }

    @Override
    public Long plus(Long number1, Long number2) {
        return number1+number2;
    }
}
