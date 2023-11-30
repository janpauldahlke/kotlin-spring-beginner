package com.kotlinspring.controller

import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.service.CourseService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

//we need all of this annotations
@RestController // so it is rest
@RequestMapping("/v1/courses") // so it know its route
class CourseController(val courseService: CourseService) {

    @PostMapping  // the http verb
    @ResponseStatus(HttpStatus.CREATED) // type of response status
    // @RequestBody annotation tells Spring to deserialize the JSON request body into a CourseDTO object.
    fun addCourse(@RequestBody courseDTO: CourseDTO) : CourseDTO{
        return courseService.addCourse(courseDTO)
    }

}