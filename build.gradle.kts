plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.sparkjava:spark-core:2.9.4")
    implementation("org.slf4j:slf4j-api:2.0.5")
    implementation("org.slf4j:slf4j-simple:2.0.5")
    implementation("io.github.openfeign:feign-okhttp:12.2")
    implementation("io.github.openfeign:feign-gson:12.2")
    implementation("io.github.openfeign:feign-slf4j:12.2")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "org.example.Main"
    }
}