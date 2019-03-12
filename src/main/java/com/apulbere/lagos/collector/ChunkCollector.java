package com.apulbere.lagos.collector;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;

public class ChunkCollector {

    public static <T> Collector<T, List<List<T>>, List<List<T>>> toChunks(int size) {
        return Collector.of(ArrayList::new, (list, value) -> {
            List<T> chunk = list.isEmpty() ? null : list.get(list.size() - 1);
            if (chunk == null || chunk.size() == size) {
                chunk = new ArrayList<>(size);
                list.add(chunk);
            }
            chunk.add(value);
        }, (list1, list2) -> {
            throw new UnsupportedOperationException();
        });
    }
}
