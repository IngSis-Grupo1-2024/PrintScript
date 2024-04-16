plugins{
    java
    `maven-publish`
}


publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/maxigeist/PrintScript")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_AUTHOR")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publications {
        register<MavenPublication>("maven") {
            groupId = "org.gradle.PrintScript"
            artifactId = "library"
            version = "1.0.0-SNAPSHOT"
            from(components["java"])
        }

    }
}