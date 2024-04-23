package ingsis.parser.scan

import ingsis.components.Token
import ingsis.components.statement.Value

interface ValueScanner {
    fun canHandle(tokens: List<Token>): Boolean

    fun makeValue(tokens: List<Token>): Value
}
