package any.mytestproject2;

import any.CommonInterface;
import any.WrappedParam;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Marius Dinu (marius.dinu@gmail.com) on 27/09/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/context2.xml"})
public class Example2Test {

    @Inject
    @Named(value = "implThree")
    private CommonInterface commonInterface;

    @Test
    public void testAggregated() {
        //test aggregated method
        Set<Integer> listWithNoParam1 = commonInterface.getListWithNoParam();
        assertTrue(listWithNoParam1.contains(1));
        assertTrue(listWithNoParam1.contains(2));
        assertTrue(listWithNoParam1.contains(1001));
        assertTrue(listWithNoParam1.contains(1002));
        assertEquals(listWithNoParam1.size(), 4);

        //test method not intercepted
        String text1 = commonInterface.getText2(1);
        assertEquals(text1, ImplThree.MSG);
        String text2 = commonInterface.getText2(10000);
        assertEquals(text2, ImplThree.MSG);


        List<String> list1 = commonInterface.getList(new WrappedParam(1L, "name1"));
        List<String> list2 = commonInterface.getList(new WrappedParam(1001L, "name2"));

        assertEquals(list1.size(), 2);
        assertEquals(list2.size(), 2);
        assertEquals(list1.get(0), ImplThree.MSG + "-1");
        assertEquals(list2.get(0), ImplFour.MSG + "-2");

        commonInterface.show(new WrappedParam(1L, "name1"));
        commonInterface.show(new WrappedParam(1001L, "name2"));
    }

    @Test
    public void testNotInterceptedOverriddenMethods() {
        String name1 = commonInterface.getText1(new WrappedParam(1L, "name aaaa 1"), 1);
        assertEquals(name1, ImplThree.MSG);
        String name2 = commonInterface.getText1(new WrappedParam(1001L, "name2"), 2);
        assertEquals(name2, ImplThree.MSG);
    }
}
