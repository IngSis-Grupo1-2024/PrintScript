package cli

import ingsis.components.Token
import ingsis.interpreter.interpretStatement.Input
import ingsis.utils.OutputEmitter
import java.io.FileInputStream
import java.io.InputStream
import java.nio.file.Path

class ValidationCli(outputEmitter: OutputEmitter, version: Version, input: Input) : Cli(outputEmitter, version, input) {
    fun validateFile(filePath: Path): String {
        return validate(FileInputStream(filePath.toFile()))
    }

    fun validate(inputStream: InputStream): String {
        val inputReader = InputReader(inputStream)
        var tokens: List<Token>
        val string = StringBuilder()
        var line: String? = inputReader.nextLine()
        while (line != null) {
            val lines = splitLines(line)
            for (l in lines) {
                tokens = tokenizeWithLexer(l)
                if (tokens.isEmpty()) continue
                parse(tokens)
            }
            line = inputReader.nextLine()
        }
        if (string.isEmpty()) {
            return "VALIDATION SUCCESSFUL"
        }
        return string.toString()
    }

    fun validateResultInFile(
        input: Path,
        output: Path,
    ) = writeInFile(output.toString(), validateFile(input))
}
