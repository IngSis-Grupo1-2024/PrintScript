package cli

import components.Position
import components.Token
import components.statement.Statement
import error.ParserError
import ingsis.interpreter.PrintScriptInterpreter
import ingsis.lexer.Lexer
import ingsis.parser.PrintScriptParser
import scaRules.Rule
import java.io.PrintWriter

class Cli(private val scaRules: ArrayList<Rule>, version: Version) {
    private val lexer = Lexer(Position(0, 0))
    private val parser = PrintScriptParser.createParser(version.toString())
    private val interpreter = PrintScriptInterpreter.createInterpreter(version.toString())

    fun startCli(codeLines: String): String {
        val lines = splitLines(codeLines)
        var tokens: List<Token>
        var statement: Statement
        val string = StringBuilder()
        for ((i, line) in lines.withIndex()) {
            tokens = tokenizeWithLexer(line)
            string.append("\ntokens of line $i: $tokens")
            try {
                statement = parse(tokens)
                string.append("\nstatement of line $i -> $statement\n")
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
}
