import ingsis.components.Position
import ingsis.components.Token
import ingsis.components.TokenType
import ingsis.components.statement.*
import ingsis.formatter.Formatter
import ingsis.formatter.PrintScriptFormatter
import ingsis.formatter.scan.ScanAssignation
import ingsis.formatter.scan.ScanCompoundAssignation
import ingsis.formatter.scan.ScanDeclaration
import ingsis.formatter.scan.ScanPrintLine
import ingsis.formatter.utils.FormatterRule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class FormatterTest {
    @Test
    fun testRuleClassCreation() {
        val formatterRule = FormatterRule(true, 1)
        assertEquals(true, formatterRule.on)
        assertEquals(1, formatterRule.quantity)
    }

    @Test
    fun testFormatWithSimpleDeclaration() {
        val scanners = listOf(ScanDeclaration())
        val formatter = Formatter(scanners)
        val keyword = Keyword(Modifier.MUTABLE, "let", Position())
        val variable = Variable("x", Position(5, 5, 1, 1, 5, 5))
        val type = Type(TokenType.INTEGER, Position(14, 14, 1, 1, 14, 14))
        val position = Position(8, 8, 1, 1, 8, 8)
        val declaration = Declaration(keyword, variable, type, position)
        val result = formatter.format(declaration, "src/main/kotlin/ingsis/formatter/rules/rules.json")
        val expected = "let x: number;\n"
        assertEquals(expected, result)
    }

    @Test
    fun testFormatWithSimpleAssignation() {
        val scanners = listOf(ScanAssignation())
        val formatter = Formatter(scanners)
        val variable = Variable("x", Position(1, 1, 1, 1, 1, 1))
        val token = Token(Position(3, 3, 1, 1, 3, 3), "4", TokenType.INTEGER)
        val value = SingleValue(token)
        val position = Position(2, 2, 1, 1, 2, 2)
        val assignation = Assignation(position, variable, value)
        val result = formatter.format(assignation, "src/main/kotlin/ingsis/formatter/rules/rules.json")
        val expected = "x  =  4;\n"
        assertEquals(expected, result)
    }

    @Test
    fun testFormatWithSimpleAssignationAndOperatorAsValue() {
        val scanners = listOf(ScanAssignation())
        val formatter = Formatter(scanners)
        val variable = Variable("x", Position(1, 1, 1, 1, 1, 1))
        val four = Token(Position(3, 3, 1, 1, 3, 3), "4", TokenType.INTEGER)
        val plus = Token(Position(4, 4, 1, 1, 4, 4), "+", TokenType.INTEGER)
        val five = Token(Position(5, 5, 1, 1, 5, 5), "5", TokenType.INTEGER)
        val value = Operator(plus, Operator(four), Operator(five))
        val position = Position(2, 2, 1, 1, 2, 2)
        val assignation = Assignation(position, variable, value)
        val result = formatter.format(assignation, "src/main/kotlin/ingsis/formatter/rules/rules.json")
        val expected = "x  =  4 + 5;\n"
        assertEquals(expected, result)
    }

    @Test
    fun testFormatWithCompoundAssignation() {
        val scanners = listOf(ScanCompoundAssignation())
        val formatter = Formatter(scanners)
        val int = Token(Position(16, 16, 1, 1, 1, 1), "4", TokenType.INTEGER)
        val value = SingleValue(int)
        val keyword = Keyword(Modifier.MUTABLE, "let", Position())
        val variable = Variable("x", Position(5, 5, 1, 1, 5, 5))
        val type = Type(TokenType.INTEGER, Position(14, 14, 1, 1, 14, 14))
        val declarationPosition = Position(8, 8, 1, 1, 8, 8)
        val declaration = Declaration(keyword, variable, type, declarationPosition)
        val assignationPosition = Position(15, 15, 1, 1, 14, 14)
        val compoundAssignation = CompoundAssignation(assignationPosition, declaration, value)
        val result = formatter.format(compoundAssignation, "src/main/kotlin/ingsis/formatter/rules/rules.json")
        val expected = "let x: number  =  4;\n"
        assertEquals(expected, result)
    }

    @Test
    fun testFormatWithPrintFunction() {
        val scanners = listOf(ScanPrintLine())
        val formatter = Formatter(scanners)
        val token =
            Token(
                Position(8, 8, 1, 1, 8, 8),
                "4",
                TokenType.INTEGER,
            )
        val value = SingleValue(token)
        val function =
            PrintLine(
                Position(0, 6, 1, 1, 0, 6),
                value,
            )
        val result = formatter.format(function, "src/main/kotlin/ingsis/formatter/rules/rules.json")
        val expected =
            "\n" +
                "\n" +
                "println(4);" +
                "\n"
        assertEquals(expected, result)
    }

    @Test
    fun testCreatePrintScriptFormatterV1() {
        val formaterV1 = PrintScriptFormatter.createFormatter("VERSION_1")
        val scanners = listOf(ScanDeclaration(), ScanAssignation(), ScanPrintLine(), ScanCompoundAssignation())
        val printLineStatement = PrintLine(Position(), SingleValue(Token(Position(), "4", TokenType.INTEGER)))
        assertEquals(
            formaterV1.format(printLineStatement, "src/main/kotlin/ingsis/formatter/rules/rules.json"),
            Formatter(scanners).format(printLineStatement, "src/main/kotlin/ingsis/formatter/rules/rules.json"),
        )
    }

    @Test
    fun testCreatePrintScriptFormatterWithInvalidVersion() {
        val formaterV1 = PrintScriptFormatter.createFormatter("VERSION_4")
        val scanners = listOf(ScanDeclaration(), ScanAssignation(), ScanPrintLine(), ScanCompoundAssignation())
        val printLineStatement = PrintLine(Position(), SingleValue(Token(Position(), "4", TokenType.INTEGER)))
        assertEquals(
            formaterV1.format(printLineStatement, "src/main/kotlin/ingsis/formatter/rules/rules.json"),
            Formatter(scanners).format(printLineStatement, "src/main/kotlin/ingsis/formatter/rules/rules.json"),
        )
    }

    @Test
    fun testInvalidPathToReadRules() {
        val formaterV1 = PrintScriptFormatter.createFormatter("VERSION_1")
        val printLineStatement = PrintLine(Position(), SingleValue(Token(Position(), "4", TokenType.INTEGER)))
        assertThrows<Exception> {
            formaterV1.format(printLineStatement, "src/main/kotlin/ingsis/formatter/rules/rules2.json")
        }
    }
}
