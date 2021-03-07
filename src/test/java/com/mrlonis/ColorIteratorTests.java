package com.mrlonis;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.mrlonis.interfaces.Iterator;
import com.mrlonis.utils.Constants;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class ColorIteratorTests {
    @Test
    public void simpleTesting() {
        ColorTable table1 = new ColorTable(3, 6, Constants.QUADRATIC, .49);
        int[] data1 = new int[] { 32960, 4293315, 99011, 296390 };
        for (int datum : data1) {
            table1.increment(new Color(datum));
        }

        Iterator it1 = table1.iterator();
        List<Long> actual1 = new ArrayList<>();
        while (it1.hasNext()) {
            actual1.add(it1.next());
        }

        ColorTable table2 = new ColorTable(3, 6, Constants.QUADRATIC, .49);
        int[] data2 = new int[] { 32960, 4293315, 99011, 296390 };
        for (int datum : data2) {
            table2.increment(new Color(datum));
        }

        Iterator it2 = table2.iterator();
        List<Long> actual2 = new ArrayList<>();
        while (it2.hasNext()) {
            actual2.add(it2.next());
        }

        assertEquals(actual1, actual2);
    }
}
