package com.kotlinspring.repository

import com.kotlinspring.entitiy.Course
import org.springframework.data.repository.CrudRepository

interface CourseRepository: CrudRepository<Course, Int> {

    //in mem database h2
    //jpa custom querries
    //https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html

    fun findByNameContaining(courseName:String): List<Course>

}