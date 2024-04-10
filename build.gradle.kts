tasks.register("checkHooks") {
    doLast {
        val hooksDir = file("hooks")
        val gitHooksDir = file(".git/hooks")

        hooksDir.listFiles()?.forEach { projectHook ->
            val gitHook = File(gitHooksDir, projectHook.name)
            if (!gitHook.exists() || projectHook.readText() != gitHook.readText()) {
                projectHook.copyTo(gitHook, overwrite = true)
                println("Copied hook ${projectHook.name} to .git/hooks directory.")
            }
        }

        gitHooksDir.listFiles()?.forEach { gitHook ->
            val projectHook = File(hooksDir, gitHook.name)
            if (!projectHook.exists()) {
                gitHook.delete()
                println("Deleted hook ${gitHook.name} from .git/hooks directory.")
            }
        }
    }
}


plugins {
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
            version = "1.1.0-SNAPSHOT"
            from(components["java"])
        }

    }
}
