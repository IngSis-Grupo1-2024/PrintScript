plugins{
    `maven-publish`
}


publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/IngSis-Grupo1-2024/PrintScript")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_AUTHOR")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            version = "1.1.0-SNAPSHOT"
            from(components["java"])
        }
    }
}
