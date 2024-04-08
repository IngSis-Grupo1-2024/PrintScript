import components.Position
import components.Token
import components.statement.Statement
import ingsis.interpreter.Interpreter
import ingsis.lexer.Lexer
import ingsis.parser.Parser
import ingsis.utils.Variable
import scaRules.Rule
import scan.ScanAssignation
import scan.ScanDeclaration
import scan.ScanFunction
import java.io.PrintWriter

class Cli(private val scaRules: ArrayList<Rule>) {
    private val lexer = Lexer(Position(0, 0))
    private val parser = Parser(listOf(ScanDeclaration(), ScanAssignation(), ScanFunction()))
    private val interpreter = Interpreter()

    fun startCli(codeLines: String) : String{
        val lines = splitLines(codeLines)
        var tokens: List<Token>
        var statement: Statement
        val string = StringBuilder()
//        val variableMapList = ArrayList<Map<String, Variable>>()
        for ((i, line) in lines.withIndex()) {
            tokens = tokenizeWithLexer(line)
            string.append("\ntokens of line $i: $tokens")

            statement = parse(tokens)
            string.append("\nstatement of line $i -> $statement\n")
//            variableMapList = interpret(variableMapList)
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

    fun startCliResultInFile(fileInput: String, fileOutput: String) {
        val string = startCli(fileInput)
        val writer = PrintWriter(fileOutput)
        writer.append(string)
        writer.close()
    }
}
