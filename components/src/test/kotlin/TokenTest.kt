package componentsTest
import components.Position
import components.Token
import components.TokenType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TokenTest {
    @Test
    fun testTokenCreation() {
        val token = Token(Position(), "=", TokenType.ASSIGNATION)
        assertEquals("=", token.getValue())
        assertEquals(TokenType.ASSIGNATION, token.getType())
        assertEquals(Position(), token.getPosition())
    }
}
