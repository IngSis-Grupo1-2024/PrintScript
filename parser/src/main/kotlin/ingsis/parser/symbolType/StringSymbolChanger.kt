package ingsis.parser.symbolType

import ingsis.components.Token
import ingsis.components.TokenType

class StringSymbolChanger : SymbolChanger {
    override fun canHandle(token: Token): Boolean = checkIfString(token) || checkIfChar(token)

    override fun changeToken(token: Token): Token = Token(token.getPosition(), getStringWithoutQuotes(token), TokenType.STRING)

    private fun getStringWithoutQuotes(token: Token): String = token.getValue().substring(1, token.getValue().length - 1)

    private fun checkIfString(token: Token) = token.getValue()[0] == '"' && token.getValue().last() == '"'

    private fun checkIfChar(token: Token): Boolean = token.getValue()[0] == '\'' && token.getValue().last() == '\''
}
