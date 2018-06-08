package com.apulbere.lagos.collector;

import com.apulbere.lagos.model.Model;
import com.apulbere.lagos.model.ModelGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static java.util.stream.Collectors.collectingAndThen;

public class GroupCollector<T> {
    private Supplier<ModelGroup<T>> groupSupplier;

    public GroupCollector(Supplier<ModelGroup<T>> groupSupplier) {
        this.groupSupplier = groupSupplier;
    }

    public List<ModelGroup<T>> group(List<Model<T>> models) {
        Collector<Model, Map<T, ModelGroup<T>>, Map<T, ModelGroup<T>>> mapSupplier = Collector.of(
                HashMap::new,
                this::addToMap,
                (m1, m2) -> { throw new UnsupportedOperationException(); }
        );
        return models.stream().collect(collectingAndThen(collectingAndThen(mapSupplier, Map::values), ArrayList::new));
    }

    private void addToMap(Map<T, ModelGroup<T>> result, Model<T> model) {
        T parentId = model.getParentId();
        if (parentId == null) {
            result.computeIfAbsent(model.getId(), id -> groupSupplier.get()).setValue(model);
        } else {
            result.computeIfAbsent(parentId, key -> groupSupplier.get()).getChildren().add(model);
        }
    }
}
