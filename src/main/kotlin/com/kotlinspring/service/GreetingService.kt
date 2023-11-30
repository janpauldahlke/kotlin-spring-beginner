package com.kotlinspring.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service // we neeed this but why is not clear at this moment
class GreetingService {
    //bind to the application.yml property using @Value
    @Value("\${message}")
    //lateInit
    lateinit var message : String;

    fun retrieveGreeting(name: String) = "Hello $name, $message"
}