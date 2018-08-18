package com.apulbere.lagos.validator;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.joining;

public class Validator<S, T> {
    private Function<S, T> source;
    private List<ValueValidator<T>> values;
    private List<Validator<T, ?>> children = emptyList();

    public Validator(Function<S, T> source, List<ValueValidator<T>> values) {
        this.source = source;
        this.values = values;
    }

    public Validator(Function<S, T> source, List<ValueValidator<T>> values, List<Validator<T, ?>> children) {
        this.source = source;
        this.values = values;
        this.children = children;
    }

    public String validate(S object) {
        T value = source.apply(object);
        if(value != null) {
            return validateValue(value).append(validateChildren(value)).toString();
        }
        return "";
    }

    private StringBuilder validateValue(T value) {
        return values.stream().map(v -> v.validate(value))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(collectingAndThen(joining("\n"), StringBuilder::new));
    }

    private String validateChildren(T value) {
        return children.stream()
                .map(c -> c.validate(value))
                .collect(collectingAndThen(joining("\n"), this::finishChildrenMsg));
    }

    private String finishChildrenMsg(String msg) {
        return msg.isEmpty() ? msg : "\n\t" + msg;
    }
}