package com.apulbere.lagos.validator;

import java.util.Optional;
import java.util.function.Function;

import static java.util.Optional.empty;

public class ValueValidator<T> {
    private String validationMsg;
    private Function<T, Object> function;

    public ValueValidator(String validationMsg, Function<T, Object> function) {
        this.validationMsg = validationMsg;
        this.function = function;
    }

    public Optional<String> validate(T object) {
        return function.apply(object) != null ? empty() : Optional.of(validationMsg);
    }

}
