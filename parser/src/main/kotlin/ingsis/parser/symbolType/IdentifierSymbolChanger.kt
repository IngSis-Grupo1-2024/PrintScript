package ingsis.parser.symbolType

import ingsis.components.Token
import ingsis.components.TokenType

class IdentifierSymbolChanger: SymbolChanger {
    private val stringSymbolChanger = StringSymbolChanger()
    private val integerSymbolChanger = IntegerSymbolChanger()
    private val doubleSymbolChanger = DoubleSymbolChanger()
    override fun canHandle(token: Token): Boolean =
        !stringSymbolChanger.canHandle(token) &&
        !integerSymbolChanger.canHandle(token) &&
        !doubleSymbolChanger.canHandle(token)

    override fun changeToken(token: Token): Token =
        Token(token.getPosition(), token.getValue(), TokenType.IDENTIFIER)
}