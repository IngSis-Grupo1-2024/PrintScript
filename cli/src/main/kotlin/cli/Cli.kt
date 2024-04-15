package cli

import ingsis.components.Position
import ingsis.components.Token
import ingsis.components.statement.Statement
import ingsis.formatter.PrintScriptFormatter
import ingsis.interpreter.PrintScriptInterpreter
import ingsis.lexer.PrintScriptLexer
import ingsis.parser.PrintScriptParser
import ingsis.parser.error.ParserError
import ingsis.sca.PrintScriptSca
import ingsis.utils.Result
import java.io.PrintWriter
import java.nio.file.Path

class Cli(version: Version) {
    private val lexer = PrintScriptLexer.createLexer(version.toString())
    private val parser = PrintScriptParser.createParser(version.toString())
    private val interpreter = PrintScriptInterpreter.createInterpreter(version.toString())
    private val formatter = PrintScriptFormatter.createFormatter(version.toString())
    private val sca = PrintScriptSca.createSCA(version.toString())
    private var position = Position()

    fun startCli(codeLines: String): String {
        val lines = splitLines(codeLines)
        if (lines.isEmpty()) return "empty file"
        var tokens: List<Token>
        var statement: Statement
        val string = StringBuilder()
        var variableMap = HashMap<String, Result>()
        for ((i, line) in lines.withIndex()) {
            tokens = tokenizeWithLexer(line)
            if (tokens.isEmpty()) continue
            try {
                statement = parse(tokens)
                string.append("\nstatement of line $i -> $statement\n")
                val pair = interpreter.interpret(statement, variableMap)
                variableMap = pair.first
                if (pair.second != null) string.append(pair.second)
            } catch (e: Exception) {
                string.append("\n" + e.localizedMessage)
            }
        }
        return string.toString()
    }

    private fun tokenizeWithLexer(line: String): List<Token> {
        if (line.isEmpty() || line == ";") {
            return emptyList()
        }
        if (line[0] == '\n') {
            incrementOneLine()
            return tokenizeWithLexer(line.substring(1))
        }
        return lexer.tokenize(line, position)
    }

    private fun incrementOneLine() {
        position =
            position.copy(
                startLine = position.startLine + 1,
                endLine = position.startLine + 1,
            )
    }

    private fun parse(tokens: List<Token>): Statement = parser.parse(tokens)

    private fun splitLines(codeLines: String): List<String> {
        return codeLines.split(";")
            .filter { it.isNotEmpty() }
            .map { "$it;" }
            .toList()
    }

    fun startCliResultInFile(
        fileInput: String,
        fileOutput: String,
    ) = writeInFile(fileOutput, startCli(fileInput))

    fun validate(codeLines: String): String {
        val lines = splitLines(codeLines)
        var tokens: List<Token>
        val string = StringBuilder()
        for (line in lines) {
            tokens = tokenizeWithLexer(line)
            if (tokens.isEmpty()) continue
            try {
                parse(tokens)
            } catch (e: ParserError) {
                string.append("\n" + e.localizedMessage + " in position :" + e.getTokenPosition())
            }
        }
        if (string.isEmpty()) {
            return "VALIDATION SUCCESSFUL"
        }
        return string.toString()
    }

    fun validateResultInFile(
        input: String,
        output: String,
    ) = writeInFile(output, validate(input))

    private fun writeInFile(
        fileOutput: String,
        string: String,
    ) {
        val writer = PrintWriter(fileOutput)
        writer.append(string)
        writer.close()
    }

    fun format(
        codeLines: String,
        file: Path,
    ) {
        val lines = splitLines(codeLines)
        var tokens: List<Token>
        var statement: Statement
        val result = StringBuilder()
        if (lines.isEmpty()) return writeInFile(file.toString(), "empty file")
        for (line in lines) {
            tokens = tokenizeWithLexer(line)
            if (tokens.isEmpty()) continue
            try {
                statement = parse(tokens)
                result.append(
                    formatter.format(statement, "formatter/src/main/kotlin/ingsis/formatter/rules/rules.json"),
                )
            } catch (e: ParserError) {
                result.append("\n" + e.localizedMessage + " in position :" + e.getTokenPosition())
            }
        }
        writeInFile(file.toString(), result.toString())
    }

    fun analyzeFileInFileOutput(
        codeLines: String,
        path: String,
    ) {
        writeInFile(path, analyzeFile(codeLines))
    }

    fun analyzeFile(codeLines: String): String {
        val lines = splitLines(codeLines)
        var tokens: List<Token>
        var statement: Statement
        val result = StringBuilder()
        for (line in lines) {
            tokens = tokenizeWithLexer(line)
            if (tokens.isEmpty()) continue
            try {
                statement = parse(tokens)
                result.append(
                    sca.analyze(
                        statement,
                        "/Users/tinavalenzi/projects/dissis/PrintScript/sca/src/main/kotlin/ingsis/sca/rules/rules.json",
                    ),
                )
            } catch (e: ParserError) {
                result.append("\n" + e.localizedMessage + " in position :" + e.getTokenPosition())
            }
        }
        return result.toString()
    }
}
