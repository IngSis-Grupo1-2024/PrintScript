
import ingsis.components.Position
import ingsis.components.Token
import ingsis.components.TokenType
import ingsis.components.statement.*
import ingsis.sca.Sca
import ingsis.sca.scan.ScanIdentifierCase
import ingsis.sca.scan.ScanPrintLine
import ingsis.sca.scan.ScanReadInput
import ingsis.utils.ReadScaRulesFile
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ScaTest {
    @Test
    fun testScanAssignationWithVariable() {
        val printLine = PrintLine(Position(), SingleValue(Token(Position(), "a", TokenType.IDENTIFIER)))
        val scanPrintLine = ScanPrintLine(emptyList())
        val sca = Sca(listOf(scanPrintLine))
        val ruleMap = ReadScaRulesFile()
        ruleMap.readSCARulesAndStackMap("src/main/resources/rules.json")
        Assertions.assertEquals("PRINT_LINE with IDENTIFIER is not allowed at line 1 and column 1\n", sca.analyze(printLine, ruleMap))
    }

    @Test
    fun testScanAssignationWithLiteral() {
        val printLine = PrintLine(Position(), SingleValue(Token(Position(), "1", TokenType.INTEGER)))
        val scanPrintLine = ScanPrintLine(arrayListOf(TokenType.INTEGER))
        val sca = Sca(listOf(scanPrintLine))
        val ruleMap = ReadScaRulesFile()
        ruleMap.readSCARulesAndStackMap("src/main/resources/rules.json")
        Assertions.assertEquals("", sca.analyze(printLine, ruleMap))
    }

    @Test
    fun testScanAssignationWithString() {
        val printLine = PrintLine(Position(), SingleValue(Token(Position(), "hello world", TokenType.STRING)))
        val scanPrintLine = ScanPrintLine(arrayListOf(TokenType.STRING))
        val sca = Sca(listOf(scanPrintLine))
        val ruleMap = ReadScaRulesFile()
        ruleMap.readSCARulesAndStackMap("src/main/resources/rules.json")
        Assertions.assertEquals("", sca.analyze(printLine, ruleMap))
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
        val scanPrintLine = ScanPrintLine(arrayListOf())
        val sca = Sca(listOf(scanPrintLine))
        val ruleMap = ReadScaRulesFile()
        ruleMap.readSCARulesAndStackMap("src/main/resources/rules.json")
        Assertions.assertEquals("PRINT_LINE with expression is not allowed at line 1 and column 1\n", sca.analyze(printLine, ruleMap))
    }

    @Test
    fun testScanValidCamelCaseDeclaration() {
        val keyword = Keyword(Modifier.MUTABLE, "let", Position())
        val variable = Variable("isThisTestValid", Position())
        val type = Type(TokenType.INTEGER, Position())
        val declaration = Declaration(keyword, variable, type, Position())
        val scanIdentifierCase = ScanIdentifierCase()
        val sca = Sca(listOf(scanIdentifierCase))
        val ruleMap = ReadScaRulesFile()
        ruleMap.readSCARulesAndStackMap("src/main/resources/rules.json")
        Assertions.assertEquals("", sca.analyze(declaration, ruleMap))
    }

    @Test
    fun testScanInvalidSnakeCaseDeclaration() {
        val keyword = Keyword(Modifier.MUTABLE, "let", Position())
        val variable = Variable("is_this_test_valid", Position())
        val type = Type(TokenType.INTEGER, Position())
        val declaration = Declaration(keyword, variable, type, Position())
        val scanIdentifierCase = ScanIdentifierCase()
        val sca = Sca(listOf(scanIdentifierCase))
        val ruleMap = ReadScaRulesFile()
        ruleMap.readSCARulesAndStackMap("src/main/resources/rules.json")
        Assertions.assertEquals(
            "Variable name is_this_test_valid is not in camelCase format at line 1 and column 1\n",
            sca.analyze(declaration, ruleMap),
        )
    }

    @Test
    fun testScanReadInputWithValidLiteralInteger() {
        val value = SingleValue(Token(Position(), "4", TokenType.INTEGER))
        val variable = Variable("x", Position())
        val assignationReadInput = AssignationReadInput(Position(), variable, value)
        val scanReadInput = ScanReadInput(arrayListOf(TokenType.INTEGER))
        val sca = Sca(listOf(scanReadInput))
        val ruleMap = ReadScaRulesFile()
        ruleMap.readSCARulesAndStackMap("src/main/resources/rules.json")
        Assertions.assertEquals("", sca.analyze(assignationReadInput, ruleMap))
    }

    @Test
    fun testScanReadInputWithInvalidLiteralString() {
        val value = SingleValue(Token(Position(), "4", TokenType.INTEGER))
        val variable = Variable("x", Position())
        val assignationReadInput = AssignationReadInput(Position(), variable, value)
        val scanReadInput = ScanReadInput(arrayListOf(TokenType.STRING))
        val sca = Sca(listOf(scanReadInput))
        val ruleMap = ReadScaRulesFile()
        ruleMap.readSCARulesAndStackMap("src/main/resources/rules.json")
        Assertions.assertEquals(
            "ASSIGNATION_READ_INPUT with INTEGER is not allowed at line 1 and column 1\n",
            sca.analyze(assignationReadInput, ruleMap),
        )
    }

    @Test
    fun testScanCompoundReadInputWithInvalidLiteralString() {
        val value = SingleValue(Token(Position(), "4", TokenType.INTEGER))
        val declaration =
            Declaration(
                Keyword(Modifier.MUTABLE, "let", Position()),
                Variable("x", Position()),
                Type(TokenType.INTEGER, Position()),
                Position(),
            )
        val assignationReadInput = CompoundAssignationReadInput(Position(), declaration, value)
        val scanReadInput = ScanReadInput(arrayListOf(TokenType.STRING))
        val sca = Sca(listOf(scanReadInput))
        val ruleMap = ReadScaRulesFile()
        ruleMap.readSCARulesAndStackMap("src/main/resources/rules.json")
        Assertions.assertEquals(
            "COMPOUND_ASSIGNATION_READ_INPUT with INTEGER is not allowed at line 1 and column 1\n",
            sca.analyze(assignationReadInput, ruleMap),
        )
    }
}
