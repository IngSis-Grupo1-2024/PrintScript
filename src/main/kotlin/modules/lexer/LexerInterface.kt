package modules.lexer

import components.Position
import components.Token

interface LexerInterface {

    val position: Position

    fun tokenize(input: String): List<Token>

}