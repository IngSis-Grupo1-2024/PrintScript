package util

import app.*
import com.github.ajalt.clikt.core.subcommands

class FormatKT() {
    fun format(
        command: String,
        inputFilePath: String,
        version: String,
        outputFilePath: String,
        ruleFilePath: String,
    ) {
        App()
            .subcommands(Validate(), Execution(), Analyzer(), Format())
            .main(listOf(command, inputFilePath, version, outputFilePath, ruleFilePath))
    }
}
