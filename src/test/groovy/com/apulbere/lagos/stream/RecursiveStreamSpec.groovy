package com.apulbere.lagos.stream

import spock.lang.Specification
import com.apulbere.lagos.stream.RecursiveStream.Node

class RecursiveStreamSpec extends Specification {

    def recursiveStream = new RecursiveStream()


    def "it find all nodes with given code at all levels"() {
        given:
            def lvl4a1b1c1d1 = new Node(8, "c1")
            def lvl3a1b1c2 = new Node(7, "c2", [])
            def lvl3a1b1c1 = new Node(6, "c1", [lvl4a1b1c1d1])
            def lvl2a1b1 = new Node(5, "b1", [lvl3a1b1c1, lvl3a1b1c2])
            def lvl1a1 = new Node(4, "a1", [lvl2a1b1])
            def lvl1a2 = new Node(3, "a2", [])
            def lvl1a3 = new Node(2, "c1")
            def parent = new Node(1, "p1", [lvl1a1, lvl1a2, lvl1a3])
        when:
            def result = recursiveStream.findAllWithCode([parent], "c1")
        then:
            result.collect({ it.id }).sort() == [ 2, 6, 8 ]
    }
}
