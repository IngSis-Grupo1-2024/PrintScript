package app

import cli.Cli
import cli.Version
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.default
import com.github.ajalt.clikt.parameters.arguments.help
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.path
import kotlin.io.path.readText

class App : CliktCommand() {
    private val version by argument()
        .choice("v1")
        .help { "the version of the printscript" }

    private val operation by argument()
        .choice("validation", "execution", "formatting", "analyzing")
        .help { "this is the operation that you want to do with the code" }
        .default("execution")

    private val fileInput by argument()
        .path(canBeDir = false, mustExist = true, mustBeReadable = true)
        .help { "the file path for the PrintScript code" }

    private val fileOutput by argument()
        .path(canBeDir = false, mustExist = true, mustBeWritable = true)
        .help { "the file where the output will be put" }
        .optional()

    private val rules by option()
        .help { "this is the rules for additional SCA" }
        .default("none")

    private lateinit var cli: Cli

    override fun run() {
        startCli()
        if (checkValidation()) {
            doValidation()
        } else if (checkFormatting()) {
            doFormatting()
        } else if (checkAnalyzing()) {
            doAnalyze()
        } else {
            doExecution()
        }
    }

    private fun startCli() {
        if (version == "v1") {
            cli = Cli(Version.VERSION_1)
        }
    }

    private fun doExecution() {
        echo("execution in progress...")
        echo("rules for SCA: $rules")
        if (outputPresent()) {
            cli.startCliResultInFile(fileInput.readText(), fileOutput!!.toString())
        } else {
            print(cli.startCli(fileInput.readText()))
        }
    }

    private fun checkAnalyzing(): Boolean = operation == "analyzing"

    private fun doAnalyze() {
        echo("analyzing...")
        echo("rules for SCA: $rules")
        if (outputPresent()) {
            cli.analyzeFileInFileOutput(fileInput.readText(), fileOutput!!.toString())
        } else {
            print(cli.analyzeFile(fileInput.readText()))
        }
    }

    private fun doFormatting() {
        echo("formatting...")
        if (outputPresent()) {
            cli.format(fileInput.readText(), fileOutput!!)
        } else {
            cli.format(fileInput.readText(), fileInput)
        }
    }

    private fun checkFormatting(): Boolean = operation == "formatting"

    private fun doValidation() {
        echo("validation in progress...")
        if (outputPresent()) {
            cli.validateResultInFile(fileInput.readText(), fileOutput.toString())
        } else {
            cli.validate(fileInput.readText())
        }
    }

    private fun checkValidation() = operation == "validation"

    private fun outputPresent(): Boolean = fileOutput != null
}

fun main(args: Array<String>) {
    App().main(args)
}

fun test1WOoutput() =
    App()
        .main(listOf("v1", "formatting", "app/src/main/resources/test1"))

fun test1Woutput() = App().main(listOf("v1", "execution", "app/src/main/resources/test1", "app/src/main/resources/resultTest1"))

fun test2() = App().main(listOf("v1", "execution", "app/src/main/resources/test2"))

fun test2Woutput() = App().main(listOf("validation", "app/src/main/resources/test2", "app/src/main/resources/resultTest2"))

fun test3Woutput() =
    App().main(listOf("validation", "app/src/main/resources/errorWOSemicolon", "app/src/main/resources/resultErrorWOSemicolon"))
