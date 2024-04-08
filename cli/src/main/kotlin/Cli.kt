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

class Cli(private val scaRules: ArrayList<Rule>) {
    private val lexer = Lexer(Position(0, 0))
    private val parser = Parser(listOf(ScanDeclaration(), ScanAssignation(), ScanFunction()))
    private val interpreter = Interpreter()

    fun startCli(codeLines: String) {
        val lines = splitLines(codeLines)
        var tokens: List<Token>
        var statement: Statement
        val variableMapList = ArrayList<Map<String, Variable>>()
        for ((i, line) in lines.withIndex()) {
            tokens = tokenizeWithLexer(line)
            print("\ntokens of line $i: $tokens")
            statement = parse(tokens)
            print("\nstatement of line $i -> $statement\n")
//            variableMapList = interpret(variableMapList)
        }
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
}
