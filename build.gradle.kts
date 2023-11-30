import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
//we need this to configure test the old way
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

plugins {
	id("org.springframework.boot") version "3.2.0"
	id("io.spring.dependency-management") version "1.1.4"
	kotlin("jvm") version "1.9.20"
	kotlin("plugin.spring") version "1.9.20"
	kotlin("plugin.jpa") version "1.9.20"
}

group = "com.kotlinspring"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	//logging
	implementation("io.github.microutils:kotlin-logging-jvm:2.0.11")

	runtimeOnly("com.h2database:h2")
	//runtimeOnly("org.postgresql:postgresql")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-starter-webflux")

	//mocks in tests
	testImplementation("io.mockk:mockk:1.10.4")
	testImplementation("com.ninja-squad:springmockk:3.0.1")

}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
//confiuring tests
sourceSets {
	test {
		//deprecated
		//before 7.1 // also make sure to have the import from above
		//import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
		withConvention(KotlinSourceSet::class) {
			kotlin.setSrcDirs(listOf("src/test/int", "src/test/unit"))
		}

		//java {
		//	setSrcDirs(listOf("src/test/int", "src/test/unit"))
		//}

		/*sourceSets {
			val test by getting {
				java.srcDir("src/test/int")
				java.srcDir("src/test/unit")
			}
		}*/

	}
}