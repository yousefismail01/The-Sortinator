package edu.psu.ist311.sortinator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

public class LazyImplTests extends AbstractSortinatorTests {
    @Override
    <U> ISortinator<U> createInstance(Comparator<U> order) {
        return new LazyImpl<>(order);
    }

}
