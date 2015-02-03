package com.dms;

import any.CommonInterface;
import any.WrappedParam;
import any.mytestproject.ImplTwo;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Marius Dinu (marius.dinu@gmail.com) on 22/01/15.
 */
public class InitializationTest {

    private CommonInterface commonInterface;

    @Test
    public void testInit() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan(DiscriminatorInterface.BASE_PACKAGE, "any.mytestproject");
        context.refresh();
        commonInterface = (CommonInterface) context.getBean("implOne");
        String nameFromImplTwoRunOnImplOne = commonInterface.getText1(new WrappedParam(1001L, "name2"), 2);
        assertTrue(nameFromImplTwoRunOnImplOne.equalsIgnoreCase(ImplTwo.MSG));

        List<Integer> arrlist = new ArrayList<Integer>();

        // use add() method to add elements in the list
        System.out.println("1:" + arrlist.add(1));
        System.out.println("1:" + arrlist.add(2));
        System.out.println("1:" + arrlist.add(3));
        System.out.println("1:" + arrlist.add(1));
    }
}
