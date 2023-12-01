package com.kotlinspring.repository

import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.entitiy.Course
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
@DataJpaTest // look this up in rtfm
@ActiveProfiles("test")
class CourseRepositoryIntTest {

    @Autowired
    lateinit var repository: CourseRepository

    @BeforeEach
    fun setUp() {
        //data
        val listOfCouses = listOf(
            CourseDTO(null, "My Yoga Course", "WELLNESS"),
            CourseDTO(null, "The Big JUNIT5 Course", "TESTING"),
            CourseDTO(null, "Another FOO Course Name", "ANOTHER"),
        ).let {
            it.map {
                Course(it.id, it.name, it.category)
            }
        }

        repository.deleteAll()
        //persist
        repository.saveAll(listOfCouses)


    }

    @Test
    fun findNameContaining() {
        val course = repository.findByNameContaining("Yoga")
        assertEquals(1, course.size)

        val courses = repository.findByNameContaining("Course")
        assertEquals(3, courses.size)
    }

    @Test
    fun findNameContainingNativeQuery() {
        val course = repository.findCourseByNameAsNativeQuery("Yoga")
        assertEquals(1, course.size)

        val courses = repository.findCourseByNameAsNativeQuery("Course")
        assertEquals(3, courses.size)
    }


}