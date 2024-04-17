plugins {
    id("PrintScript.kotlin-library-conventions")
}

dependencies {
    api(project(":components"))
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.0")
}
