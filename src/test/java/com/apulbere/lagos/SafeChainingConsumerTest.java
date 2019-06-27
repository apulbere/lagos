package com.apulbere.lagos;

import static junit.framework.TestCase.assertEquals;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;

class SafeChainingConsumerTest {

    @Test
    void allIdsAreSetToNull() {
        var clc = new ClassC(3L, "clc");
        var cld = new ClassD(4L, "cld");
        var clb = new ClassB(2L, "clb", cld);
        var cla = new ClassA(1L, "cla", clb, List.of(clc));

        SafeChainingConsumer<ClassB> safeB = b -> b.setId(null);
        safeB = safeB.andThen(ClassB::getClassD, d -> d.setId(null));

        SafeChainingConsumer<ClassA> safeA = a -> a.setId(null);
        safeA.andThen(ClassA::getClassB, safeB)
             .andThen(ClassA::getClassCList, cList -> cList.forEach(c -> c.setId(null)))
             .accept(cla);

        var clcExpected = new ClassC(null, "clc");
        var cldExpected = new ClassD(null, "cld");
        var clbExpected = new ClassB(null, "clb", cldExpected);
        var claExpected = new ClassA(null, "cla", clbExpected, List.of(clcExpected));

        assertEquals(claExpected, cla);
    }

    @Test
    void noNullPointerExceptionIsThrown() {
        var cla = new ClassA(3L, "cla", null, null);

        SafeChainingConsumer<ClassB> safeB = b -> b.setId(null);
        safeB = safeB.andThen(ClassB::getClassD, d -> d.setId(null));

        SafeChainingConsumer<ClassA> safeA = a -> a.setId(null);
        safeA.andThen(ClassA::getClassB, safeB)
                .andThen(ClassA::getClassCList, cList -> cList.forEach(c -> c.setId(null)))
                .accept(cla);

        var expectedCla = new ClassA(null, "cla", null, null);

        assertEquals(expectedCla, cla);
    }

    @Data
    @AllArgsConstructor
    public class ClassA {
        Long id;
        String name;
        ClassB classB;
        List<ClassC> classCList;
    }

    @Data
    @AllArgsConstructor
    class ClassB {
        Long id;
        String name;
        ClassD classD;
    }

    @Data
    @AllArgsConstructor
    class ClassC {
        Long id;
        String name;
    }

    @Data
    @AllArgsConstructor
    class ClassD {
        Long id;
        String name;
    }

}
