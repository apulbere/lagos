package com.apulbere.lagos.collector

import groovy.transform.EqualsAndHashCode
import groovy.transform.TupleConstructor
import spock.lang.Specification

class GroupCollectorSpec extends Specification {

    @TupleConstructor
    @EqualsAndHashCode
    class Customer {
        String name
        Long id
        Long parentId
    }

    @TupleConstructor
    @EqualsAndHashCode
    class Group {
        Customer customer
        List<Customer> children = new ArrayList<>()
    }

    def "it should create groups with its children"() {
        given:
            def c1 = new Customer("c1", 3L, 1L)
            def c2 = new Customer("c2", 6L, 1L)
            def g1 = new Customer("g1", 1L, null)
            def g2 = new Customer("g2", 2L, null)
        when:
            def groups = new GroupCollector(
                    { new Group() },
                    { Customer c -> c.getId() },
                    { Customer c -> c.getParentId() },
                    { Group g, Customer c -> g.setCustomer(c) },
                    { Group g -> g.getChildren() }
            ).group([c1, g1, c2, g2])
        then:
            groups == [
                new Group(g1, [c1, c2]),
                new Group(g2, [])
            ]
    }
}
