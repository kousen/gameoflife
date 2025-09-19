plugins {
    java
    application
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