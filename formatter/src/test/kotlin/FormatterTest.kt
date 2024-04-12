//
// import components.Position
// import components.Token
// import components.TokenType
// import components.statement.*
// import components.statement.Function
// import ingsis.formatter.Formatter
// import org.junit.jupiter.api.Assertions.assertEquals
// import org.junit.jupiter.api.Test
// import scan.ScanAssignation
// import scan.ScanCompoundAssignation
// import scan.ScanDeclaration
// import scan.ScanFunction
// import utils.FormatterRule
//
class FormatterTest {
//    @Test
//    fun testRuleClassCreation() {
//        val formatterRule = FormatterRule(true, 1)
//        assertEquals(true, formatterRule.on)
//        assertEquals(1, formatterRule.quantity)
//    }
//
//    @Test
//    fun testFormatWithSimpleDeclaration() {
//        val scanners = listOf(ScanDeclaration())
//        val formatter = Formatter(scanners)
//        val keyword = Keyword(Modifier.MUTABLE, "let", Position())
//        val variable = Variable("x", Position(5, 5, 1, 1, 5, 5))
//        val type = Type("number", Position(14, 14, 1, 1, 14, 14))
//        val position = Position(8, 8, 1, 1, 8, 8)
//        val declaration = Declaration(keyword, variable, type, position)
//        formatter.format(declaration, "src/test/kotlin/test.txt")
//    }
//
//    @Test
//    fun testFormatWithSimpleAssignation() {
//        val scanners = listOf(ScanAssignation())
//        val formatter = Formatter(scanners)
//        val variable = Variable("x", Position(1, 1, 1, 1, 1, 1))
//        val token = Token(Position(3,3,1,1,3,3), "4", TokenType.INTEGER)
//        val value = SingleValue(token)
//        val position = Position(2, 2, 1, 1, 2, 2)
//        val assignation = Assignation(variable, value, position)
//        formatter.format(assignation, "src/test/kotlin/test.txt")
//    }
//
//    @Test
//    fun testFormatWithSimpleAssignationAndOperatorAsValue() {
//        val scanners = listOf(ScanAssignation())
//        val formatter = Formatter(scanners)
//        val variable = Variable("x", Position(1, 1, 1, 1, 1, 1))
//        val four = Token(Position(3,3,1,1,3,3), "4", TokenType.INTEGER)
//        val plus = Token(Position(4,4,1,1,4,4), "+", TokenType.INTEGER)
//        val five = Token(Position(5,5,1,1,5,5), "5", TokenType.INTEGER)
//        val value = Operator(plus, Operator(four), Operator(five))
//        val position = Position(2, 2, 1, 1, 2, 2)
//        val assignation = Assignation(variable, value, position)
//        formatter.format(assignation, "src/test/kotlin/test.txt")
//    }
//
//    @Test
//    fun testFormatWithCompoundAssignation() {
//        val scanners = listOf(ScanCompoundAssignation())
//        val formatter = Formatter(scanners)
//        val int = Token(Position(16,16,1,1,1,1), "4", TokenType.INTEGER)
//        val value = SingleValue(int)
//        val keyword = Keyword(Modifier.MUTABLE, "let", Position())
//        val variable = Variable("x", Position(5, 5, 1, 1, 5, 5))
//        val type = Type("number", Position(14, 14, 1, 1, 14, 14))
//        val declarationPosition = Position(8, 8, 1, 1, 8, 8)
//        val declaration = Declaration(keyword, variable, type, declarationPosition)
//        val assignationPosition = Position(15,15,1,1,14,14)
//        val compoundAssignation = CompoundAssignation(declaration, value, assignationPosition)
//        formatter.format(compoundAssignation, "src/test/kotlin/test.txt")
//    }
//
//    @Test
//    fun testFormatWithPrintFunction(){
//        val scanners = listOf(ScanFunction())
//        val formatter = Formatter(scanners)
//        val token = Token(Position(8,8,1,1,8,8), "4", TokenType.INTEGER)
//        val value = SingleValue(token)
//        val function = Function(Token(Position(0, 6, 1, 1, 0, 6), "println", TokenType.FUNCTION), value)
//        formatter.format(function, "src/test/kotlin/test.txt")
//    }
//
//
//
}
