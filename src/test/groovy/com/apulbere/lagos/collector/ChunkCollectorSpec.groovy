package com.apulbere.lagos.collector

import spock.lang.Specification

class ChunkCollectorSpec extends Specification {

    def "it should divide a list in equal size lists"() {
        given:
            def numbers = 1..101
        when:
            def chunks = numbers.stream().collect(ChunkCollector.toChunks(10))
        then:
            chunks.size() == 11
            chunks[0] == 1..10
            chunks[10] == [101]
    }

    def "it doesn't support parallel processing"() {
        when:
            (1..102).parallelStream().collect(ChunkCollector.toChunks(20))
        then:
            thrown(UnsupportedOperationException)
    }
}
