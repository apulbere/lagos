package com.apulbere.lagos.validator;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.joining;

public class Checker<S, T> {
    Function<S, T> source;
    List<CheckerValue<T>> values;
    List<Checker<T, ?>> children = emptyList();

    public Checker(Function<S, T> source, List<CheckerValue<T>> values, List<Checker<T, ?>> children) {
        this.source = source;
        this.values = values;
        this.children = children;
    }

    public Checker(Function<S, T> source, List<CheckerValue<T>> values) {
        this.source = source;
        this.values = values;
    }

    public String validate(S object) {
        T value = source.apply(object);
        if(value != null) {
            String valueString = values.stream().map(v -> v.validate(value)).filter(Optional::isPresent).map(Optional::get).collect(joining("\n"));
            valueString += "\n\t";
            valueString += children.stream().map(c -> c.validate(value)).collect(joining("\n"));
            return valueString;
        }
        return "";
    }
}