package util

import app.*
import com.github.ajalt.clikt.core.subcommands

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
