package com.kotlinspring.service

import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.entitiy.Course
import com.kotlinspring.repository.CourseRepository
import mu.KLogging
import org.springframework.stereotype.Service

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
}