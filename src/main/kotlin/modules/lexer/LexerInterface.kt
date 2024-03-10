package modules.lexer

import components.Position
import components.Token

interface LexerInterface {

    val position: Position

    fun tokenize(input: String): List<Token>

    fun tokenize(input: String, start: Int, end: Int): List<Token>

    fun tokenize(input: String, start: Int, end: Int, line: Int, column: Int): List<Token>

    fun tokenize(input: String, start: Int, end: Int, line: Int, column: Int, offset: Int): List<Token>
}