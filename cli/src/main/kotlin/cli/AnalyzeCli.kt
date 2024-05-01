package cli

import ingsis.components.Token
import ingsis.components.statement.Statement
import ingsis.interpreter.interpretStatement.Input
import ingsis.parser.error.ParserError
import ingsis.utils.OutputEmitter

class AnalyzeCli(outputEmitter: OutputEmitter, version: Version, input: Input) : Cli(outputEmitter, version, input) {
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
        val map = getSCAMap(rulePath)
        for (line in lines) {
            tokens = tokenizeWithLexer(line)
            if (tokens.isEmpty()) continue
            try {
                statement = parse(tokens)
                if (statement.isEmpty()) continue
                result = analyze(result, statement, map)
            } catch (e: ParserError) {
                result.append("\n" + e.localizedMessage + " in position :" + e.getTokenPosition())
            }
        }

        if (parser.isThereAnIf()) {
            result.append(
                sca.analyze(parser.getIfStatement(), map),
            )
        }

        if (result.isEmpty()) {
            return "SUCCESSFUL ANALYSIS"
        }
        return result.toString()
    }
}
