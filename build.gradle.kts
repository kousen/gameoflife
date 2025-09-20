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
    classDirectories.setFrom(
        sourceSets.main.get().output.asFileTree.matching {
            exclude("com/example/gameoflife/Main.class")
        }
    )
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
                "com.example.gameoflife.Main",     // Demo CLI excluded
                "com.example.gameoflife.GameRules"  // Lambda utilities
            )
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.70".toBigDecimal()
            }
        }
    }
    classDirectories.setFrom(
        sourceSets.main.get().output.asFileTree.matching {
            exclude("com/example/gameoflife/Main.class")
        }
    )
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
    group = "application"
    description = "Run Game of Life with Blinker pattern"
    mainClass.set("com.example.gameoflife.Main")
    classpath = sourceSets["main"].runtimeClasspath
    systemProperty("pattern", "BLINKER")
}

tasks.register<JavaExec>("runGlider") {
    group = "application"
    description = "Run Game of Life with Glider pattern"
    mainClass.set("com.example.gameoflife.Main")
    classpath = sourceSets["main"].runtimeClasspath
    systemProperty("pattern", "GLIDER")
}

sonar {
    properties {
        property("sonar.projectKey", "kousen_gameoflife")
        property("sonar.organization", "kousen-it-inc")

        // Exclusions - Main.java is a demo CLI with intentional println usage for teaching
        property("sonar.exclusions", "**/Main.java")
        property("sonar.coverage.exclusions", "**/Main.java")

        // Coverage reporting
        property("sonar.coverage.jacoco.xmlReportPaths", "build/reports/jacoco/test/jacocoTestReport.xml")
    }
}