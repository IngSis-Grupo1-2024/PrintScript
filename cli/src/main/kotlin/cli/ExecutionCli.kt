package cli

import ingsis.components.Token
import ingsis.components.statement.Statement
import ingsis.interpreter.interpretStatement.Input
import ingsis.utils.OutputEmitter
import ingsis.utils.Result
import java.io.FileInputStream
import java.io.InputStream
import java.nio.file.Path

class ExecutionCli(outputEmitter: OutputEmitter, version: Version, input: Input) : Cli(outputEmitter, version, input) {
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
            line = incrementOneLine(inputReader)
        }
        if (isThereAnIf()) {
            interpret(listOf(getIfStatement()), variableMap)
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
}
