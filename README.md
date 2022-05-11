# The Sortinator: 2000

In this assignment you will provide two implementations of a data structure termed the sortinator 2000. The sortinator 2000 is a 'machine' that operates in two states: insertion and extraction. In the insertion state, users insert new entries to the machine via calls to `add(..)`. When `switchMode()` is called, adding entries is no longer possible and the machine is switched into 'extraction' mode, whereby users may retrieve the smallest element by priority via the `removeSmallest()` operation.

In this assignment you will:
* develop a jUnit testing heirarchy that works on multiple implementations of a common sortinator interface,
* practice implementing reusable, generically-typed data structures that operate on the basis of Java's `Comparator` interface,
* analyze big-O performance tradeoffs between differing implementation strategies.

Start **early** on this one (*start now!*)

## Conceptualizing the `ISortinator` Interface

The `ISortinator` interface conceptualizes this structure as a tuple (ordered pair) of a multiset *m* of entries of some type `T` and a boolean *accepting* flag:
> ( m : multiset of T,  accepting : boolean )

Recall that a multiset of `T` is like an ordinary set (i.e.: a collection of unordered entries of type `T`) but one that allows for 
duplicated elements. For example:

> { a, a, c, b, d, a, a }  is a multiset of characters, where the entry `a` has multiplicity 4 (as `a` appears 4 times).

When the sortinator starts up (initializes), the accepting flag is true, and multiset *m* is empty.

New entries of type `T` can be added to the sortinator via calls to `add(..)`. Note that there 
is no guarantee (at the interface level) that calls to `add(..)` will necessarily store the elements in any particular order 
-- *so it is left up to the implementation when/where the actual sorting occurs*.

A call to `switchState()` changes the state of the machine into an extraction state and only then can 'min/smallest' elements be removed (one at a time) via calls to `removeSmallest()`. Where the 'minimum' element is determined by the `Comparator<T>` passed into a given implementation's constructor.

### Interface Operations

An illustration of the conceptual view of this structure and some of its primary operations is given below:

<img src="https://github.com/dtwelch/resources/blob/master/311fa21/asg3/sortinator.png" width="480">

There are many ways to implement this structure: using a heap, a binary search tree (maintained in so-called *pile formation*), or via linear structures like lists, arrays, stacks, or queues. 

In this assignment you will provide two implementations of `ISortinator` (detailed below in parts one and two). Part three of the assignment is unit testing (though it really should be done concurrently with your implementation). Part four has you reflect in writing on the performance tradeoffs made by each implementation you provide.

## Part 1: Providing a 'slow' implementation of `ISortinator` (30pts)

In this implementation, the `ISortinator` should be represented in code using the following fields:
* a **private** field: `List<T> contents` -- to store the entries,
* a **private** field: `boolean isAccepting` -- to track the state the machine is operating in (insertion or extraction).

For when and where to do the sorting of the `contents` list your implementation must adhere to one of two strategies:

### a 'bulk' strategy (call this implementation: `BulkImpl`):

> 1) each time the user adds a new entry, it is added to the end `contents` list (so the list is maintained in unsorted order during insertion state)    
> 2) when switchState is invoked, the `isAccepting` flag is flipped and -- unbeknownst to callers -- a **private** `sort(..)` operation is invoked on the internal `contents` list
> 3) users may now call `removeSmallest` - which removes and returns the front of the now sorted `contents` list

### or a 'lazy' strategy (call this implementation: `LazyImpl`):

> 1) each time the user adds a new entry, it is added to the `contents` list in unsorted order  
> 2) switchState simply flips the `isAccepting` flag  
> 3) users call `removeSmallest` which triggers a *linear search* of the `contents` list for the minimum entry -- which is then removed and returned 

### *In class example: an 'eager' strategy - `EagerImpl`:*

To help everyone get started, and to draw attention to various performance considerations, I'll be implementing a third strategy in class **(note: you can't submit this one for credit)**.

> 1) each time the user adds a new entry, it is added to the `contents` list in the correct position (i.e. `contents` is maintained in sorted order at all times)  
> 2) switchState simply flips the `isAccepting` flag  
> 3) users call `removeSmallest` which just removes the front entry of the `contents` list and returns it to the caller

All 'implementation strategies' (lazy, bulk, and eager) impose different performance ramifications for the `add(..)`, `switchState()`, and `removeSmallest()` operations. 
* Think carefully about the performance tradeoffs each of these approaches make -- as you'll have to communicate the rationale for your choice using big-O notation in part four. 
* Also be careful about which implementation of `List` you use for the `contents` field (i.e.: arraylist or linkedlist) -- it will have a nontrivial impact on the overall runtime of your chosen implementation's methods.
* You **are** allowed to call `contents.sort(..)` though you should only use this if you want a simple way of implementing `BulkImpl`. In lieu of calling this, you might also consider the extra credit opportunity below.

### Extra Credit Opportunity #1 (10pts): implement your own sorting algorithm

*This extra credit opportunity assumes you've chosen to pursue the 'bulk' implementation strategy.* 

