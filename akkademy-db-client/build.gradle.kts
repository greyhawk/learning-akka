/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Java Library project to get you started.
 * For more details take a look at the Java Libraries chapter in the Gradle
 * user guide available at https://docs.gradle.org/5.0/userguide/java_library_plugin.html
 */

plugins {
    // Apply the java-library plugin to add support for Java Library
    `java-library`
}

repositories {
    // Use jcenter for resolving your dependencies.
    // You can declare any Maven/Ivy/file repository here.
    mavenLocal()
    mavenCentral()
    jcenter()
}

dependencies {
    implementation("com.typesafe.akka:akka-actor_2.12:2.5.18")
    implementation("com.typesafe.akka:akka-remote_2.12:2.5.18")
    implementation("com.google.guava:guava:27.0.1-jre")

    // Use JUnit test framework
    testImplementation("junit:junit:4.12")
    testImplementation("com.typesafe.akka:akka-testkit_2.12:2.5.18")
    testImplementation("org.assertj:assertj-core:3.8.0")
}
