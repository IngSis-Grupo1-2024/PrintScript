package ingsis.lexer

import ingsis.components.Position
import ingsis.components.Token
import ingsis.components.TokenType

class TokenAssignator(
    private val type: TokenType,
    private val validStrings: List<String>,
) {
    fun isInsideValidatedString(string: String): Boolean {
        return string in validStrings
    }

    fun assignToken(
        string: String,
        position: Position,
    ): Token {
        return Token(position, string, type)
    }
}
