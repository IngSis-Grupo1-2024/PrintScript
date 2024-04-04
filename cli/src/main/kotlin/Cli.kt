import components.Position
import components.Token
import components.ast.ASTInterface
import ingsis.interpreter.Interpreter
import ingsis.lexer.Lexer
import ingsis.parser.Parser
import ingsis.utils.Variable
import scaRules.Rule
import java.io.File

class Cli(private val scaRules: ArrayList<Rule>) {
    private fun readFile(fileName: String): String {
        return File(fileName)
            .inputStream()
            .bufferedReader()
            .use { it.readText() }
    }

    fun startCli(fileName: String) {
        val codeLines = readFile(fileName)
        val lines = splitLines(codeLines)

        val tokens = tokenizeWithLexer()
        val astList = parse(tokens)
        val variableMapList = interpret(astList)
    }

    private fun tokenizeWithLexer(): List<List<Token>> {
        val tokenList = ArrayList<List<Token>>()
        val lexer = Lexer(Position())

//        for (line in splitLines()) {
//            tokenList.add(lexer.tokenize(line))
//        }
        return tokenList
    }

    private fun parse(tokens: List<List<Token>>): List<ASTInterface> {
        val parser = Parser()
        val treeList = ArrayList<ASTInterface>()

        for (token in tokens) {
            treeList.add(parser.parse(token))
        }

        return treeList
    }

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
