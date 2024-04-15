package app

import cli.Cli
import cli.Version
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.default
import com.github.ajalt.clikt.parameters.arguments.help
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.path
import kotlin.io.path.Path
import kotlin.io.path.readText

class Analyzer: CliktCommand(help = "Analyze a PrintScript script file") {
    private val fileInput by argument()
        .path(canBeDir = false, mustExist = true, mustBeReadable = true)
        .help { "the file path for the PrintScript code" }

    private val version by argument()
        .choice("v1")
        .help { "the version of the printscript" }

    private val fileOutput by argument()
        .path(canBeDir = false, mustExist = true, mustBeWritable = true)
        .help { "OPTIONAL: the file where the output will be put" }
        .optional()

    private val rulesSCA by argument()
        .path(canBeDir = false, mustExist = true, mustBeReadable = true)
        .help { "OPTIONAL: this are the rules for the SCA" }
        .default(Path("../sca/src/main/resources/rules.json"))

    private lateinit var cli: Cli

    override fun run() {
        startCli()
        echo("rules for SCA: \n${rulesSCA.readText()}")
        echo("analyzing...")
        if (outputPresent()) {
            cli.analyzeFileInFileOutput(rulesSCA.toString(), fileInput.readText(), fileOutput!!.toString())
        } else {
            print(cli.analyzeFile(rulesSCA.toString(), fileInput.readText()))
        }
    }

    private fun outputPresent(): Boolean = fileOutput != null

    private fun startCli() {
        if (version == "v1") {
            cli = Cli(Version.VERSION_1)
        }
    }
}