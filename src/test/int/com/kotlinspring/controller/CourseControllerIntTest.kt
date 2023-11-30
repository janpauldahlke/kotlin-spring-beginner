package com.kotlinspring.controller

import com.kotlinspring.dto.CourseDTO
import mu.KLogging
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.ParameterizedTypeReference
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class CourseControllerIntTest {

    companion object: KLogging()
    @Autowired
    lateinit var webTestClient: WebTestClient
    @Test
    fun addCourse() {

        val course = CourseDTO(null, "Testing Test Course", "TESTING")

        val savedCourse = webTestClient.post()
            .uri("/v1/courses")
            .bodyValue(course)
            .exchange()
            .expectStatus().isCreated
            .expectBody(CourseDTO::class.java) //note the typing here
            .returnResult()
            .responseBody // do not forget, unwrap

        Assertions.assertTrue{
            savedCourse!!.id != null
        }

    }

    @Test
    fun getAllCourse() {

        //we need to post some, before we can get them with h2 in mem db, so here we go
        // one should not mix the db with test data and allow tests to write to it so this is my bad practice, i guess

        //exampe dat
        val listOfCouses : List<CourseDTO> = listOf(
            CourseDTO(null, "My Yoga Course", "WELLNESS"),
            CourseDTO(null, "The Big JUNIT5 Course", "TESTING"),
            CourseDTO(null, "Another FOO Course Name", "ANOTHER"),
        )

        //basically copy paste top test
        webTestClient.post()
            .uri("/v1/courses")
            .bodyValue(listOfCouses[0])
            .exchange()
            .expectBody(CourseDTO::class.java) //note the typing here
            .returnResult()
            .responseBody // do not forget, unwrap

        Thread.sleep(500) // dont do this

        val result = webTestClient.get()
            .uri("/v1/courses")
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBody(object : ParameterizedTypeReference<List<CourseDTO>>() {}) //interesting hack when java does not suppert List
            .returnResult()
            .responseBody

        Assertions.assertTrue{
            result is List<CourseDTO>
        }

        logger.info("TEST--: $result")

        Assertions.assertTrue {
            result!!.size > 0 //optimistic usage of !!
            result!![0].name.contains("Yoga")
        }


    }
}