package com.apulbere.lagos.pipedstream;

import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

class ZipStream {

    static <A, B, C> Stream<C> zip(Stream<A> streamA, Stream<B> streamB, BiFunction<A, B, C> zipper) {
        var iteratorA = streamA.iterator();
        var iteratorB = streamB.iterator();
        var iteratorC = new Iterator<C>() {

            @Override
            public boolean hasNext() {
                return iteratorA.hasNext() && iteratorB.hasNext();
            }

            @Override
            public C next() {
                return zipper.apply(iteratorA.next(), iteratorB.next());
            }
        };
        boolean isParallel = streamA.isParallel() || streamB.isParallel();
        return iteratorToFiniteStream(iteratorC, isParallel);
    }

    private static <T> Stream<T> iteratorToFiniteStream(Iterator<T> iterator, boolean isParallel) {
        Iterable<T> iterable = () -> iterator;
        return StreamSupport.stream(iterable.spliterator(), isParallel);
    }

    static class Tuple<A, B> {
        A left;
        B right;

        Tuple(A left, B right) {
            this.left = left;
            this.right = right;
        }
    }
}
