/*
 * This file was generated by the Gradle 'init' task.
 */

plugins {
    id("PrintScript.kotlin-library-conventions")
}

dependencies {
    api(project(":components"))
    api(project(":sca"))
    api(project(":lexer"))
    api(project(":parser"))
    api(project(":interpreter"))
    api(project(":formatter"))
}
