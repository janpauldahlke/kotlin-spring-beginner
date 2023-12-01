package com.kotlinspring.repository

import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.entitiy.Course
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import java.util.stream.Stream

@DataJpaTest // look this up in rtfm
@ActiveProfiles("test")
class CourseRepositoryIntTest {

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

    @Autowired
    lateinit var repository: CourseRepository

    @BeforeEach
    fun setUp() {

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

    //this is good, because it runs the tests in parallel
    @ParameterizedTest
    @MethodSource("courseAndSize")
    fun findCoursesByName_2(name:String, expectedSize: Int) {
        val course = repository.findCourseByNameAsNativeQuery(name)
        assertEquals(expectedSize, course.size)

        val courses = repository.findCourseByNameAsNativeQuery(name)
        assertEquals(expectedSize, courses.size)
    }

    companion object {

        @JvmStatic //In JUnit 5, methods referenced by @MethodSource are expected to be static !!
        fun courseAndSize(): Stream<Arguments> {
            return Stream.of(
                Arguments.arguments("Yoga", 1),
                Arguments.arguments("Course", 3),
            )
        }
    }


}