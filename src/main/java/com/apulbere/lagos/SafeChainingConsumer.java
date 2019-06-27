package com.apulbere.lagos;

import java.util.function.Function;

public interface SafeChainingConsumer<T> {

    void accept(T obj);

    default <G> SafeChainingConsumer<T> andThen(Function<T, G> function, SafeChainingConsumer<G> safeChainingConsumer) {
        return (T obj) -> {
            accept(obj);
            G g = function.apply(obj);
            if(g != null) {
                safeChainingConsumer.accept(g);
            }
        };
    }
}
