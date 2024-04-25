
import ingsis.components.Position
import ingsis.components.Token
import ingsis.components.TokenType
import ingsis.components.statement.*
import ingsis.formatter.Formatter
import ingsis.formatter.PrintScriptFormatter
import ingsis.formatter.defaultConfig.getDefaultAssignationSpaces
import ingsis.formatter.defaultConfig.getDefaultDeclarationSpaces
import ingsis.formatter.defaultConfig.getDefaultIfTabs
import ingsis.formatter.defaultConfig.getDefaultPrintlnNewLines
import ingsis.formatter.scan.*
import ingsis.formatter.utils.FormatterRule
import ingsis.formatter.utils.readJsonAndStackMap
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
    fun testFormatWithPrintScriptFormatterCreation() {
        val formatter = PrintScriptFormatter.createFormatter("VERSION_1")
        val keyword = Keyword(Modifier.MUTABLE, "let", Position())
        val variable = Variable("x", Position(5, 5, 1, 1, 5, 5))
        val type = Type(TokenType.INTEGER, Position(14, 14, 1, 1, 14, 14))
        val position = Position(8, 8, 1, 1, 8, 8)
        val declaration = Declaration(keyword, variable, type, position)
        val ruleMap = readJsonAndStackMap("src/main/resources/allFalse.json")
        val result = formatter.format(declaration, ruleMap)
        val expected = "let x : number;\n"
        assertEquals(expected, result)
    }

    @Test
    fun testFormatWithPrintScriptFormatterCreationV2() {
        val formatter = PrintScriptFormatter.createFormatter("VERSION_2")
        val booleanTrue = SingleValue(Token(Position(), "true", TokenType.BOOLEAN))
        val keyword = Keyword(Modifier.MUTABLE, "let", Position())
        val variable = Variable("x", Position(5, 5, 1, 1, 5, 5))
        val type = Type(TokenType.INTEGER, Position(14, 14, 1, 1, 14, 14))
        val position = Position(8, 8, 1, 1, 8, 8)
        val declaration = Declaration(keyword, variable, type, position)
        val token = Token(Position(8, 8, 1, 1, 8, 8), "4", TokenType.INTEGER)
        val value = SingleValue(token)
        val function = PrintLine(Position(0, 6, 1, 1, 0, 6), value)
        val elseStatement = Else(listOf())
        val ifStatement = If(booleanTrue, elseStatement, listOf(declaration, function))
        val expected =
            "if{\n" +
                "\tlet x: number;\n\n\n" +
                "\tprintln(4);\n" +
                "}"
        assertEquals(expected, formatter.format(ifStatement, readJsonAndStackMap("src/main/kotlin/ingsis/formatter/rules/rules.json")))
    }

    @Test
    fun testDefaultConfigFile() {
        assertEquals(1, getDefaultIfTabs())
        assertEquals(1, getDefaultDeclarationSpaces())
        assertEquals(1, getDefaultPrintlnNewLines())
        assertEquals(1, getDefaultAssignationSpaces())
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
        val ruleMap = readJsonAndStackMap("src/main/kotlin/ingsis/formatter/rules/rules.json")
        val result = formatter.format(declaration, ruleMap)
        val expected = "let x: number;\n"
        assertEquals(expected, result)
    }

    @Test
    fun testFormatWithSimpleDeclarationAndAllRulesInFalse() {
        val scanners = listOf(ScanDeclaration())
        val formatter = Formatter(scanners)
        val keyword = Keyword(Modifier.MUTABLE, "let", Position())
        val variable = Variable("x", Position(5, 5, 1, 1, 5, 5))
        val type = Type(TokenType.INTEGER, Position(14, 14, 1, 1, 14, 14))
        val position = Position(8, 8, 1, 1, 8, 8)
        val declaration = Declaration(keyword, variable, type, position)
        val ruleMap = readJsonAndStackMap("src/main/resources/allFalse.json")
        val result = formatter.format(declaration, ruleMap)
        val expected = "let x : number;\n"
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
        val ruleMap = readJsonAndStackMap("src/main/kotlin/ingsis/formatter/rules/rules.json")
        val result = formatter.format(assignation, ruleMap)
        val expected = "x  =  4;\n"
        assertEquals(expected, result)
    }

    @Test
    fun testFormatWithSimpleAssignationAndAllRulesInFalse() {
        val scanners = listOf(ScanAssignation())
        val formatter = Formatter(scanners)
        val variable = Variable("x", Position(1, 1, 1, 1, 1, 1))
        val token = Token(Position(3, 3, 1, 1, 3, 3), "4", TokenType.INTEGER)
        val value = SingleValue(token)
        val position = Position(2, 2, 1, 1, 2, 2)
        val assignation = Assignation(position, variable, value)
        val ruleMap = readJsonAndStackMap("src/main/resources/allFalse.json")
        val result = formatter.format(assignation, ruleMap)
        val expected = "x = 4;\n"
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
        val ruleMap = readJsonAndStackMap("src/main/kotlin/ingsis/formatter/rules/rules.json")
        val result = formatter.format(assignation, ruleMap)
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
        val ruleMap = readJsonAndStackMap("src/main/kotlin/ingsis/formatter/rules/rules.json")
        val result = formatter.format(compoundAssignation, ruleMap)
        val expected = "let x: number  =  4;\n"
        assertEquals(expected, result)
    }

    @Test
    fun testFormatWithCompoundAssignationAndRulesInFalse() {
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
        val ruleMap = readJsonAndStackMap("src/main/resources/allFalse.json")
        val result = formatter.format(compoundAssignation, ruleMap)
        val expected = "let x : number = 4;\n"
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
        val function = PrintLine(Position(0, 6, 1, 1, 0, 6), value)
        val ruleMap = readJsonAndStackMap("src/main/kotlin/ingsis/formatter/rules/rules.json")
        val result = formatter.format(function, ruleMap)
        val expected =
            "\n" +
                "\n" +
                "println(4);" +
                "\n"
        assertEquals(expected, result)
    }

    @Test
    fun testIfWithTabsFromRules() {
        val scanners = listOf(ScanIf())
        val formatter = Formatter(scanners)
        val booleanTrue = SingleValue(Token(Position(), "true", TokenType.BOOLEAN))
        val keyword = Keyword(Modifier.MUTABLE, "let", Position())
        val variable = Variable("x", Position(5, 5, 1, 1, 5, 5))
        val type = Type(TokenType.INTEGER, Position(14, 14, 1, 1, 14, 14))
        val position = Position(8, 8, 1, 1, 8, 8)
        val declaration = Declaration(keyword, variable, type, position)
        val token = Token(Position(8, 8, 1, 1, 8, 8), "4", TokenType.INTEGER)
        val value = SingleValue(token)
        val function = PrintLine(Position(0, 6, 1, 1, 0, 6), value)
        val elseStatement = Else(listOf())
        val ifStatement = If(booleanTrue, elseStatement, listOf(declaration, function))
        val expected =
            "if{\n" +
                "\tlet x: number;\n\n\n" +
                "\tprintln(4);\n" +
                "}"
        assertEquals(expected, formatter.format(ifStatement, readJsonAndStackMap("src/main/kotlin/ingsis/formatter/rules/rules.json")))
    }

    @Test
    fun testCreatePrintScriptFormatterV1() {
        val formaterV1 = PrintScriptFormatter.createFormatter("VERSION_1")
        val scanners = listOf(ScanDeclaration(), ScanAssignation(), ScanPrintLine(), ScanCompoundAssignation())
        val printLineStatement = PrintLine(Position(), SingleValue(Token(Position(), "4", TokenType.INTEGER)))
        assertEquals(
            formaterV1.format(printLineStatement, readJsonAndStackMap("src/main/kotlin/ingsis/formatter/rules/rules.json")),
            Formatter(scanners).format(printLineStatement, readJsonAndStackMap("src/main/kotlin/ingsis/formatter/rules/rules.json")),
        )
    }

    @Test
    fun testCreatePrintScriptFormatterWithInvalidVersion() {
        val formaterV1 = PrintScriptFormatter.createFormatter("VERSION_4")
        val scanners = listOf(ScanDeclaration(), ScanAssignation(), ScanPrintLine(), ScanCompoundAssignation())
        val printLineStatement = PrintLine(Position(), SingleValue(Token(Position(), "4", TokenType.INTEGER)))
        assertEquals(
            formaterV1.format(printLineStatement, readJsonAndStackMap("src/main/kotlin/ingsis/formatter/rules/rules.json")),
            Formatter(scanners).format(printLineStatement, readJsonAndStackMap("src/main/kotlin/ingsis/formatter/rules/rules.json")),
        )
    }

    @Test
    fun testInvalidPathToReadRules() {
        val formaterV1 = PrintScriptFormatter.createFormatter("VERSION_1")
        val printLineStatement = PrintLine(Position(), SingleValue(Token(Position(), "4", TokenType.INTEGER)))
        assertThrows<Exception> {
            formaterV1.format(printLineStatement, readJsonAndStackMap("src/main/kotlin/ingsis/formatter/rules/rules2.json"))
        }
    }
}
