import ingsis.components.Position
import ingsis.components.Token
import ingsis.components.TokenType
import ingsis.components.statement.*
import ingsis.sca.Sca
import ingsis.sca.defaultConfig.getDefaultBooleanValue
import ingsis.sca.defaultConfig.getDefaultCase
import ingsis.sca.scan.ScanIdentifierCase
import ingsis.sca.scan.ScanPrintLine
import ingsis.sca.scan.ScanReadInput
import ingsis.sca.scan.cases.ScanCamelCase
import ingsis.utils.ReadScaRulesFile
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ScaTest {
    @Test
    fun testScanPrintLineWithVariable() {
        val printLine = PrintLine(Position(), SingleValue(Token(Position(), "a", TokenType.IDENTIFIER)))
        val scanPrintLine = ScanPrintLine(emptyList())
        val sca = Sca(listOf(scanPrintLine))
        val ruleMap = ReadScaRulesFile()
        ruleMap.readSCARulesAndStackMap("src/main/resources/rules.json")
        assertEquals(
            "PRINT_LINE with IDENTIFIER is not allowed at line 1 and column 1\n",
            sca.analyze(printLine, ruleMap),
        )
    }

    @Test
    fun testScanPrintLineWithLiteral() {
        val printLine = PrintLine(Position(), SingleValue(Token(Position(), "1", TokenType.INTEGER)))
        val scanPrintLine = ScanPrintLine(arrayListOf(TokenType.INTEGER))
        val sca = Sca(listOf(scanPrintLine))
        val ruleMap = ReadScaRulesFile()
        ruleMap.readSCARulesAndStackMap("src/main/resources/rules.json")
        assertEquals("", sca.analyze(printLine, ruleMap))
    }

    @Test
    fun testScanPrintLineWithString() {
        val printLine = PrintLine(Position(), SingleValue(Token(Position(), "hello world", TokenType.STRING)))
        val scanPrintLine = ScanPrintLine(arrayListOf(TokenType.STRING))
        val sca = Sca(listOf(scanPrintLine))
        val ruleMap = ReadScaRulesFile()
        ruleMap.readSCARulesAndStackMap("src/main/resources/rules.json")
        assertEquals("", sca.analyze(printLine, ruleMap))
    }

    @Test
    fun testScanPrintLineWithExpression() {
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
        Assertions.assertEquals(
            "PRINT_LINE with expression is not allowed at line 1 and column 1\n",
            sca.analyze(printLine, ruleMap),
        )
    }

    @Test
    fun testScanPrintLineWithInvalidLiteralsOfTypeInteger() {
        val printLine =
            PrintLine(
                Position(),
                SingleValue(
                    Token(Position(), "1", TokenType.INTEGER),
                ),
            )
        val scanPrintLine = ScanPrintLine(arrayListOf(TokenType.INTEGER))
        val sca = Sca(listOf(scanPrintLine))
        val ruleMap = ReadScaRulesFile()
        ruleMap.readSCARulesAndStackMap("src/main/resources/testRulesWithNoLiteral.json")
        Assertions.assertEquals(
            "PRINT_LINE with INTEGER is not allowed at line 1 and column 1\n",
            sca.analyze(printLine, ruleMap),
        )
    }

    @Test
    fun testScanPrintLineWithInvalidLiteralsOfTypeString() {
        val printLine =
            PrintLine(
                Position(),
                SingleValue(
                    Token(Position(), "1", TokenType.STRING),
                ),
            )
        val scanPrintLine = ScanPrintLine(arrayListOf(TokenType.STRING))
        val sca = Sca(listOf(scanPrintLine))
        val ruleMap = ReadScaRulesFile()
        ruleMap.readSCARulesAndStackMap("src/main/resources/testRulesWithNoLiteral.json")
        Assertions.assertEquals(
            "PRINT_LINE with STRING is not allowed at line 1 and column 1\n",
            sca.analyze(printLine, ruleMap),
        )
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
    fun testScanValidSnakeCaseDeclaration() {
        val keyword = Keyword(Modifier.MUTABLE, "let", Position())
        val variable = Variable("is_this_test_valid", Position())
        val type = Type(TokenType.INTEGER, Position())
        val declaration = Declaration(keyword, variable, type, Position())
        val scanIdentifierCase = ScanIdentifierCase()
        val sca = Sca(listOf(scanIdentifierCase))
        val ruleMap = ReadScaRulesFile()
        ruleMap.readSCARulesAndStackMap("src/main/resources/testRulesWithSnakeCase.json")
        Assertions.assertEquals(
            "",
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
    fun testScanReadInputWithDisabledLiteralInRuleWithTypeInteger() {
        val value = SingleValue(Token(Position(), "4", TokenType.INTEGER))
        val variable = Variable("x", Position())
        val assignationReadInput = AssignationReadInput(Position(), variable, value)
        val scanReadInput = ScanReadInput(arrayListOf(TokenType.INTEGER))
        val sca = Sca(listOf(scanReadInput))
        val ruleMap = ReadScaRulesFile()
        ruleMap.readSCARulesAndStackMap("src/main/resources/testRulesWithNoLiteral.json")
        Assertions.assertEquals(
            "ASSIGNATION_READ_INPUT with INTEGER is not allowed at line 1 and column 1\n",
            sca.analyze(assignationReadInput, ruleMap),
        )
    }

    @Test
    fun testScanReadInputWithDisabledLiteralInRuleWithTypeString() {
        val value = SingleValue(Token(Position(), "4", TokenType.STRING))
        val variable = Variable("x", Position())
        val assignationReadInput = AssignationReadInput(Position(), variable, value)
        val scanReadInput = ScanReadInput(arrayListOf(TokenType.STRING))
        val sca = Sca(listOf(scanReadInput))
        val ruleMap = ReadScaRulesFile()
        ruleMap.readSCARulesAndStackMap("src/main/resources/testRulesWithNoLiteral.json")
        Assertions.assertEquals(
            "ASSIGNATION_READ_INPUT with STRING is not allowed at line 1 and column 1\n",
            sca.analyze(assignationReadInput, ruleMap),
        )
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
    fun testScanReadInputWithOperatorAsValue() {
        val operator =
            Operator(
                Token(Position(), "+", TokenType.OPERATOR),
                SingleValue(
                    Token(Position(), "1", TokenType.INTEGER),
                ),
                SingleValue(Token(Position(), "2", TokenType.INTEGER)),
            )
        val variable = Variable("x", Position())
        val assignationReadInput = AssignationReadInput(Position(), variable, operator)
        val scanReadInput = ScanReadInput(arrayListOf(TokenType.STRING))
        val sca = Sca(listOf(scanReadInput))
        val ruleMap = ReadScaRulesFile()
        ruleMap.readSCARulesAndStackMap("src/main/resources/rules.json")
        Assertions.assertEquals(
            "ASSIGNATION_READ_INPUT with expression is not allowed at line 1 and column 1\n",
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

    @Test
    fun testDefaultConfigFile() {
        assertEquals(true, getDefaultBooleanValue("identifier"))
        assertEquals(false, getDefaultBooleanValue("expression"))
        assertEquals(true, getDefaultBooleanValue("literal"))
        assertEquals(false, getDefaultBooleanValue("invalid"))
        assertEquals(ScanCamelCase::class, getDefaultCase()::class)
    }
}
