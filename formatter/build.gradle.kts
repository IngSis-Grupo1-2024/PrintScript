plugins {
    id("PrintScript.kotlin-library-conventions")
}


dependencies {
    api(project(":components"))
    implementation("com.google.code.gson:gson:2.8.8")
}
