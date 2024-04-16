/*
 * This file was generated by the Gradle 'init' task.
 */

plugins {
    application
    id("PrintScript.kotlin-application-conventions")
    id("publish-plugin")
}

dependencies {
    api(project(":cli"))
    implementation("com.github.ajalt.clikt:clikt:4.3.0")
}

application {
    // Define the main class for the application.
    mainClass = "app.AppKt"
}
