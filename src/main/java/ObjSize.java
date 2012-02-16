import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.TreeMap;

import com.twitter.common.objectsize.ObjectSizeCalculator;

public class ObjSize {
    public static void main(String[] args) {
        p(new Object());
        p("");
        p("1");
        p("12");
        p("123");
        p("1234");
        p("12345");
        p("123456");
        p(new byte[0]);
        p(new byte[] { 1 });
        p(new byte[] { 1, 2 });
        p(new ArrayList<Object>(0));
        p(new LinkedList<Object>());
        p(new HashMap<Object, Object>(0));
        p(new TreeMap<Object, Object>());
        p(new LinkedHashMap<Object, Object>(0));
    }

    private static void p(Object o) {
        System.out.printf("%s(%s) : %d%n", o.getClass().getName(), o,
                ObjectSizeCalculator.getObjectSize(o));
    }
}
