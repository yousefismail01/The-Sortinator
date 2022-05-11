package edu.psu.ist311.sortinator;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class EagerImpl<E> implements ISortinator<E> {

    private final List<E> contents = new LinkedList<>();
    private final Comparator<E> order;
    private boolean isAccepting;

    public EagerImpl(Comparator<E> order) {
        this.order = order;
        this.isAccepting = true;
    }

    @Override
    public void add(E e) {

        //takes entry e and inserts it onto its correct position inside
        //contents
        int insertPos = 0;  // O(1)

        for (int i = 0; i < contents.size(); i++) {  //O(n)
            E curr = contents.get(i);  //0(n)

            if (order.compare(e, curr) >= 0) {  //0(1)
                insertPos++;    //0(1)

            }
        }
        //0(n) * 0(n) = 0(n^2)
        // by time loop completes, insertPos will hold the index where e belongs
        contents.add(insertPos, e);  //0(n)
    }

    @Override
    public void switchState() { this.isAccepting = !isAccepting; }

    @Override
    public E removeSmallest() { return contents.remove(0); }

    @Override
    public int elementCount() { return contents.size(); }

    @Override
    public boolean acceptingElements() { return isAccepting; }

    @Override
    public Comparator<E> orderCmp() { return order; }

    @Override
    public void clear() {

    }

    @Override
    public Iterator<E> iterator() { return contents.iterator(); }
}
