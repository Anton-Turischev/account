package com.fxclub.account

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@SpringBootTest
@ActiveProfiles(profiles = "test")
class AccountApplicationTests extends Specification {

    def "Context loads"() {
        expect:
        1 == 1
    }

}
