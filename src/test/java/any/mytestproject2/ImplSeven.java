package any.mytestproject2;

import any.CommonInterface;
import any.WrappedParam;
import com.dms.Discriminated;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ImplSix, 13.02.2015
 *
 * Copyright (c) 2014 1&1 Internet AG. All rights reserved.
 *
 * @author mdinu
 * @version $Id$
 */
@Named
@Discriminated
public class ImplSeven implements CommonInterface {

    @Override
    public void show(WrappedParam param1) {

    }

    @Override
    public String getText1(WrappedParam param1, Integer p2) {
        return null;
    }

    @Override
    public String getText2(Integer p1) {
        return null;
    }

    @Override
    public List<String> getList(WrappedParam param1) {
        return new ArrayList<String>();
    }

    @Override
    public Set<Integer> getListWithNoParam() {
        return new HashSet<>();
    }
}
