package com.kotlinspring.controller

import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.entitiy.Course
import com.kotlinspring.repository.CourseRepository
import mu.KLogging
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.ParameterizedTypeReference
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBodyList

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class CourseControllerIntTest {
    companion object: KLogging()
    @Autowired
    lateinit var webTestClient: WebTestClient
    @Autowired
    lateinit var courseRepository: CourseRepository

    //prep exampe dat
    val listOfCouses = listOf(
        CourseDTO(null, "My Yoga Course", "WELLNESS"),
        CourseDTO(null, "The Big JUNIT5 Course", "TESTING"),
        CourseDTO(null, "Another FOO Course Name", "ANOTHER"),
    ).let {
         it.map {
            Course(it.id, it.name, it.category)
        }
    }
    //use before Each
    @BeforeEach
    fun setUp() {
        courseRepository.deleteAll()
        val courses = listOfCouses
        courseRepository.saveAll(courses)
    }

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


    // do not do it like this, since we are posting and populating our database,
    // look the at the before each, how to add fake data to the repository itself not using the webinterface
    //@Test
    /*fun getAllCourse() {

        //we need to post some, before we can get them with h2 in mem db, so here we go
        // one should not mix the db with test data and allow tests to write to it so this is my bad practice, i guess

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


    }*/

    @Test
    fun refactorGetAllCourse() {

        val result = webTestClient.get()
            .uri("/v1/courses")
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBodyList(CourseDTO::class.java) // much better
            //.expectBody(object : ParameterizedTypeReference<List<CourseDTO>>() {}) //interesting hack when java does not suppert List
            .returnResult()
            .responseBody

        Assertions.assertTrue{
            result is List<CourseDTO>
        }

        Assertions.assertTrue {
            result!!.size >= 3
            result!![0].id == 1
        }
    }


    @Test
    fun updatedCourse() {
        val updatedCourse = CourseDTO(
            1,
            "My power YOGA course",
            "SPORT"
        )

        val result = webTestClient
            .put()
            .uri("/v1/courses/${updatedCourse.id}")
            .bodyValue(updatedCourse)
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectStatus().isAccepted
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertTrue {
            result!!.name.contains("power")
        }

    }
}