import components.Position
import components.Token
import components.ast.ASTInterface
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
    private val lexer = Lexer(Position(0,0 ))
    private val parser = Parser(listOf(ScanDeclaration(), ScanAssignation(), ScanFunction()))
    fun startCli(codeLines: String) {
        val lines = splitLines(codeLines)
        var tokens: List<Token>
        for(line in lines) {
            tokens = tokenizeWithLexer(line)
            print(parse(tokens).toString())
        }
//        val variableMapList = interpret(astList)
    }

    private fun tokenizeWithLexer(line: String): List<Token> = lexer.tokenize(line)

    private fun parse(tokens: List<Token>): Statement = parser.parse(tokens)

    private fun interpret(astList: List<ASTInterface>): List<Map<String, Variable>> {
        val interpreter = Interpreter()
        val variableMapList = ArrayList<Map<String, Variable>>()
        val sca = Sca(scaRules)

        for (ast in astList) {
            if (sca.analyze(ast)) {
                variableMapList.add(interpreter.interpret(ast))
            }
        }

        return variableMapList
    }

    private fun splitLines(codeLines: String): List<String> {
        return codeLines.split("\n")
    }
}
