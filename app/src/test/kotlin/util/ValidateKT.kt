package util

import app.*
import com.github.ajalt.clikt.core.subcommands

class ValidateKT() {
    fun validate(
        command: String,
        inputFilePath: String,
        version: String,
        outputFilePath: String,
    ) {
        App()
            .subcommands(Validate(), Execution(), Analyzer(), Format())
            .main(listOf(command, inputFilePath, version, outputFilePath))
    }
}
