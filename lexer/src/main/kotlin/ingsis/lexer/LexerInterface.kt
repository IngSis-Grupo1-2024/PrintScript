package ingsis.lexer

import ingsis.components.Position
import ingsis.components.Token

interface LexerInterface {
    val position: Position

    fun tokenize(input: String): List<Token>
}
