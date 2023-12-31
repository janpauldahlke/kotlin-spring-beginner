package com.kotlinspring.controller

import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.service.CourseService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

//we need all of this annotations
@RestController // so it is rest
@RequestMapping("/v1/courses") // so it know its route
class CourseController(val courseService: CourseService) {

    @PostMapping  // the http verb
    @Valid
    @ResponseStatus(HttpStatus.CREATED) // type of response status
    // @RequestBody annotation tells Spring to deserialize the JSON request body into a CourseDTO object.
    fun addCourse(@Valid @RequestBody courseDTO: CourseDTO) : CourseDTO{
        return courseService.addCourse(courseDTO)
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAllCourses() : List<CourseDTO>{
        return courseService.getAllCourses()
    }

    //update
    //update will use courseId and look here
    @PutMapping("/{course_id}")
    @Valid
    @ResponseStatus(HttpStatus.OK)
    fun updateCourse(@RequestBody @Valid courseDTO: CourseDTO): CourseDTO {
        return courseService.updateCourse(courseDTO)
    }

    //better delete using @PathVariable
    @DeleteMapping("/{course_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCourse(@PathVariable("course_id") courseId: Int)  {
        return courseService.deleteCourse(courseId)
    }

    //delete by id
    /*@DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    //return Unit here?
    fun deleteCourse(@RequestBody courseDTO: CourseDTO) : Unit {
        //only pass id here already? does it matter in which layer we do it?
        return courseService.deleteCourse(courseDTO)
    }*/
}