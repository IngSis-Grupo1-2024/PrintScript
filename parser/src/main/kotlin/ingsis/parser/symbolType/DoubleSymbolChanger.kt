package ingsis.parser.symbolType

import ingsis.components.Token
import ingsis.components.TokenType

class DoubleSymbolChanger : SymbolChanger {
    override fun canHandle(token: Token): Boolean = checkIfDouble(token)

    override fun changeToken(token: Token): Token = Token(token.getPosition(), token.getValue(), TokenType.DOUBLE)

    private fun checkIfDouble(token: Token): Boolean = token.getValue().contains(".") && (token.getValue().toDoubleOrNull() != null)
}
