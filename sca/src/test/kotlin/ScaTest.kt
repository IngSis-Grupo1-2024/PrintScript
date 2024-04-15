
import ingsis.components.Position
import ingsis.components.Token
import ingsis.components.TokenType
import ingsis.components.statement.*
import ingsis.sca.Sca
import ingsis.sca.scan.ScanIdentifierCase
import ingsis.sca.scan.ScanPrintLine
import org.junit.jupiter.api.Test

class ScaTest {
    @Test
    fun testScanAssignationWithVariable() {
        val printLine = PrintLine(Position(), SingleValue(Token(Position(), "a", TokenType.IDENTIFIER)))
        val scanPrintLine = ScanPrintLine(arrayListOf(TokenType.IDENTIFIER))
        val sca = Sca(listOf(scanPrintLine))
        sca.analyze(printLine, "src/main/kotlin/ingsis/sca/rules/rules.json")
    }

    @Test
    fun testScanAssignationWithLiteral() {
        val printLine = PrintLine(Position(), SingleValue(Token(Position(), "1", TokenType.INTEGER)))
        val scanPrintLine = ScanPrintLine(arrayListOf(TokenType.INTEGER))
        val sca = Sca(listOf(scanPrintLine))
        sca.analyze(printLine, "src/main/kotlin/ingsis/sca/rules/rules.json")
    }

    @Test
    fun testScanAssignationWithString() {
        val printLine = PrintLine(Position(), SingleValue(Token(Position(), "hello world", TokenType.STRING)))
        val scanPrintLine = ScanPrintLine(arrayListOf(TokenType.STRING))
        val sca = Sca(listOf(scanPrintLine))
        sca.analyze(printLine, "src/main/kotlin/ingsis/sca/rules/rules.json")
    }

    @Test
    fun testScanAssignationWithExpression() {
        val printLine =
            PrintLine(
                Position(),
                Operator(
                    Token(Position(), "+", TokenType.OPERATOR),
                    SingleValue(
                        Token(Position(), "1", TokenType.INTEGER),
                    ),
                    SingleValue(Token(Position(), "2", TokenType.INTEGER)),
                ),
            )
        val scanPrintLine = ScanPrintLine(arrayListOf(TokenType.OPERATOR))
        val sca = Sca(listOf(scanPrintLine))
        sca.analyze(printLine, "src/main/kotlin/ingsis/sca/rules/rules.json")
    }

    @Test
    fun testScanValidCamelCaseDeclaration() {
        val keyword = Keyword(Modifier.MUTABLE, "let", Position())
        val variable = Variable("isThisTestValid", Position())
        val type = Type(TokenType.INTEGER, Position())
        val declaration = Declaration(keyword, variable, type, Position())
        val scanIdentifierCase = ScanIdentifierCase()
        val sca = Sca(listOf(scanIdentifierCase))
        sca.analyze(declaration, "src/main/kotlin/ingsis/sca/rules/rules.json")
    }

    @Test
    fun testScanInvalidCamelCaseDeclaration() {
        val keyword = Keyword(Modifier.MUTABLE, "let", Position())
        val variable = Variable("is_this_test_valid", Position())
        val type = Type(TokenType.INTEGER, Position())
        val declaration = Declaration(keyword, variable, type, Position())
        val scanIdentifierCase = ScanIdentifierCase()
        val sca = Sca(listOf(scanIdentifierCase))
        sca.analyze(declaration, "src/main/kotlin/ingsis/sca/rules/rules.json")
    }

    @Test
    fun testScanValidSnakeCaseDeclaration() {
        val keyword = Keyword(Modifier.MUTABLE, "let", Position())
        val variable = Variable("is_this_test_valid", Position())
        val type = Type(TokenType.INTEGER, Position())
        val declaration = Declaration(keyword, variable, type, Position())
        val scanIdentifierCase = ScanIdentifierCase()
        val sca = Sca(listOf(scanIdentifierCase))
        sca.analyze(declaration, "src/main/kotlin/ingsis/sca/rules/rules.json")
    }

    @Test
    fun testScanInvalidSnakeCaseDeclaration() {
        val keyword = Keyword(Modifier.MUTABLE, "let", Position())
        val variable = Variable("isThisTestValid", Position())
        val type = Type(TokenType.INTEGER, Position())
        val declaration = Declaration(keyword, variable, type, Position())
        val scanIdentifierCase = ScanIdentifierCase()
        val sca = Sca(listOf(scanIdentifierCase))
        sca.analyze(declaration, "src/main/kotlin/ingsis/sca/rules/rules.json")
    }
}
