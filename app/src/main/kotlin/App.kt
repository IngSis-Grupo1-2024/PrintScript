import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.convert
import com.github.ajalt.clikt.parameters.arguments.help
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.path
import java.util.ArrayList
import kotlin.io.path.readText

class App : CliktCommand(name = "PrintScript") {
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

    // agregar opciones para interpreter, SCA, interpreter with SCA or Formatter
    //    el formatter debe tener un input y output

    private val cli = Cli(ArrayList())

    override fun run() {
        echo("rules for SCA: $rules")
        if (fileOutput != null) {
            cli.startCliResultInFile(fileInput, fileOutput!!.toString())
        } else {
            print(cli.startCli(fileInput))
        }
    }
}

fun main() {
//    test1Woutput()
//    test2Woutput()
    test3Woutput()
}

fun test1WOoutput() = App().main(listOf("app/src/main/resources/test1"))

fun test1Woutput() = App().main(listOf("app/src/main/resources/test1", "app/src/main/resources/resultTest1"))

fun test2() = App().main(listOf("app/src/main/resources/test2"))

fun test2Woutput() = App().main(listOf("app/src/main/resources/test2", "app/src/main/resources/resultTest2"))

fun test3Woutput() = App().main(listOf("app/src/main/resources/errorWOSemicolon", "app/src/main/resources/resultErrorWOSemicolon"))
