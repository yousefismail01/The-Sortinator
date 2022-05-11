package edu.psu.ist311.sortinator;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An <i>ISortinator</i> can be conceptualized as a tuple (ordered pair) of a
 * multiset <i>m</i> and a boolean flag <i>accepting</i>:
 * <pre>   ( <i>m</i> : multiset of T,  <i>accepting</i> : boolean )</pre>
 * <p>
 * When the sortinator starts up (initializes), its <i>accepting</i> flag is
 * set to true, and its multiset <i>m</i> is empty.
 * <p>
 * New entries of type {@code T} can be added to the sortinator via calls to
 * {@link #add(Object)}, though the interface itself makes no commitments as to
 * which order the entries will be stored in after being added.
 * <p>
 * Elements may only be removed when the sortinator is operating in the
 * <b>extraction state</b>. A call to {@link #switchState()} can change the
 * machine into the extraction state and only then can 'min/smallest' elements
 * be removed via calls to {@link #removeSmallest()}.
 * <p>
 * Here, 'smallest' entries are determined by a {@link Comparator<T>} passed
 * into a given implementation's constructor.
 *
 * @param <T> the type of elements stored in the sortinator
 */
public interface ISortinator<T> extends Iterable<T> {

    /**
     * Adds an element {@code e} of type {@code T} to {@code this} sortinator.
     * <p>
     * <b>ensures:</b> {@code e} is added to this sortinator's multiset.
     *
     * @throws IllegalArgumentException if {@code e} is added while {@code this}
     *                                  is not in an accepting state.
     */
    void add(T e);

    /**
     * Switches {@code this} sortinator into insertion or extraction state
     * (depending on the current/existing state of {@code this} sortinator
     * object).
     * <p>
     * <b>ensures:</b> {@code e} is added to this sortinator's multiset.
     */
    void switchState();

    /**
     * Removes and returns some 'smallest' T from the contents of {@code this}
     * sortinator's multiset, where 'smallest' is determined by the
     * {@link Comparator<T>} passed into the implementation's constructor.
     * <p>
     * <b>requires:</b> this sortinator's multiset is not empty.<p>
     * <b>ensures:</b> forall t : T,
     * removeSmallest returns the 'smallest' entry present
     * in this sortinator's multiset m (prior to the call to removeSmallest).
     * The accepting flag also doesn't change.
     *
     * @throws NoSuchElementException if the sortinator's multiset is empty
     * @throws IllegalStateException  if the sortinator is not in extraction
     *                                state when called
     */
    T removeSmallest();

    /**
     * Returns the number of elements in {@code this} sortinator.
     * <p>
     * <b>ensures:</b> the number of elements in this sortinator is returned.
     */
    int elementCount();

    /**
     * <b>ensures:</b> {@code true} is returned <b>if and only if</b>
     * {@code this} sortinator is accepting new entries.
     */
    boolean acceptingElements();

    /**
     * <b>ensures:</b> the ordering {@link Comparator<T>} used for {@code this}
     * sortinator instance.
     */
    Comparator<T> orderCmp();

    /**
     * <b>ensures:</b> {@code this} sortinator is reset to its default state
     * (accepting and empty).
     */
    void clear();

    Iterator<T> iterator();
}
