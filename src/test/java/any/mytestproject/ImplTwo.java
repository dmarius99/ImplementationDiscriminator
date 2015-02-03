package any.mytestproject;

import any.CommonInterface;
import any.WrappedParam;
import com.dms.Discriminator;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Marius on 27/09/14.
 */
@Named
@Discriminator
public class ImplTwo implements CommonInterface {

    public static final String MSG = "Show message from ImplTwo... 222";

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
        myList.add(MSG + "-2");
        myList.add(MSG + "-22");
        return myList;
    }

    public Set<Integer> getListWithNoParam() {
        System.out.println(MSG);
        Set mySet = new HashSet<Integer>();
        mySet.add(1001);
        mySet.add(1002);
        return mySet;
    }
}
