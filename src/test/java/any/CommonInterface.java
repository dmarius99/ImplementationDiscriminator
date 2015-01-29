package any;

import java.util.List;
import java.util.Set;

/**
 * Created by Marius on 27/09/14.
 */
public interface CommonInterface {

    public void show(WrappedParam param1);

    public String getText1(WrappedParam param1, Integer p2);

    public String getText2(Integer p1);

    public List<String> getList(WrappedParam param1);

    public Set<Integer> getListWithNoParam();

}
