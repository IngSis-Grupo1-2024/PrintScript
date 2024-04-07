import components.Position
import components.Token
import components.TokenType
import components.ast.AST
import ingsis.formatter.Formatter
import ingsis.formatter.FormatterRule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

class FormatterTest {
    @Test
    fun testRuleClassCreation() {
        val formatterRule = FormatterRule(true, 1)
        assertEquals(true, formatterRule.on)
        assertEquals(1, formatterRule.quantity)
    }

    @Test
    fun testFormatWithSimpleDeclaration() {
        val formatter = Formatter()
        val children = ArrayList<AST>()
        children.add(AST(Token(Position(1, 3, 1, 1, 1, 3), "let", TokenType.KEYWORD)))
        children.add(AST(Token(Position(5, 5, 1, 1, 5, 5), "x", TokenType.IDENTIFIER)))
        children.add(AST(Token(Position(9, 14, 1, 1, 9, 14), "string", TokenType.STRING)))
        val input = AST(Token(Position(8, 8, 1, 1, 8, 8), ":", TokenType.DECLARATION), children)
        val output = formatter.format(input)

        // This is an assert not equals, because the memory position of the two AST is different.
        assertNotEquals(AST(Token(Position(1, 3, 1, 1, 1, 3), "let", TokenType.KEYWORD)), output.getChildren()[0])
        assertEquals(6, output.getToken().getPosition().startOffset)
        assertEquals(6, output.getToken().getPosition().endOffset)
        assertEquals(6, output.getToken().getPosition().startColumn)
        assertEquals(6, output.getToken().getPosition().endColumn)
        assertEquals(8, output.getChildren()[2].getToken().getPosition().startOffset)
        assertEquals(13, output.getChildren()[2].getToken().getPosition().endOffset)
        assertEquals(8, output.getChildren()[2].getToken().getPosition().startColumn)
        assertEquals(13, output.getChildren()[2].getToken().getPosition().endColumn)
    }
}
