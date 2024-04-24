package app

import cli.Cli
import cli.InputEmitter
import cli.PrintOutputEmitter
import cli.Version
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.help
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.path

class Execution : CliktCommand(help = "Executes a PrintScript script file") {
    private val fileInput by argument()
        .path(canBeDir = false, mustExist = true, mustBeReadable = true)
        .help { "the file path for the PrintScript code" }

    private val version by argument()
        .choice("v1", "v2")
        .help { "Printscript version" }

    private val fileOutput by argument()
        .path(canBeDir = false, mustExist = true, mustBeWritable = true)
        .help { "OPTIONAL: the file where the output will be put" }
        .optional()

    private lateinit var cli: Cli

    override fun run() {
        startCli()
//        if (outputPresent()) {
//            cli.startCliResultInFile(fileInput.readText(), fileOutput!!.toString())
//        } else {
        cli.executeFile(fileInput)
//        }
    }

//    private fun outputPresent(): Boolean = fileOutput != null

    private fun startCli() {
        if (version == "v1") {
            cli = Cli(PrintOutputEmitter(), Version.VERSION_1, InputEmitter())
        }
        cli = Cli(PrintOutputEmitter(), Version.VERSION_2, InputEmitter())
    }
}
