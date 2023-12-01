package com.kotlinspring.dto

import jakarta.validation.constraints.NotBlank

data class CourseDTO(
    val id: Int?,
    // we use the validator bean here to ensure, that name is never empty when we query into the db
    @get:NotBlank(message= "CourseDTO.name must not be blank")
    val name: String,
    @get:NotBlank(message= "CourseDTO.category must not be blank")
    val category: String,
)