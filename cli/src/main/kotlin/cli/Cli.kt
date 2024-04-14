package cli

import components.Token
import components.statement.Statement
import ingsis.formatter.PrintScriptFormatter
import ingsis.interpreter.PrintScriptInterpreter
import ingsis.lexer.PrintScriptLexer
import ingsis.parser.PrintScriptParser
import ingsis.parser.error.ParserError
import ingsis.utils.Result
import scaRules.Rule
import java.io.PrintWriter
import java.nio.file.Path

class Cli(private val scaRules: ArrayList<Rule>, version: Version) {
    private val lexer = PrintScriptLexer.createLexer(version.toString())
    private val parser = PrintScriptParser.createParser(version.toString())
    private val interpreter = PrintScriptInterpreter.createInterpreter(version.toString())
    private val formatter = PrintScriptFormatter.createFormatter(version.toString())

    fun startCli(codeLines: String): String {
        val lines = splitLines(codeLines)
        var tokens: List<Token>
        var statement: Statement
        val string = StringBuilder()
        val variableMap = HashMap<String, Result>()
        for ((i, line) in lines.withIndex()) {
            tokens = tokenizeWithLexer(line)
            if (tokens.isEmpty()) continue
            try {
                statement = parse(tokens)
                string.append("\nstatement of line $i -> $statement\n")
                interpreter.interpret(statement, variableMap)
            } catch (e: Exception) {
                string.append("\n" + e.localizedMessage)
            }
        }
        return string.toString()
    }

    private fun tokenizeWithLexer(line: String): List<Token> = lexer.tokenize(line)

    private fun parse(tokens: List<Token>): Statement = parser.parse(tokens)

//    private fun interpret(statement: Statement, map: ArrayList<Map<String, Variable>>): ArrayList<Map<String, Variable>> {
//        val sca = Sca(scaRules)
//        if (sca.analyze(statement)) {
//            map.add(interpreter.interpret(statement))
//        }
//        return map
//    }

    private fun splitLines(codeLines: String): List<String> {
        return codeLines.split("\n")
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
        for (line in lines) {
            tokens = tokenizeWithLexer(line)
            if (tokens.isEmpty()) continue
            try {
                statement = parse(tokens)
                result.append(formatter.format(statement, "formatter/src/main/kotlin/ingsis/formatter/rules/rules.json"))
            } catch (e: ParserError) {
                result.append("\n" + e.localizedMessage + " in position :" + e.getTokenPosition())
            }
        }
        writeInFile(file.toString(), result.toString())
    }
}
