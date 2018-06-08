package com.apulbere.lagos.collector;

import com.apulbere.lagos.model.Customer;
import com.apulbere.lagos.model.Group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;

import static java.util.stream.Collectors.collectingAndThen;

public class GroupCollector {

    public static List<Group> group(List<Customer> customers) {
        Collector<Customer, Map<Long, Group>, Map<Long, Group>> mapSupplier = Collector.of(
                HashMap::new,
                GroupCollector::addToMap,
                (m1, m2) -> { throw new UnsupportedOperationException(); }
        );
        return customers.stream().collect(collectingAndThen(collectingAndThen(mapSupplier, Map::values), ArrayList::new));
    }

    private static void addToMap(Map<Long, Group> result, Customer customer) {
        if (customer.getParentId() == null) {
            result.computeIfAbsent(customer.getId(), id -> new Group()).setName(customer.getName());
        } else {
            result.computeIfAbsent(customer.getParentId(), parentId -> new Group()).getCustomerIds().add(customer.getId());
        }
    }
}
