package edu.psu.ist311.sortinator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

public abstract class AbstractSortinatorTests {

    abstract <U> ISortinator<U> createInstance(Comparator<U> order);

    /**
     * If jUnit tells you "No tests were found" it just means you haven't
     * provided an implementation of this abstract sortinator factory method:
     * {@link #createInstance(Comparator)}
     * <p>
     * So create a class that extends {@code AbstractSortinatorTests} and
     * implements this method. Then you should be able to execute the test below.
     * <p>
     * (see README.md for specifics for an example)
     */
    @Test
    public void testRoutine1() {
        ISortinator<Integer> s = createInstance(Integer::compareTo);
        s.add(33);
        s.add(9);
        s.add(43);
        s.add(0);
        s.add(-4);
        s.switchState();

        int[] expected = {-4, 0, 9, 33, 43};
        int currExpectedArrayIdx = 0;

        while (s.elementCount() > 0) {    //0(n)
            Assertions.assertEquals(expected[currExpectedArrayIdx],
                    (int) s.removeSmallest()); //0(logn)
            currExpectedArrayIdx++;
        }
        //while loops run in: 0(n) * 0(logn) = 0(nlogn)
    }
    @Test
    public void testAdd() {
        ISortinator<Integer> s = createInstance(Integer::compareTo);
        s.add(10);
        s.add(24);
        s.add(13);

        Assertions.assertEquals("[10, 24, 13]", s.toString());
    }
    @Test
    public void testAddFailure() {
        ISortinator<Integer> s = createInstance(Integer::compareTo);
        s.switchState();

        Assertions.assertThrows(Exception.class, () -> s.add(8)) ;
    }
    @Test
    public void testSwitchState() {
        ISortinator<Integer> s = createInstance(Integer::compareTo);
        s.add(1);
        s.add(2);
        s.add(45);

        Assertions.assertTrue(s.acceptingElements());
        s.switchState();
        Assertions.assertFalse(s.acceptingElements());
    }
    @Test
    public void testRemoveSmallestFailure() {
        ISortinator<Integer> s = createInstance(Integer::compareTo);

        Assertions.assertThrows(Exception.class, s::removeSmallest);
    }
    @Test
    public void testRemoveSmallest() {
        ISortinator<Integer> s = createInstance(Integer::compareTo);
        s.add(25);
        s.add(50);
        s.add(75);
        s.switchState();

        Assertions.assertEquals(25, s.removeSmallest());
    }
    @Test
    public void testElementCount() {
        ISortinator<Integer> s = createInstance(Integer::compareTo);
        s.add(1);
        s.add(2);
        s.add(3);

        Assertions.assertEquals(3, s.elementCount());
    }
    @Test
    public void testClear() {
        ISortinator<Integer> s = createInstance(Integer::compareTo);
        s.add(1);
        s.add(5);
        s.add(8);
        s.clear();

        Assertions.assertEquals("[]", s.toString());
        Assertions.assertEquals(0, s.elementCount());
        Assertions.assertTrue(s.acceptingElements());
    }

    @Test
    public void testCustomObjectOrdering() {

        Comparator<Horse> horseComparator = new Comparator<Horse>() {
            @Override
            public int compare(Horse o1, Horse o2) {
                if(o1.age < o2.age && o1.weight < o2.weight ) {
                    return -1;
                }
                else if(o1.age > o2.age && o1.weight > o2.weight ) {
                    return 1;
                }
                else {
                    return 0;
                }
            }
        };
        ISortinator<Horse> s = createInstance(horseComparator);
        s.add(new Horse("Black", 8, 590 ));
        s.add(new Horse("Brown", 9, 700));
        s.add(new Horse("White", 7, 600));
        s.switchState();
        Assertions.assertEquals(
                new Horse("Black", 8, 590 )
                , s.removeSmallest());
    }
    public static class Horse {
        public String color;
        public int age;
        public int weight;

        public Horse(String color, int age, int weight) {
            this.color = color;
            this.age = age;
            this.weight = weight;
        }

        @Override
        public int hashCode() {
            return color.hashCode() + Integer.hashCode(age) +
                    Integer.hashCode(weight) * 31;
        }
        @Override
        public boolean equals(Object obj) {
            boolean result = obj instanceof Horse;
            if(result) {
                result = this.color.equals(((Horse) obj).color) &&
                        this.age == ((Horse) obj).age &&
                        this.weight == ((Horse) obj).weight;
            }
            return result;
        }
        @Override
        public String toString() {
            return color + " age:" + age;
        }
    }
}


