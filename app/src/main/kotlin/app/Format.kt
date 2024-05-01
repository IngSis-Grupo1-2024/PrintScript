package app

import cli.FormatterCli
import cli.Version
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.default
import com.github.ajalt.clikt.parameters.arguments.help
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.path
import kotlin.io.path.Path

class Format : CliktCommand(help = "Formats a PrintScript script file") {
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

    private val rulesFormatter by argument()
        .path(canBeDir = false, mustExist = true, mustBeReadable = true)
        .help { "OPTIONAL: this are the rules" }
        .default(Path("../formatter/src/main/kotlin/ingsis/formatter/rules/rules.json"))

    private lateinit var formatter: FormatterCli

    override fun run() {
        startFormatter()

        if (outputPresent()) {
            formatter.formatFileResultInOutput(rulesFormatter.toString(), fileInput, fileOutput!!)
        } else {
            formatter.formatFile(rulesFormatter.toString(), fileInput)
        }
    }

    private fun outputPresent(): Boolean = fileOutput != null

    private fun startFormatter() {
        if (version == "v1") {
            formatter = FormatterCli(PrintOutputEmitter(), Version.VERSION_1, InputEmitter())
        }
        else formatter = FormatterCli(PrintOutputEmitter(), Version.VERSION_2, InputEmitter())
    }
}
