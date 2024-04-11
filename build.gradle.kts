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
