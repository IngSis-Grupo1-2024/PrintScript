package cli

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ingsis.components.Position
import ingsis.components.Token
import ingsis.components.statement.Statement
import ingsis.formatter.PrintScriptFormatter
import ingsis.formatter.utils.FormatterRule
import ingsis.interpreter.PrintScriptInterpreter
import ingsis.interpreter.interpretStatement.Input
import ingsis.lexer.PrintScriptLexer
import ingsis.parser.PrintScriptParser
import ingsis.sca.PrintScriptSca
import ingsis.utils.OutputEmitter
import ingsis.utils.ReadScaRulesFile
import java.io.PrintWriter
import java.lang.reflect.Type
import java.nio.file.Files.readAllLines
import kotlin.io.path.Path

abstract class Cli(outputEmitter: OutputEmitter, version: Version, input: Input) {
    private val lexer = PrintScriptLexer.createLexer(version.toString())
    protected val parser = PrintScriptParser.createParser(version.toString())
    protected val formatter = PrintScriptFormatter.createFormatter(version.toString())
    protected val interpreter = PrintScriptInterpreter.createInterpreter(version.toString(), outputEmitter, input)
    protected val sca = PrintScriptSca.createSCA(version.toString())
    private var position = Position()

    protected fun tokenizeWithLexer(line: String): List<Token> {
        if (line.isEmpty() || line == ";") {
            return emptyList()
        }
        if (line[0] == '\n') {
            return tokenizeWithLexer(line.substring(1))
        }
        return lexer.tokenize(line, position)
    }

    protected fun incrementOneLine(inputStream: InputReader): String? {
        position =
            position.copy(
                startLine = position.startLine + 1,
                endLine = position.startLine + 1,
            )
        return inputStream.nextLine()
    }

    protected fun parse(tokens: List<Token>): List<Statement?> = parser.parse(tokens)

    protected fun splitLines(codeLines: String): List<String> {
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

    protected fun writeInFile(
        fileOutput: String,
        string: String,
    ) {
        val writer = PrintWriter(fileOutput)
        writer.append(string)
        writer.close()
    }

    protected fun analyze(
        result: StringBuilder,
        statements: List<Statement?>,
        ruleMap: ReadScaRulesFile,
    ): StringBuilder {
        statements.forEach { statement ->
            if (statement != null) {
                result.append(
                    sca.analyze(
                        statement,
                        ruleMap,
                    ),
                )
            }
        }
        return result
    }

    protected fun isThereAnIf(): Boolean = parser.isThereAnIf()

    protected fun getIfStatement(): Statement = parser.getIfStatement()

    protected fun getFormatterMap(rulePath: String): Map<String, FormatterRule> {
        if (rulePath == "") return emptyMap()
        val gson = Gson()
        val fileContent = getFileContent(rulePath)
        val typeToken: Type = object : TypeToken<Map<String, FormatterRule>>() {}.type
        return gson.fromJson(fileContent, typeToken)
    }

    protected fun getSCAMap(rulePath: String): ReadScaRulesFile {
        if (rulePath == "") {
            return ReadScaRulesFile()
        }
        val ruleMap = ReadScaRulesFile()
        if(rulePath == "") return ruleMap
        ruleMap.readSCARulesAndStackMap(rulePath)
        return ruleMap
    }

    private fun getFileContent(rulePath: String): String {
        val stringBuilder = StringBuilder()
        readAllLines(Path(rulePath)).forEach {
            stringBuilder.append(it)
        }
        return stringBuilder.toString()
    }
}
