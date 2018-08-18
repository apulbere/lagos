package com.apulbere.lagos.validator;

import java.util.Optional;
import java.util.function.Function;

public class CheckerValue<T> {
    String validationString;
    Function<T, Object> fun;

    public CheckerValue(String validationString, Function<T, Object> fun) {
        this.validationString = validationString;
        this.fun = fun;
    }

    public Optional<String> validate(T object) {
        return fun.apply(object) != null ? Optional.empty() : Optional.of(validationString);
    }

}
