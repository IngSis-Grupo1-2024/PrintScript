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
                Token(
                    Position(1, 6, 1, 1, 1, 6),
                    "const",
                    TokenType.KEYWORD,
                ),
                Token(
                    Position(7, 8, 1, 1, 7, 8),
                    "a",
                    TokenType.SYMBOL,
                ),
                Token(
                    Position(8, 9, 1, 1, 8, 9),
                    ":",
                    TokenType.DECLARATION,
                ),
                Token(
                    Position(9, 15, 1, 1, 9, 15),
                    "number",
                    TokenType.TYPE,
                ),
                Token(
                    Position(19, 20, 1, 1, 19, 20),
                    ";",
                    TokenType.DELIMITER,
                ),
            )
        val astExpected: Statement =
            Declaration(
                Keyword(Modifier.IMMUTABLE, "const", position),
                Variable("a", position),
                Type(TokenType.INTEGER, position),
                position,
            )
        assertEquals(listOf(astExpected).toString(), parserV2.parse(tokens).toString())
        val exception = assertThrows<ParserError> { parserV1.parse(tokens) }
        assertEquals("error: keyword not found", exception.message)
        assertEquals(
            Position(1, 6, 1, 1, 1, 6),
            exception.getTokenPosition(),
        )
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
        assertEquals(listOf(astExpected).toString(), parserV2.parse(tokens).toString())
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
        assertEquals(listOf(astExpected).toString(), parserV2.parse(tokens).toString())
        val exception = assertThrows<ParserError> { parserV1.parse(tokens) }
        assertEquals("error: invalid token", exception.message)
    }

    @Test
    fun `test 004 - compound assignation of x as boolean as let`() {
        val tokens =
            listOf(
                Token(position, "let", TokenType.KEYWORD),
                Token(position, "a", TokenType.SYMBOL),
                Token(position, ":", TokenType.DECLARATION),
                Token(position, "number", TokenType.TYPE),
                Token(position, "=", TokenType.ASSIGNATION),
                Token(position, "true", TokenType.SYMBOL),
                Token(position, ";", TokenType.DELIMITER),
            )
        val astExpectedV2: Statement =
            CompoundAssignation(
                position,
                Declaration(
                    Keyword(Modifier.MUTABLE, "let", position),
                    Variable("a", position),
                    Type(TokenType.INTEGER, position),
                    position,
                ),
                SingleValue(Token(position, "true", TokenType.BOOLEAN)),
            )

        val astExpectedV1: Statement =
            CompoundAssignation(
                position,
                Declaration(
                    Keyword(Modifier.MUTABLE, "let", position),
                    Variable("a", position),
                    Type(TokenType.INTEGER, position),
                    position,
                ),
                SingleValue(Token(position, "true", TokenType.IDENTIFIER)),
            )
        assertEquals(listOf(astExpectedV2).toString(), parserV2.parse(tokens).toString())
        assertEquals(listOf(astExpectedV1).toString(), parserV1.parse(tokens).toString())
    }

    @Test
    fun `test 005 - if condition without code block`() {
        val tokens =
            listOf(
                Token(position, "if", TokenType.FUNCTION_KEYWORD),
                Token(position, "(", TokenType.PARENTHESIS),
                Token(position, "8", TokenType.SYMBOL),
                Token(position, "<=", TokenType.OPERATOR),
                Token(position, "9", TokenType.SYMBOL),
                Token(position, ")", TokenType.PARENTHESIS),
                Token(position, "{", TokenType.BRACES),
            )

        val tokensLastBraces: List<Token> =
            listOf(
                Token(position, "}", TokenType.BRACES),
            )
        parserV2.parse(tokens)
        val exception = assertThrows<ParserError> { parserV2.parse(tokensLastBraces) }
        assertEquals(
            "error: there are no statements inside if",
            exception.message,
        )
    }

    @Test
    fun `test 006 - if condition, with code block, and with a simple boolean inside`() {
        val tokens =
            listOf(
                Token(position, "if", TokenType.FUNCTION_KEYWORD),
                Token(position, "(", TokenType.PARENTHESIS),
                Token(position, "true", TokenType.BOOLEAN),
                Token(position, ")", TokenType.PARENTHESIS),
                Token(position, "{", TokenType.BRACES),
            )
        val tokensDeclaration: List<Token> =
            listOf(
                Token(position, "let", TokenType.KEYWORD),
                Token(position, "x", TokenType.SYMBOL),
                Token(position, ":", TokenType.DECLARATION),
                Token(position, "string", TokenType.TYPE),
                Token(position, "", TokenType.DELIMITER),
            )
        val tokensLastBraces: List<Token> =
            listOf(
                Token(position, "}", TokenType.BRACES),
            )
        val astExpected: Statement =
            If(
                SingleValue(Token(position, "true", TokenType.BOOLEAN)),
                Else(emptyList()),
                listOf(
                    Declaration(
                        Keyword(Modifier.MUTABLE, "let", position),
                        Variable("x", position),
                        Type(TokenType.STRING, position),
                        position,
                    ),
                ),
            )

        parserV2.parse(tokens)
        parserV2.parse(tokensDeclaration)
        parserV2.parse(tokensLastBraces).toString()
        assertEquals(astExpected.toString(), parserV2.getIfStatement().toString())
    }

    @Test
    fun `test 007 - if condition, with code block, and with a complex operation`() {
        val tokens =
            listOf(
                Token(position, "if", TokenType.FUNCTION_KEYWORD),
                Token(position, "(", TokenType.PARENTHESIS),
                Token(position, "8", TokenType.SYMBOL),
                Token(position, "<=", TokenType.OPERATOR),
                Token(position, "9", TokenType.SYMBOL),
                Token(position, ")", TokenType.PARENTHESIS),
                Token(position, "{", TokenType.BRACES),
            )
        val tokensDeclaration: List<Token> =
            listOf(
                Token(position, "let", TokenType.KEYWORD),
                Token(position, "x", TokenType.SYMBOL),
                Token(position, ":", TokenType.DECLARATION),
                Token(position, "string", TokenType.TYPE),
                Token(position, "", TokenType.DELIMITER),
            )
        val tokensLastBraces: List<Token> =
            listOf(
                Token(position, "}", TokenType.BRACES),
            )
        val astExpected: Statement =
            If(
                Operator(
                    Token(position, "<=", TokenType.OPERATOR),
                    SingleValue(Token(position, "8", TokenType.INTEGER)),
                    SingleValue(Token(position, "9", TokenType.INTEGER)),
                ),
                Else(emptyList()),
                listOf(
                    Declaration(
                        Keyword(Modifier.MUTABLE, "let", position),
                        Variable("x", position),
                        Type(TokenType.STRING, position),
                        position,
                    ),
                ),
            )
        parserV2.parse(tokens)
        parserV2.parse(tokensDeclaration)
        parserV2.parse(tokensLastBraces)
        assertEquals(astExpected.toString(), parserV2.getIfStatement().toString())
    }

    @Test
    fun `test 008 - else condition without code block`() {
        val tokens =
            listOf(
                Token(position, "else", TokenType.FUNCTION_KEYWORD),
                Token(position, "{", TokenType.BRACES),
            )
        val tokensLastBraces: List<Token> =
            listOf(
                Token(position, "}", TokenType.BRACES),
            )
        parserV2.parse(tokens)
        val exception = assertThrows<ParserError> { parserV2.parse(tokensLastBraces) }
        assertEquals(
            "error: there are no statements inside else",
            exception.message,
        )
    }

    @Test
    fun `test 009 - else condition with code block`() {
        val ifTokens =
            listOf(
                Token(position, "if", TokenType.FUNCTION_KEYWORD),
                Token(position, "(", TokenType.PARENTHESIS),
                Token(position, "true", TokenType.BOOLEAN),
                Token(position, ")", TokenType.PARENTHESIS),
                Token(position, "{", TokenType.BRACES),
            )
        val ifTokensDeclaration: List<Token> =
            listOf(
                Token(position, "let", TokenType.KEYWORD),
                Token(position, "x", TokenType.SYMBOL),
                Token(position, ":", TokenType.DECLARATION),
                Token(position, "string", TokenType.TYPE),
                Token(position, "", TokenType.DELIMITER),
            )
        val ifTokensLastBraces: List<Token> =
            listOf(
                Token(position, "}", TokenType.BRACES),
            )

        val elseTokens =
            listOf(
                Token(position, "else", TokenType.FUNCTION_KEYWORD),
                Token(position, "{", TokenType.BRACES),
            )
        val elseTokensDeclaration: List<Token> =
            listOf(
                Token(position, "let", TokenType.KEYWORD),
                Token(position, "x", TokenType.SYMBOL),
                Token(position, ":", TokenType.DECLARATION),
                Token(position, "string", TokenType.TYPE),
                Token(position, "", TokenType.DELIMITER),
            )
        val elseTokensLastBraces: List<Token> =
            listOf(
                Token(position, "}", TokenType.BRACES),
            )
        val astExpected: Statement =
            If(
                SingleValue(Token(position, "true", TokenType.BOOLEAN)),
                Else(
                    listOf(
                        Declaration(
                            Keyword(Modifier.MUTABLE, "let", position),
                            Variable("x", position),
                            Type(TokenType.STRING, position),
                            position,
                        ),
                    )),
                listOf(
                    Declaration(
                        Keyword(Modifier.MUTABLE, "let", position),
                        Variable("x", position),
                        Type(TokenType.STRING, position),
                        position,
                    ),
                ),
            )
        parserV2.parse(ifTokens)
        parserV2.parse(ifTokensDeclaration)
        parserV2.parse(ifTokensLastBraces)
        parserV2.parse(elseTokens)
        parserV2.parse(elseTokensDeclaration)
        assertEquals(listOf(astExpected).toString(), parserV2.parse(elseTokensLastBraces).toString())
    }
}
