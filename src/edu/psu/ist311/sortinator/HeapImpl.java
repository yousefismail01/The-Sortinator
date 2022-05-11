package edu.psu.ist311.sortinator;

import java.util.*;

public class HeapImpl<E> implements ISortinator<E> {

    private final List<E> heap = new ArrayList<>(); // we use an arraylist here specifically: why?
    private final Comparator<E> order;
    private boolean isAccepting = true;

    /**
     * <b>requires:</b> the order comparator imposes a total preordering on the set of possible entries `E`.
     */
    public HeapImpl(Comparator<E> order) {
        this.order = order;
    }

    /**
     * Trickles the element located at index {@code i} 'down' into
     * the correct position within the heap such that the heap rooted at index
     * {@code i} obeys the heap ordering property.
     * <p>
     * Note that this internal helper method mutates {@code this} object's
     * {@code contents} list.
     * <p>
     * This operation runs as a worst case time complexity of <tt>O(log n)</tt>
     * <p>
     * <b>requires:</b> left-tree(i)  is-conformal-heap-for {@code order}
     * <b>and</b> right-tree(i) is-valid-heap-for {@code order}<p>
     * <b>ensures:</b> right-tree(i) is-conformal-heap-for {@code order}
     *      <b>and</b> tree(i) is-valid-heap-for {@code order}
     */
    @Override
    public String toString() {
        return heap.toString();
    }

    private void makeProperHeapUnderIndex(int i) {

        int left = left(i);
        int right = right(i);
        int idxOfSmallest = i;
        
        if(left < heap.size()
                && order.compare(heap.get(left), heap.get(i)) < 0) {
            idxOfSmallest = left;
        }
        if(right < heap.size()
                && order.compare(heap.get(right), heap.get(idxOfSmallest)) < 0) {
            idxOfSmallest = right;
        }
        if(idxOfSmallest != i) {
            Collections.swap(heap, i , idxOfSmallest);
            makeProperHeapUnderIndex(idxOfSmallest);
        }
    }

    private int left(int i) {
        return (2 * i) + 1;
    }

    private int right(int i) {
        return (2 * i) + 2;
    }

    private int parent(int i) {
        return (i - 1) / 2;
    }

    @Override
    public void add(E e) {
        if(!isAccepting) {throw new IllegalArgumentException("not in an accepting state");}
        heap.add(e);
        int pos = heap.size() - 1;
        while(pos > 0 && order.compare(heap.get(pos), heap.get(parent(pos))) < 0) {
            Collections.swap(heap, parent(pos), pos);
            pos = parent(pos);
        }
    }

    @Override
    public void switchState() { this.isAccepting = !isAccepting; }

    @Override
    public E removeSmallest() {
        if(heap.isEmpty()) {
            throw new NoSuchElementException("sortinator is empty.");
        }
        if(isAccepting) {
            throw new IllegalStateException("trying to remove when the machine is only accepting new entries");
        }
        //0(1) < 0(logn) < 0(n) < 0(nlogn) < 0(n^2) < 0(n^5) < 0(2^n) < 0(n!)
        //(for sufficiently large n)
        E smallest = heap.get(0);  //0(1)
        heap.set(0, heap.get(heap.size() - 1)); //0(1)
        heap.remove(heap.size() -1);  //0(1)
        makeProperHeapUnderIndex(0); //0(logn)
        return smallest;
        //0(1)) + 0(1) + 0(1) + 0(logn) = 0(logn)
    }

    @Override
    public int elementCount() {
        return heap.size();
    }

    @Override
    public boolean acceptingElements() {
        return isAccepting;
    }

    @Override
    public Comparator<E> orderCmp() {
        return null;
    }

    @Override
    public void clear() {
        isAccepting = true;
        heap.clear();
    }
    @Override
    public Iterator<E> iterator() {
        return heap.iterator();
    }
}



