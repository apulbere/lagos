package com.apulbere.lagos.collector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static java.util.stream.Collectors.collectingAndThen;

public class GroupCollector<G, M, I> {
    private Supplier<G> groupSupplier;
    private Function<M, I> identityFunction;
    private Function<M, I> groupIdentityFunction;
    private BiConsumer<G, M> groupModelBiConsumer;
    private Function<G, List<M>> childrenFunction;

    public GroupCollector(Supplier<G> groupSupplier,
                          Function<M, I> identityFunction,
                          Function<M, I> groupIdentityFunction,
                          BiConsumer<G, M> groupModelBiConsumer,
                          Function<G, List<M>> childrenFunction) {
        this.groupSupplier = groupSupplier;
        this.identityFunction = identityFunction;
        this.groupIdentityFunction = groupIdentityFunction;
        this.groupModelBiConsumer = groupModelBiConsumer;
        this.childrenFunction = childrenFunction;
    }

    public List<G> group(List<M> models) {
        Collector<M, Map<I, G>, Map<I, G>> mapSupplier = Collector.of(
                HashMap::new,
                this::addToMap,
                (m1, m2) -> { throw new UnsupportedOperationException(); }
        );
        return models.stream().collect(collectingAndThen(collectingAndThen(mapSupplier, Map::values), ArrayList::new));
    }

    private void addToMap(Map<I, G> result, M model) {
        I parentId = groupIdentityFunction.apply(model);
        if (parentId == null) {
            groupModelBiConsumer.accept(result.computeIfAbsent(identityFunction.apply(model), key -> groupSupplier.get()), model);
        } else {
            childrenFunction.apply(result.computeIfAbsent(parentId, key -> groupSupplier.get())).add(model);
        }
    }
}
