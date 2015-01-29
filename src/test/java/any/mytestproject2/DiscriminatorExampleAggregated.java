package any.mytestproject2;

import any.CommonInterface;
import any.WrappedParam;
import com.dms.DiscriminatorImplementation;

import javax.inject.Named;

/**
 * Created by Marius on 27/09/14.
 */
@Named
public class DiscriminatorExampleAggregated extends DiscriminatorImplementation<WrappedParam, CommonInterface> {

    @Override
    public CommonInterface getImplementationForDiscriminator(WrappedParam parameter) {
        if (parameter.getId()<1000) {
            return getImplementations().get(ImplThree.class.getName());
        } else {
            return getImplementations().get(ImplFour.class.getName());
        }
    }

}
