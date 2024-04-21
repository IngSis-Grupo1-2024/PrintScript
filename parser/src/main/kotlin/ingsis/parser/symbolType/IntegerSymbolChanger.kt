package ingsis.parser.symbolType

import ingsis.components.Token
import ingsis.components.TokenType

class IntegerSymbolChanger : SymbolChanger {
    override fun canHandle(token: Token): Boolean = checkIfNumber(token)

    override fun changeToken(token: Token): Token = Token(token.getPosition(), token.getValue(), TokenType.INTEGER)

    private fun checkIfNumber(token: Token): Boolean = token.getValue().toIntOrNull() != null
}
