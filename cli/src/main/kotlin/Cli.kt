import components.Position
import components.Token
import components.statement.Statement
import error.ParserError
import ingsis.interpreter.Interpreter
import ingsis.interpreter.interpretStatement.AssignationInterpreter
import ingsis.interpreter.interpretStatement.CompoundAssignationInterpreter
import ingsis.interpreter.interpretStatement.DeclarationInterpreter
import ingsis.interpreter.interpretStatement.PrintLineInterpreter
import ingsis.lexer.Lexer
import ingsis.parser.Parser
import ingsis.utils.Result
import scaRules.Rule
import scan.ScanAssignation
import scan.ScanDeclaration
import scan.ScanPrintLine
import java.io.PrintWriter

class Cli(private val scaRules: ArrayList<Rule>) {
    private val lexer = Lexer(Position(0, 0))
    private val parser = Parser(listOf(ScanDeclaration(), ScanAssignation(), ScanPrintLine()))
    private val interpreter =
        Interpreter(listOf(DeclarationInterpreter(), AssignationInterpreter(), CompoundAssignationInterpreter(), PrintLineInterpreter()))

    fun startCli(codeLines: String): String {
        val lines = splitLines(codeLines)
        var tokens: List<Token>
        var statement: Statement
        val string = StringBuilder()
        var variableMap = HashMap<String, Result>()
        for ((i, line) in lines.withIndex()) {
            tokens = tokenizeWithLexer(line)
            string.append("\ntokens of line $i: $tokens")
            try {
                statement = parse(tokens)
                string.append("\nstatement of line $i -> $statement\n")
                variableMap = interpreter.interpret(statement, variableMap)
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

    fun validate(codeLines: String) : String {
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
        if(string.isEmpty()) {
            return "VALIDATION SUCCESSFUL"
        }
        return string.toString()
    }
    fun validateResultInFile(input: String, output: String) =
        writeInFile(output, validate(input))

    private fun writeInFile(fileOutput: String, string: String) {
        val writer = PrintWriter(fileOutput)
        writer.append(string)
        writer.close()
    }
}
