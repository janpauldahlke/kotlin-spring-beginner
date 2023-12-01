package com.kotlinspring.controller

import com.kotlinspring.service.GreetingService
import com.ninjasquad.springmockk.MockkBean
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.reactive.server.WebTestClient
import io.mockk.every

//use WebMVC
@WebMvcTest(controllers = [GreetingController::class])
@AutoConfigureWebTestClient
class GreetingControllerUnitTest {

    @Autowired
    lateinit var webTestClient : WebTestClient

    @MockkBean
    lateinit var greetServiceMock : GreetingService

    @Test
    fun retrieveGreeting() {

        val name = "HagbardCeline"
        val defaultMessage = "Hello from test profile"

        every {
            greetServiceMock.retrieveGreeting((any()))
        } returns "Hello $name, $defaultMessage"

        val result = webTestClient.get()
            .uri("/v1/greet/{name}", name)
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBody(String::class.java)
            .returnResult()

           Assertions.assertEquals("Hello $name, $defaultMessage", result.responseBody)
    }
}