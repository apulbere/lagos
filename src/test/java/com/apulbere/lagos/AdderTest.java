package com.apulbere.lagos;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class AdderTest {

    @Test
    void addIntegers() {
        int sum = Adder.sumValues(List.of(4, 6, 8, 2), Integer::sum);
        assertEquals(20, sum);
    }

    @Test
    void emptyList() {
        Integer sum = Adder.sumValues(List.of(), Integer::sum);
        assertNull(sum);
    }

    @Test
    void emptyListWithZero() {
        BigDecimal sum = Adder.sumValues(List.of(), BigDecimal.ZERO, BigDecimal::add);
        assertEquals(BigDecimal.ZERO, sum);
    }
}
