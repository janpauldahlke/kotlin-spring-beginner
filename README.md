### setting up Course Catalog Service

* visit [spring initilizar](https://start.spring.io/)
* use these settings ![img.png](docs/img.png)
* this is how to run graddle from cli `./gradlew clean build`

#### create a simple route
* create a class
  * annotate it with 
  * `@RestController`
  * `@RequestMapping`
  * create your method and annotate it with your http verb like
  * `GetMapping("/{name}")` and then map the pathvariable to the arguemtn of the function like so
    * `(@PathVariable("name") name: String)`
      * whole example 
        ```Kotlin
          @RestController
          @RequestMapping("/v1/greet")
          class GreetingController {

          @GetMapping("/{name}")
          fun retrieveGreeting(@PathVariable("name") name: String): String {
            return """
            Hello $name
            """.trimIndent()
            }
          }
      ```