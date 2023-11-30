package com.kotlinspring.repository

import com.kotlinspring.entitiy.Course
import org.springframework.data.repository.CrudRepository

interface CourseRepository: CrudRepository<Course, Int> {

    //in mem database h2

}