package any.mytestproject2;

import any.CommonInterface;
import any.WrappedParam;
import com.dms.DiscriminatorImplementation;

import javax.inject.Named;
import java.util.Set;

/**
 * Created by Marius on 27/09/14.
 */
@Named
public class DiscriminatorExampleAggregated extends DiscriminatorImplementation<WrappedParam, CommonInterface> {

    @Override
    public CommonInterface getImplementationForDiscriminator(WrappedParam parameter) {
        if (parameter.getId() < 1000) {
            return getImplementations().get(ImplThree.class.getName());
        } else {
            return getImplementations().get(ImplFour.class.getName());
        }
    }

    @Override
    public Set<String> getInterceptedMethods() {
        Set<String> interceptedMethods = super.getInterceptedMethods();
        interceptedMethods.remove("show");
        interceptedMethods.remove("getText1");
        return interceptedMethods;
    }

    @Override
    public Set<String> getUnInterceptedMethods() {
        Set<String> unInterceptedMethods = super.getUnInterceptedMethods();
        unInterceptedMethods.add("show");
        unInterceptedMethods.add("getText1");
        return unInterceptedMethods;
    }

    @Override
    public Set<String> getAggregatedMethods() {
        return super.getAggregatedMethods();
    }

}
