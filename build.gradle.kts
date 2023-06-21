

plugins {
    id("java")
    id("application")
    id("eclipse")
}

group = "org.example"
version = "1.0-SNAPSHOT"


repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.2"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    implementation ("com.google.ortools:ortools-java:9.6.2534")
    implementation ("org.apache.commons:commons-csv:1.10.0")
}

tasks.test {
    useJUnitPlatform()
}