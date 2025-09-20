// Dependency verification is enabled via gradle/verification-metadata.xml
// To update: ./gradlew --write-verification-metadata sha256 build
// Note: SonarQube plugin brings in transitive JUnit 5.x dependencies

plugins {
    java
    application
    jacoco
    id("org.sonarqube") version "6.3.1.5724"
}

group = "com.example"
version = "1.0.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:6.0.0-RC3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
    finalizedBy(tasks.jacocoTestReport)
}

jacoco {
    toolVersion = "0.8.13"
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
    }
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                minimum = "0.70".toBigDecimal()
            }
        }
        rule {
            element = "CLASS"
            excludes = listOf(
                "com.example.gameoflife.Main",
                "com.example.gameoflife.GameRules"
            )
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.70".toBigDecimal()
            }
        }
    }
}

tasks.check {
    dependsOn(tasks.jacocoTestCoverageVerification)
}

application {
    mainClass.set("com.example.gameoflife.Main")
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}

// Custom tasks for running specific patterns
tasks.register<JavaExec>("runBlinker") {
    mainClass.set("com.example.gameoflife.Main")
    classpath = sourceSets["main"].runtimeClasspath
    systemProperty("pattern", "BLINKER")
}

tasks.register<JavaExec>("runGlider") {
    mainClass.set("com.example.gameoflife.Main")
    classpath = sourceSets["main"].runtimeClasspath
    systemProperty("pattern", "GLIDER")
}

sonar {
    properties {
        property("sonar.projectKey", "kousen_gameoflife")
        property("sonar.organization", "kousen-it-inc")
    }
}