package app

import cli.AnalyzeCli
import cli.Version
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.default
import com.github.ajalt.clikt.parameters.arguments.help
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.path
import kotlin.io.path.Path

class Analyzer : CliktCommand(help = "Analyze a PrintScript script file") {
    private val fileInput by argument()
        .path(canBeDir = false, mustExist = true, mustBeReadable = true)
        .help { "the file path for the PrintScript code" }

    private val version by argument()
        .choice("v1", "v2")
        .help { "PrintScript version" }

    private val fileOutput by argument()
        .path(canBeDir = false, mustExist = true, mustBeWritable = true)
        .help { "OPTIONAL: the file where the output will be put" }
        .optional()

    private val rulesSCA by argument()
        .path(canBeDir = false, mustExist = true, mustBeReadable = true)
        .help { "OPTIONAL: this are the rules for the SCA" }
        .optional()

    private lateinit var analyzeCli: AnalyzeCli

    override fun run() {
        startAnalyzerCli()
        if (outputPresent()) {
            if(rulePathPresent())
                analyzeCli.analyzeFileInFileOutput(rulesSCA.toString(), fileInput, fileOutput!!)
            else analyzeCli.analyzeFileInFileOutput("", fileInput, fileOutput!!)

        } else {
            if(rulePathPresent())
                print(analyzeCli.analyzeFile(rulesSCA.toString(), fileInput))
            else
                print(analyzeCli.analyzeFile("", fileInput))
        }
    }

    private fun rulePathPresent(): Boolean =
        rulesSCA != null

    private fun outputPresent(): Boolean = fileOutput != null

    private fun startAnalyzerCli() {
        if (version == "v1") {
            analyzeCli = AnalyzeCli(PrintOutputEmitter(), Version.VERSION_1, InputEmitter())
        }
    }
}
