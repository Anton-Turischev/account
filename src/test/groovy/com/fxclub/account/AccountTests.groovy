package com.fxclub.account

import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.hamcrest.Matchers.is
import static org.hamcrest.Matchers.notNullValue
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@ActiveProfiles(profiles = "test")
@AutoConfigureMockMvc
class AccountTests extends Specification {

    @Autowired
    MockMvc mockMvc

    def "Account could be created with method"() {
        expect:
        mockMvc.perform(post("/api/v1/account/create")
                .contentType(APPLICATION_JSON_VALUE)
                .content()
                .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.id', notNullValue()))
    }

    def "Account balance could be obtained with info method"() {
        when:
        def createAccountResponseString = mockMvc.perform(post("/api/v1/account/create")
                .contentType(APPLICATION_JSON_VALUE)
                .content()
                .accept(APPLICATION_JSON_VALUE))
                .andReturn().response.contentAsString
        def createAccountResponse = new JsonSlurper().parseText(createAccountResponseString)
        def accountId = createAccountResponse.id
        then:
        mockMvc.perform(post("/api/v1/account/info")
                .contentType(APPLICATION_JSON_VALUE)
                .content("""{"id": $accountId}""".toString())
                .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.id', is(accountId)))
                .andExpect(jsonPath('$.balance', is(0)))
    }

    def "Account could be replenished with deposit method"() {
        when:
        def createAccountResponseString = mockMvc.perform(post("/api/v1/account/create")
                .contentType(APPLICATION_JSON_VALUE)
                .content()
                .accept(APPLICATION_JSON_VALUE))
                .andReturn().response.contentAsString
        def createAccountResponse = new JsonSlurper().parseText(createAccountResponseString)
        def accountId = createAccountResponse.id
        then:
        mockMvc.perform(post("/api/v1/account/info")
                .contentType(APPLICATION_JSON_VALUE)
                .content("""{"id": $accountId}""".toString())
                .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.balance', is(0)))
        expect: "when replenish is processed"
        mockMvc.perform(post("/api/v1/account/deposit")
                .contentType(APPLICATION_JSON_VALUE)
                .content("""{"id": $accountId, "amount": "100.0"}""".toString())
                .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
        and: "balance is changed"
        mockMvc.perform(post("/api/v1/account/info")
                .contentType(APPLICATION_JSON_VALUE)
                .content("""{"id": $accountId}""".toString())
                .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.balance', is(100.0d)))
    }

    def "Account could be withdrawn with withdraw method"() {
        setup:
        def createAccountResponseString = mockMvc.perform(post("/api/v1/account/create")
                .contentType(APPLICATION_JSON_VALUE)
                .content()
                .accept(APPLICATION_JSON_VALUE))
                .andReturn().response.contentAsString
        def createAccountResponse = new JsonSlurper().parseText(createAccountResponseString)
        def accountId = createAccountResponse.id
        expect: "when replenish is processed"
        mockMvc.perform(post("/api/v1/account/deposit")
                .contentType(APPLICATION_JSON_VALUE)
                .content("""{"id": $accountId, "amount": "100.0"}""".toString())
                .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
        and: "balance is changed"
        mockMvc.perform(post("/api/v1/account/info")
                .contentType(APPLICATION_JSON_VALUE)
                .content("""{"id": $accountId}""".toString())
                .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.balance', is(100.0d)))
        and: "when withdrawal is processed"
        mockMvc.perform(post("/api/v1/account/withdraw")
                .contentType(APPLICATION_JSON_VALUE)
                .content("""{"id": $accountId, "amount": "100.0"}""".toString())
                .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
        and: "balance is changed"
        mockMvc.perform(post("/api/v1/account/info")
                .contentType(APPLICATION_JSON_VALUE)
                .content("""{"id": $accountId}""".toString())
                .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.balance', is(0)))
    }

}
