package app

import cli.Cli
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.parameters.arguments.*
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.path
import com.github.ajalt.mordant.rendering.AnsiLevel
import com.github.ajalt.mordant.terminal.Terminal
import kotlin.collections.ArrayList
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
        .convert { it.readText() }
        .help { "the file path for the PrintScript code" }

    private val fileOutput by argument()
        .path(canBeDir = false, mustExist = true, mustBeWritable = true)
        .help { "the file where the output will be put" }
        .optional()

    private val rules by option()
        .help { "this is the rules for additional SCA" }
        .default("none")

    private lateinit var cli : Cli

    override fun run() {
        startCli()
        if(checkValidation()) doValidation()
        else if(checkFormatting()) doFormatting()
        else if(checkAnalyzing()) doAnalyze()
        else{
            doExecution()
        }
    }

    private fun startCli() {
        if(version == "v1"){
            cli = Cli(ArrayList(), Version.VERSION_1)
        }
    }

    private fun doExecution() {
        echo("execution in progress...")
        echo("rules for SCA: $rules")
        if (outputPresent())
            cli.startCliResultInFile(fileInput, fileOutput!!.toString())
        else
            print(cli.startCli(fileInput))
    }

    private fun checkAnalyzing(): Boolean = operation == "analyzing"

    private fun doAnalyze() {
        echo("analyzing...")
        echo("rules for SCA: $rules")
        TODO("Not yet implemented")
    }

    private fun doFormatting() {
        echo("formatting...")
        if (outputPresent()) TODO("Format with output not yet implemented")
        else TODO("Format not yet implemented")

    }

    private fun checkFormatting(): Boolean =
        operation == "formatting"

    private fun doValidation() {
        echo("validation in progress...")
        if (outputPresent()) cli.validateResultInFile(fileInput, fileOutput.toString())
        else cli.validate(fileInput)
    }

    private fun checkValidation() = operation == "validation"

    private fun outputPresent(): Boolean = fileOutput != null
}

class Hello : CliktCommand() {
    override fun run() {
        echo("hello world")
    }
}

// fun main(args: Array<String>) {
//    App().main(args)
// }

fun main(args: Array<String>) {
    test1WOoutput()
}

fun test1WOoutput() =
    App()
        .main(listOf("v1", "validation", "app/src/main/resources/test1"))

fun test1Woutput() = App().main(listOf("execution","app/src/main/resources/test1", "app/src/main/resources/resultTest1"))

fun test2() = App().main(listOf("app/src/main/resources/test2"))

fun test2Woutput() = App().main(listOf("validation", "app/src/main/resources/test2", "app/src/main/resources/resultTest2"))

fun test3Woutput() = App().main(listOf("validation", "app/src/main/resources/errorWOSemicolon", "app/src/main/resources/resultErrorWOSemicolon"))
