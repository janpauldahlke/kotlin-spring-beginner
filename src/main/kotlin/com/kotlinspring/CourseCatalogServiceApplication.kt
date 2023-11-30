package com.kotlinspring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CourseCatalogServiceApplication

fun main(args: Array<String>) {
	println("<--- Starting up ---->")
	runApplication<CourseCatalogServiceApplication>(*args)
}