package com.apulbere.lagos.collector

import com.apulbere.lagos.model.Customer
import com.apulbere.lagos.model.Group
import spock.lang.Specification

class GroupCollectorSpec extends Specification {

    def "it should create groups with its customers"() {
        given:
            def customers = [
                new Customer(3L, "c1", 1L),
                new Customer(1L, "g1", null),
                new Customer(6L, "c2", 1L),
                new Customer(2L, "g2", null)
            ]
        when:
            def groups = GroupCollector.group(customers)
        then:
            groups == [
                new Group("g1", [3L, 6L]),
                new Group("g2", [])
            ]
    }
}
