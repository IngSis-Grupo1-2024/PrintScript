package ingsis.parser.symbolType

import ingsis.components.Token

interface SymbolChanger {
    fun canHandle(token: Token): Boolean

    fun changeToken(token: Token): Token
}
