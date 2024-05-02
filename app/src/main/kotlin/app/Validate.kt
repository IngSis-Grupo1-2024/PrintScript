package app

import cli.ValidationCli
import cli.Version
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.help
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.path

class Validate : CliktCommand(help = "Validates a PrintScript script file") {
    private val fileInput by argument()
        .path(canBeDir = false, mustExist = true, mustBeReadable = true)
        .help { "the file path for the PrintScript code" }

    private val version by argument()
        .choice("v1", "v2")
        .help { "the version of the printscript" }

    private val fileOutput by argument()
        .path(canBeDir = false, mustExist = true, mustBeWritable = true)
        .help { "OPTIONAL: the file where the output will be put" }
        .optional()

    private lateinit var validationCli: ValidationCli

    override fun run() {
        startCli()
        if (outputPresent()) {
            validationCli.validateResultInFile(fileInput, fileOutput!!)
        } else {
            println(validationCli.validateFile(fileInput))
        }
    }

    private fun outputPresent(): Boolean = fileOutput != null

    private fun startCli() {
        if (version == "v1") {
            validationCli = ValidationCli(PrintOutputEmitter(), Version.VERSION_1, InputEmitter())
        }
        if (version == "v2") {
            validationCli = ValidationCli(PrintOutputEmitter(), Version.VERSION_2, InputEmitter())
        }
    }
}
