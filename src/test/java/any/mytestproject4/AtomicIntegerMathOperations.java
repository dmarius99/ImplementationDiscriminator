package any.mytestproject4;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Marius Dinu (marius.dinu@gmail.com) on 01/03/15.
 */
public class AtomicIntegerMathOperations implements MathOperations<AtomicInteger>{

    @Override
    public AtomicInteger plus(AtomicInteger number1, AtomicInteger number2) {
        return new AtomicInteger(number1.get()+number2.get());
    }
}
