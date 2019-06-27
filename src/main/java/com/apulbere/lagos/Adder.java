package com.apulbere.lagos;

import java.util.Collection;
import java.util.function.BinaryOperator;

public interface Adder {

    static <E extends Number> E sumValues(Collection<E> objectsToSum, BinaryOperator<E> sumOp) {
        return objectsToSum.stream().reduce(sumOp).orElse(null);
    }

    static <E extends Number> E sumValues(Collection<E> objectsToSum, E zero, BinaryOperator<E> sumOp) {
        return objectsToSum.stream().reduce(zero, sumOp);
    }
}
