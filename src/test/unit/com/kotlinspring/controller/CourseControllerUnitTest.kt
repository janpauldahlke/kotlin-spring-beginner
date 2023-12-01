package com.kotlinspring.controller

import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.service.CourseService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.reactive.server.WebTestClient

@WebMvcTest(controllers = [CourseController::class])
@AutoConfigureWebTestClient
class CourseControllerUnitTest {

    @Autowired
    lateinit var client : WebTestClient

    @MockkBean
    lateinit var courseServiceMock : CourseService

    @Test
    fun addCourse() {

        val courseDTO = CourseDTO(
            null,
            "Wake your inner child",
            "DEVELOPMENT"
        )

        every {
            courseServiceMock.addCourse(any())
        } returns courseDTO.copy(id=1)

        val savedCourse = client.post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        assertEquals(1, savedCourse?.id)

    }

    @Test
    fun getAllCourse() {


        val courses = listOf<CourseDTO>(
            CourseDTO(1, "MyPowerYoga", "WELLNESS"),
            CourseDTO(2, "Getting very rich fast", "TRENDS"),
            CourseDTO(3, "Being calm all day", "420")
        )

        every {
            courseServiceMock.getAllCourses()
        } returns courses


        val resultList = client
            .get()
            .uri("/v1/courses")
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBodyList(CourseDTO::class.java)
            .returnResult()
            .responseBody


        Assertions.assertTrue {
            resultList!!.size >= 3
        }

    }

    @Test
    fun updateCourse() {
         val updatedCourse = CourseDTO(
            1,
            "myPowerYogaCourse",
            "WELLNESS"
        )
        every {
            courseServiceMock.updateCourse(any())
        } returns updatedCourse
        val afterUpdate = client
            .put()
            .uri("/v1/courses/1")
            .bodyValue(updatedCourse)
            .exchange()
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        assertEquals("myPowerYogaCourse", afterUpdate?.name)

    }

    @Test
    fun deleteCourse() {

        val deleteThisId = 1

        every {
            courseServiceMock.deleteCourse(deleteThisId)
        } just runs // sidenote, just runs :-P

        val result = client
            .delete()
            .uri("v1/courses/1")
            .exchange()
            .expectStatus().isOk
    }
}