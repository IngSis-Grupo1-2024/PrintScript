package ingsis.parser.symbolType

import ingsis.components.Token
import ingsis.components.TokenType

class BooleanSymbolChanger: SymbolChanger {
    override fun canHandle(token: Token): Boolean =
        token.getValue() == "true" || token.getValue() == "false"

    override fun changeToken(token: Token): Token =
        Token(token.getPosition(), token.getValue(), TokenType.BOOLEAN)

}