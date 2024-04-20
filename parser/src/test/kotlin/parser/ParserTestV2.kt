package parser

import ingsis.components.Position
import ingsis.components.Token
import ingsis.components.TokenType
import ingsis.components.statement.*
import ingsis.parser.PrintScriptParser
import ingsis.parser.error.ParserError
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ParserTestV2 {
    private val parserV2 = PrintScriptParser.createParser("VERSION_2")
    private val parserV1 = PrintScriptParser.createParser("VERSION_1")
    private val position = Position()

    @Test
    fun `test 001 - declaration of x as number as const`() {
        val tokens =
            listOf(
                Token(position, "const", TokenType.KEYWORD),
                Token(position, "a", TokenType.SYMBOL),
                Token(position, ":", TokenType.DECLARATION),
                Token(position, "number", TokenType.TYPE),
                Token(position, ";", TokenType.DELIMITER),
            )
        val astExpected: Statement =
            Declaration(
                Keyword(Modifier.IMMUTABLE, "const", position),
                Variable("a", position),
                Type(TokenType.INTEGER, position),
                position,
            )
        assertEquals(astExpected.toString(), parserV2.parse(tokens).toString())
        val exception = assertThrows<ParserError> { parserV1.parse(tokens) }
        assertEquals("error: keyword not found", exception.message)
    }

    @Test
    fun `test 002 - declaration of x as boolean as const`() {
        val tokens =
            listOf(
                Token(position, "const", TokenType.KEYWORD),
                Token(position, "a", TokenType.SYMBOL),
                Token(position, ":", TokenType.DECLARATION),
                Token(position, "boolean", TokenType.TYPE),
                Token(position, ";", TokenType.DELIMITER),
            )
        val astExpected: Statement =
            Declaration(
                Keyword(Modifier.IMMUTABLE, "const", position),
                Variable("a", position),
                Type(TokenType.BOOLEAN, position),
                position,
            )
        assertEquals(astExpected.toString(), parserV2.parse(tokens).toString())
        val exception = assertThrows<ParserError> { parserV1.parse(tokens) }
        assertEquals("error: keyword not found", exception.message)
    }

    @Test
    fun `test 003 - declaration of x as boolean as let`() {
        val tokens =
            listOf(
                Token(position, "let", TokenType.KEYWORD),
                Token(position, "a", TokenType.SYMBOL),
                Token(position, ":", TokenType.DECLARATION),
                Token(position, "boolean", TokenType.TYPE),
                Token(position, ";", TokenType.DELIMITER),
            )
        val astExpected: Statement =
            Declaration(
                Keyword(Modifier.MUTABLE, "let", position),
                Variable("a", position),
                Type(TokenType.BOOLEAN, position),
                position,
            )
        assertEquals(astExpected.toString(), parserV2.parse(tokens).toString())
        val exception = assertThrows<ParserError> { parserV1.parse(tokens) }
        assertEquals("error: invalid token", exception.message)
    }
}