As the title suggests, instead of using the `sort` method baked into the collections framework, roll your own! You should write a **private** method to do it. Note that you can decide to implement a *slow sort* like [bubble sort](https://en.wikipedia.org/wiki/Bubble_sort) or a fast sort like [merge sort](https://en.wikipedia.org/wiki/Merge_sort). The choice is yours. 

All accumulated extra points earned on this asg will be added to your midterm exam grade (then your next lowest quiz score if earned points are leftover).

## Part 2: `HeapImpl` - an incremental heap-based implementation (40pts)

Your second implementation of `ISortinator` should use a *heap*. In this implementation, you are to employ an *incremental strategy* that maintains a proper heap in the background. Here's a snippet of the implementation to get you started

```java
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
    private void makeProperHeapUnderIndex(int i) {
        // todo
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
}
```

Some notes:

* each time a new entry is added, it should be 'bubbled up' to the correct position in the heap (you can use the algorithm discussed in class -- consult the pseudocode in the slides),
* each time a smallest entry is removed, the tree must be re-heapified from the root downwards (again, consult the pseudocode here).

So before and after each sortinator operation call, e.g.:
> s.add(4);  
> s.add(0);  
> s.switchState();  
> int x = s.removeSmallest();  

The `heap` field must refer to a conformal/well-formed heap (i.e.: it adheres to the (total) ordering property imposed by the passed `order` comparator, and is a *complete* tree). Put a different way: after the body of any method implementation is done executing, the `heap` field  should never be in an invalid configuration. 

### Hints:

1. You should use [`Collections.swap(..)`](https://docs.oracle.com/javase/7/docs/api/java/util/Collections.html#swap(java.util.List,%20int,%20int)) to perform swaps of any two elements within the `heap` list.
2. Think carefully about the ramifications that the above delineation of work imposes on the runtimes of for the `add`, `removeSmallest`, and `switchState` methods (how does it contrast with other proposed implementations? -- is it better, worse, or just *different??*)
3. Make friends with the debugger when working on this, you should be able to set a breakpoint and observe the values stored inside `heap` to see where/when things might going wrong.

### Extra Credit Opportunity #2 (10pts): reducing duplication via an `AbstractSortinator` class

While working on your second (or potentially third implementation), you might notice a fair bit of duplicated code accumulating between your implementations. To address this, we'll do what the Java collections framework does and add an 'intermediate' abstract class, `AbstractSortinator`, that 'sits between' the top level `ISortinator` interface and various concrete implementations. This abstract layer will hold/encapsulate all common fields and method implementations.

A UML class diagram showing the standard solution is below (left) and the proposed solution that introduces the intermediate abstract class is on the right (in the left pic, duplicated aspects are boxed in pink):

<img src="https://github.com/dtwelch/resources/blob/master/311fa21/asg3/abstract-sortinator.png" width="700">

As the UML suggests through the use of `#` as opposed to `+`, `-`, you should mark the fields present in the abstract sortinator class `protected` such that they can be accessed across all implementations without needing to go through getter methods. Recall the general rules for Java's access modifiers (from [here](https://stackoverflow.com/questions/16164902/what-is-the-default-access-modifier-in-java)):

<img src="https://github.com/dtwelch/resources/blob/master/311fa21/asg3/access-modifiers.png" width="600">

## Part 3: jUnit Testing (15pts)

We'll use an abstract jUnit test class to run a set of common/shared unit tests for each implementation. To make this work, we'll have an abstract jUnit base class for our unit tests called `AbstractSortinatorTests` that looks like:
```java
public abstract class AbstractSortinatorTests {

    abstract <U> ISortinator<U> createInstance(Comparator<U> order);
    
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

        while (s.elementCount() > 0) {
            Assertions.assertEquals(expected[currExpectedArrayIdx],
                    (int) s.removeSmallest());
            currExpectedArrayIdx++;
        }
    }

```

Now you'll need to provide a concrete extension class for each of the implementations you wrote (up to three). Here's one for the eager sorting implementation (`EagerImpl`):

```java
public class EagerImplTests extends AbstractSortinatorTests {
    @Override
    <U> ISortinator<U> createInstance(Comparator<U> order) {
        return new EagerImpl<>(order);
    }
}
```

You should place all general `@Test`s in the `AbstractSortinatorTests` class. If you have any tests that are specific to any one specific implementation, then they should be placed in that implementation's testing subclass (the one for the eager implementation is shown above). This class implements the abstract `createInstance` 'factory method' which instantiates a specific instance of the sortinator. So when the test harness is run, the `createInstance` method is called for each extending class, which effectively runs the `@Test` methods on all subclasses of `AbstractSortinatorTests`. So, when ready, make sure you click **all** when you click the testing button near the line numbers (this instantiate all subclasses and run the tests on each). 

*Note: the `AbstractSortinatorTest` class can only ever call methods defined in the `ISortinator` interface and hence are definitionally black box tests. They can't (or, rather, shouldn't) call anything specific to one particular implementation*.

## Part 4: Big-O Writeup (15pts)

Lastly, provide a short big-O analysis of the runtime of each implementation you turnin, namely for each of the methods:
* `add(..)`
* `switchState()`
* `removeSmallest()`

Don't keep it too simplistic. Try to provide `ISortinator` usage scenarios where it might be advantageous to use one implementation over another (e.g., due to its runtime characteristics on frequently called operations, such as `add`).

## Handin and Grading
When you are ready to submit, commit your work by typing:

> git commit -am "message goes here"

then follow this up with a

> git push origin main

You will be graded on the quality of your runtime analysis, your tests, and the correctness of your implementations and their adherence to the strategies discussed. 
