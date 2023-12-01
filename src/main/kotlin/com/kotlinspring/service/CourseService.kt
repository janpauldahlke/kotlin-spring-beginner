package com.kotlinspring.service

import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.entitiy.Course
import com.kotlinspring.repository.CourseRepository
import mu.KLogging
import org.springframework.stereotype.Service
import java.util.NoSuchElementException

//provide this annotation
@Service
class CourseService(val courseRepository: CourseRepository) {

    companion object: KLogging()
    fun addCourse(courseDTO: CourseDTO) :CourseDTO {
        //we need convert DTO into Entity
        // for this we use scopedFunction let
        // it looks ez right? seems to be a pattern
        val courseEntity = courseDTO.let{
            Course(
                null, // we gain this from the db
                it.name,
                it.category
            )
        }
        //the actual save is here
        courseRepository.save(courseEntity)
        logger.info("Saved Course is: $courseEntity")

        //the same for the way back
        return courseEntity.let {
            CourseDTO(it.id, it.name, it.category)
        }
    }

    fun getAllCourses() : List<CourseDTO>{
        return courseRepository.findAll()
            .map{ // map into dto like this
                CourseDTO(it.id, it.name, it.category)
            }
    }

    fun updateCourse(courseDTO: CourseDTO): CourseDTO {

        //ensuring not null
        val courseId = courseDTO.id
            ?: throw IllegalArgumentException("Course ID must not be null for update operation")

        //ensure a course is returned
        //since the courseRepository.findById(courseId) is mutable, one does not need a reassign here
        //check on POST, how to to gain the id miraculously
        val course = courseRepository
            .findById(courseId)
            .orElseThrow {NoSuchElementException("course with ${courseDTO.id} not found")}

        val updatedCourse = course.copy(
            name = courseDTO.name,
            category =  courseDTO.category
        )
        courseRepository.save(updatedCourse)

        return updatedCourse.let {
            CourseDTO(
                it.id, it.name, it.category
            )
        }
    }

    fun deleteCourse(courseId: Int) {
        // check if id exist before pls

        val existingCourse = courseRepository.findById(courseId)

        if(existingCourse.isPresent) {
            existingCourse.get().let {
                courseRepository.deleteById(courseId)
            }
        } else {
            throw Exception("No course found for : $courseId")
        }

        return courseRepository.deleteById(courseId)
    }

    /*fun deleteCourse(courseDTO: CourseDTO) {
        val toBeDeleted = courseDTO.let {
            Course(
                it.id, it.name, it.category
            )
        }
        courseRepository.delete(toBeDeleted)
    }*/
}