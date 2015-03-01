package any.mytestproject2;

import any.CommonInterface;
import any.WrappedParam;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Marius on 27/09/14.
 */
@Named
public class ImplFive implements CommonInterface {

    public static final String MSG = "Show message from ImplFive... 111";

    public void show(WrappedParam param1) {
        System.out.println(MSG);
    }

    public String getText1(WrappedParam param1, Integer p2) {
        System.out.println(MSG);
        return MSG;
    }

    public String getText2(Integer p1) {
        System.out.println(MSG);
        return MSG;
    }

    public List<String> getList(WrappedParam param1) {
        System.out.println(MSG);
        List<String> myList = new ArrayList<String>();
        myList.add(MSG + "-1");
        myList.add(MSG + "-11");
        return myList;
    }

    public Set<Integer> getListWithNoParam() {
        System.out.println(MSG);
        Set<Integer> mySet = new HashSet<Integer>();
        mySet.add(1);
        mySet.add(2);
        return mySet;
    }
}
