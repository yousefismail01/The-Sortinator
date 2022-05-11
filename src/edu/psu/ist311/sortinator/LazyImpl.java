package edu.psu.ist311.sortinator;

import java.util.*;

public class LazyImpl<E> implements ISortinator<E> {

    private final List<E> contents = new ArrayList<>();
    private final Comparator<E> order;
    private boolean isAccepting;

    public LazyImpl(Comparator<E> order) {
        this.order = order;
        this.isAccepting = true;
    }
    @Override
    public String toString() {
        return contents.toString();
    }
    @Override
    public void add(E e) {
        if(!isAccepting) {throw new IllegalArgumentException("not in an accepting state");}
        contents.add(e);
    }

    @Override
    public void switchState() {
        this.isAccepting = !isAccepting;
    }

    @Override
    public E removeSmallest() {
        E smallestThusFar = contents.get(0);

        for (int i = 0; i < contents.size() ; i++) {        //0(n)
            if(order.compare(contents.get(i), smallestThusFar) < 0) {
                smallestThusFar = contents.get(i);
            }
        }
        contents.remove(smallestThusFar);
        return smallestThusFar;
    }

    @Override
    public int elementCount() {
        return contents.size();
    }

    @Override
    public boolean acceptingElements() {
        return isAccepting;
    }

    @Override
    public Comparator<E> orderCmp() {
        return order;
    }

    @Override
    public void clear() {
     isAccepting = true;
     contents.clear();
    }

    @Override
    public Iterator<E> iterator() {
        return contents.iterator();
    }
}
