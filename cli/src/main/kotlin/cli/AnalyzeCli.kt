package cli

import ingsis.components.Token
import ingsis.components.statement.Statement
import ingsis.interpreter.interpretStatement.Input
import ingsis.parser.error.ParserError
import ingsis.utils.OutputEmitter
import java.io.*
import java.nio.file.Path

class AnalyzeCli(outputEmitter: OutputEmitter, version: Version, input: Input) : Cli(outputEmitter, version, input) {
    fun analyzeFileInFileOutput(
        rulePath: String,
        inputFilePath: Path,
        outputFilePath: Path,
    ) {
        writeInFile(outputFilePath.toString(), analyzeFile(rulePath, inputFilePath))
    }
    fun analyzeFile(rulePath: String, filePath: Path): String =
        analyzeInputStream(rulePath, FileInputStream(filePath.toFile()))


    fun analyzeInputStream(
        rulePath: String,
        inputStream: InputStream,
    ): String {
        val inputReader = InputReader(inputStream)
        var line: String? = inputReader.nextLine()
        var tokens: List<Token>
        var statement: List<Statement?>
        var result = StringBuilder()
        val ruleMap = getSCAMap(rulePath)
        while (line != null) {
            val lines = splitLines(line)
            for (l in lines) {
                tokens = tokenizeWithLexer(l)
                if (tokens.isEmpty()) continue
                try {
                    statement = parse(tokens)
                    if (statement.isEmpty()) continue
                    result = analyze(result, statement, ruleMap)
                } catch (e: ParserError) {
                    result.append("\n" + e.localizedMessage + " in position :" + e.getTokenPosition())
                }
            }
            line = incrementOneLine(inputReader)
        }

        if (parser.isThereAnIf()) {
            result.append(
                sca.analyze(parser.getIfStatement(), ruleMap),
            )
        }

        if (result.isEmpty()) {
            return "SUCCESSFUL ANALYSIS"
        }
        return result.toString()
    }

}
