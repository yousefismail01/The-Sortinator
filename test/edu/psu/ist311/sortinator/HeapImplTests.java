package edu.psu.ist311.sortinator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

public class HeapImplTests extends AbstractSortinatorTests {
    @Override
    <U> ISortinator<U> createInstance(Comparator<U> order) {
        return new HeapImpl<>(order);
    }
    @Test
    public void testRemoveSmallestFailure() {
        ISortinator<Integer> s = new HeapImpl<>(Integer::compareTo);
        s.add(4);
        s.add(7);
        s.add(0);

        Assertions.assertThrows(Exception.class, s::removeSmallest);
        Assertions.assertEquals("[0, 7, 4]", s.toString());
    }
}
