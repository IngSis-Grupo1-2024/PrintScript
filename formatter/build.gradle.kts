plugins {
    id("PrintScript.kotlin-library-conventions")
    id("publish-plugin")
}

dependencies {
    api(project(":components"))
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.0")
}
