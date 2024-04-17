package app

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands

class App : CliktCommand() {
    override fun run() {
    }
}

fun main(args: Array<String>) {
    App()
        .subcommands(Validate(), Execution(), Analyzer(), Format())
        .main(args)
//    val test = TestExecution()
//    test.testExecution()
}

class Exec() {
    fun execute(
        command: String,
        inputFilePath: String,
        version: String,
    ) {
        App()
            .subcommands(Validate(), Execution(), Analyzer(), Format())
            .main(listOf(command, inputFilePath, version))
    }
}
