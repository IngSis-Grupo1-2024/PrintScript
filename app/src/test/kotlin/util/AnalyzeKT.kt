package util

import app.*
import com.github.ajalt.clikt.core.subcommands

class AnalyzeKT() {
    fun analyzeWRules(
        command: String,
        inputFilePath: String,
        version: String,
        outputFilePath: String,
        ruleFilePath: String
    ) {
        App()
            .subcommands(Validate(), Execution(), Analyzer(), Format())
            .main(listOf(command, inputFilePath, version, outputFilePath, ruleFilePath))
    }

    fun analyzeWORules(
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
