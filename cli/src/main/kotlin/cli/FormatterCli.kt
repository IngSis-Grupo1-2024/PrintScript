package cli

import ingsis.components.Token
import ingsis.components.statement.Statement
import ingsis.formatter.utils.FormatterRule
import ingsis.interpreter.interpretStatement.Input
import ingsis.parser.error.ParserError
import ingsis.utils.OutputEmitter
import java.io.*
import java.nio.charset.StandardCharsets
import java.nio.file.Path

class FormatterCli(outputEmitter: OutputEmitter, version: Version, input: Input) : Cli(outputEmitter, version, input) {
    fun formatFileResultInOutput(
        rulePath: String,
        input: Path,
        output: Path,
    )  {
        val inputStream = formatInputStream(rulePath, FileInputStream(input.toFile()))
        writeInputStreamInFile(inputStream, output)
    }

    fun formatFile(
        rulePath: String,
        file: Path,
    )  {
        val inputStream = formatInputStream(rulePath, FileInputStream(file.toFile()))
        writeInputStreamInFile(inputStream, file)
    }

    private fun writeInputStreamInFile(
        inputStream: InputStream,
        output: Path,
    ) {
        val inputReader = InputReader(inputStream)
        var line: String? = inputReader.nextLine()
        val writer = FileWriter(output.toFile())
        while (line != null) {
            if(line == "") writer.write("\n")
            else writer.write(line + "\n")
            line = inputReader.nextLine()
        }
        writer.close()
    }

    fun formatInputStream(
        rulePath: String,
        inputStream: InputStream,
    ): InputStream  {
        val inputReader = InputReader(inputStream)
        var tokens: List<Token>
        var statement: List<Statement?>
        var result: InputStream = ByteArrayInputStream(ByteArray(0))
        var line: String? = inputReader.nextLine()
        val ruleMap : Map<String, FormatterRule> = getFormatterMap(rulePath)
        while (line != null) {
            val lines = splitLines(line)
            for (l in lines) {
                tokens = tokenizeWithLexer(l)
                if (tokens.isEmpty()) continue
                try {
                    statement = parse(tokens)
                    if (statement.isEmpty()) continue
                    result = SequenceInputStream(result, getFormatBytes(statement, ruleMap))
                } catch (e: ParserError) {
                    result = addError(result, e)
                }
            }
            line = incrementOneLine(inputReader)
        }
        if (isThereAnIf()) {
            result = SequenceInputStream(result, getFormatBytes(listOf(getIfStatement()), ruleMap))
        }
        return result
    }

    private fun addError(
        result: InputStream,
        e: ParserError,
    ): InputStream {
        val message = "\n" + e.localizedMessage + " in position :" + e.getTokenPosition()
        return SequenceInputStream(result, ByteArrayInputStream(message.toByteArray()))
    }

    private fun format(
        statements: List<Statement?>,
        ruleMap:  Map<String, FormatterRule>,
    ): String {
        val result = StringBuilder()
        statements.forEach { statement ->
            if (statement != null) {
                result.append(
                    formatter.format(statement, ruleMap),
                )
            }
        }
        return result.toString()
    }

    private fun getFormatBytes(
        statements: List<Statement?>,
        ruleMap:  Map<String, FormatterRule>,
    ): ByteArrayInputStream {
        val result = format(statements, ruleMap)
        return ByteArrayInputStream(result.toByteArray())
    }
}
