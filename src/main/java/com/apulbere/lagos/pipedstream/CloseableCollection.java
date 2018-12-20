package com.apulbere.lagos.pipedstream;

import java.util.Collection;
import java.util.stream.Stream;

class CloseableCollection<T extends AutoCloseable> implements AutoCloseable {
    private Collection<T> collection;

    CloseableCollection(Collection<T> collection) {
        this.collection = collection;
    }

    @Override
    public void close() throws Exception {
        for(T t: collection) {
            t.close();
        }
    }

    Stream<T> stream() {
        return collection.stream();
    }

    Collection<T> getCollection() {
        return collection;
    }
}
