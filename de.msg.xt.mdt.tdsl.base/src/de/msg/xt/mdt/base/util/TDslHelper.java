package de.msg.xt.mdt.base.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class TDslHelper {

    public static <T> T selectRandom(Iterator<T> iterator) {
        List<T> list = new ArrayList<T>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list.get(new Random(System.currentTimeMillis()).nextInt(list.size()));
    }
}
