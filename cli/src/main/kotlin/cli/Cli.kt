package cli

import ingsis.components.Position
import ingsis.components.Token
import ingsis.components.statement.Statement
import ingsis.formatter.PrintScriptFormatter
import ingsis.interpreter.PrintScriptInterpreter
import ingsis.interpreter.interpretStatement.Input
import ingsis.lexer.PrintScriptLexer
import ingsis.parser.PrintScriptParser
import ingsis.parser.error.ParserError
import ingsis.sca.PrintScriptSca
import ingsis.utils.OutputEmitter
import ingsis.utils.Result
import java.io.FileInputStream
import java.io.InputStream
import java.io.PrintWriter
import java.nio.file.Path

class Cli(outputEmitter: OutputEmitter, version: Version, input: Input) {
    private val lexer = PrintScriptLexer.createLexer(version.toString())
    private val parser = PrintScriptParser.createParser(version.toString())
    private val interpreter = PrintScriptInterpreter.createInterpreter(version.toString(), outputEmitter, input)
    private val formatter = PrintScriptFormatter.createFormatter(version.toString())
    private val sca = PrintScriptSca.createSCA(version.toString())
    private var position = Position()

    fun executeFile(filePath: Path) {
        executeInputStream(FileInputStream(filePath.toFile()))
    }

    fun executeInputStream(inputStream: InputStream) {
        val inputReader = InputReader(inputStream)
        var tokens: List<Token>
        var statement: List<Statement?>
        var variableMap = HashMap<String, Result>()
        var line: String? = inputReader.nextLine()
        while (line != null) {
            val lines = splitLines(line)
            for (l in lines) {
                tokens = tokenizeWithLexer(l)
                if (tokens.isEmpty()) continue
                statement = parse(tokens)
                if (statement.isEmpty()) continue
                variableMap = interpret(statement, variableMap)
            }
            line = inputReader.nextLine()
            incrementOneLine()
        }
        if (parser.isThereAnIf()) {
            interpreter.interpret(parser.getIfStatement(), variableMap)
        }
    }

    private fun interpret(
        statements: List<Statement?>,
        variableMap: HashMap<String, Result>,
    ): java.util.HashMap<String, Result> {
        var map = variableMap
        statements.forEach { statement ->
            if (statement != null) map = interpreter.interpret(statement, map)
        }
        return map
    }

    private fun tokenizeWithLexer(line: String): List<Token> {
        if (line.isEmpty() || line == ";") {
            return emptyList()
        }
        if (line[0] == '\n') {
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

    private fun parse(tokens: List<Token>): List<Statement?> = parser.parse(tokens)

    private fun splitLines(codeLines: String): List<String> {
        val delimiter = ";"
        val splitRegex = "(?<=$delimiter|\n)".toRegex()
        return codeLines.split(splitRegex)
            .map { it.trim() }
            .mapIndexed { index, part ->
                if (part.isNotEmpty() && codeLines[index] == '\n') {
                    part + "\n"
                } else {
                    part
                }
            }
    }

//    fun startCliResultInFile(
//        fileInput: String,
//        fileOutput: String,
//    ) = writeInFile(fileOutput, startCli(fileInput))

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
        rulePath: String,
        codeLines: String,
        file: Path,
    ) {
        val lines = splitLines(codeLines)
        var tokens: List<Token>
        var statement: List<Statement?>
        var result = StringBuilder()
        if (lines.isEmpty()) return writeInFile(file.toString(), "empty file")
        for (line in lines) {
            tokens = tokenizeWithLexer(line)
            if (tokens.isEmpty()) continue
            try {
                statement = parse(tokens)
                if (statement.isEmpty()) continue
                result = format(result, statement, rulePath)
            } catch (e: ParserError) {
                result.append("\n" + e.localizedMessage + " in position :" + e.getTokenPosition())
            }
        }
        if (parser.isThereAnIf()) {
            result.append(
                formatter.format(parser.getIfStatement(), rulePath),
            )
        }
        writeInFile(file.toString(), result.toString())
    }

    private fun format(
        result: StringBuilder,
        statements: List<Statement?>,
        rulePath: String,
    ): StringBuilder {
        statements.forEach { statement ->
            if (statement != null) {
                result.append(
                    formatter.format(statement, readJsonAndStackMap(rulePath)),
                )
            }
        }
        return result
    }

    fun analyzeFileInFileOutput(
        rulePath: String,
        codeLines: String,
        path: String,
    ) {
        writeInFile(path, analyzeFile(rulePath, codeLines))
    }

    fun analyzeFile(
        rulePath: String,
        codeLines: String,
    ): String {
        val lines = splitLines(codeLines)
        var tokens: List<Token>
        var statement: List<Statement?>
        var result = StringBuilder()
        for (line in lines) {
            tokens = tokenizeWithLexer(line)
            if (tokens.isEmpty()) continue
            try {
                statement = parse(tokens)
                if (statement.isEmpty()) continue
                result = analyze(result, statement, rulePath)
            } catch (e: ParserError) {
                result.append("\n" + e.localizedMessage + " in position :" + e.getTokenPosition())
            }
        }

        if (parser.isThereAnIf()) {
            result.append(
                sca.analyze(parser.getIfStatement(), rulePath),
            )
        }

        if (result.isEmpty()) {
            return "SUCCESSFUL ANALYSIS"
        }
        return result.toString()
    }

    private fun analyze(
        result: StringBuilder,
        statements: List<Statement?>,
        rulePath: String,
    ): StringBuilder {
        val rules = ReadScaRulesFile()
        rules.readSCARulesAndStackMap(rulePath)
        statements.forEach { statement ->
            if (statement != null) {
                result.append(
                    sca.analyze(
                        statement,
                        rules,
                    ),
                )
            }
        }
        return result
    }
}
