package com.apulbere.lagos.validator

import groovy.transform.TupleConstructor
import spock.lang.Specification

import java.time.LocalDate

class ValidatorSpec extends Specification {

    def "it should display error msg for every given null field"() {
        given:
            def validator = new Validator(
        { Excess e -> e }, [
                    new ValueValidator("value is null", { Excess e -> e.value }),
                    new ValueValidator("limit is null", { Excess e -> e.limit }),
                ]
            )
        when:
            def validationMsg = validator.validate(new Excess())
        then:
            validationMsg == "value is null\nlimit is null"
    }

    def "it should display error msg for all children"() {
        given:
        def validator = new Validator(
                { Excess e -> e }, [
                    new ValueValidator("value is null", { Excess e -> e.value }),
                ], [
                    new Validator({ Excess e -> e.limit }, [
                        new ValueValidator("date is null", { Limit l -> l.date }),
                        new ValueValidator("coordinate is null", { Limit l -> l.coordinate }),
                    ], [
                        new Validator(
                                { Limit l -> l.coordinate }, [
                                        new ValueValidator("name is null", { Coordinate c -> c.name })
                                ]
                        )
                    ])
                ]
        )
        when:
            def validationMsg = validator.validate(new Excess(limit: new Limit(coordinate: new Coordinate())))
        then:
            validationMsg == "value is null\n\tdate is null\n\tname is null"

    }

    @TupleConstructor
    class Excess {
        BigDecimal value
        Limit limit
    }

    @TupleConstructor
    class Limit {
        LocalDate date
        Coordinate coordinate
    }

    @TupleConstructor
    class Coordinate {
        String name
    }
}
