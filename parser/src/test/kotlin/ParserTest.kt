package modules.parser

import ingsis.components.Position
import ingsis.components.Token
import ingsis.components.TokenType
import ingsis.components.statement.*
import ingsis.parser.PrintScriptParser
import ingsis.parser.error.ParserError
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class ParserTest {
    private val parser = PrintScriptParser.createParser("VERSION_1")
    private val position = Position()

    @Test
    fun `test 001 - declaration of x as string`() {
        val tokens: List<Token> =
            listOf(
                Token(position, "let", TokenType.KEYWORD),
                Token(position, "x", TokenType.SYMBOL),
                Token(position, ":", TokenType.DECLARATION),
                Token(position, "string", TokenType.TYPE),
                Token(position, "", TokenType.SEMICOLON),
            )
        val astExpected: Statement =
            Declaration(
                Keyword(Modifier.MUTABLE, "let", position),
                Variable("x", position),
                Type(TokenType.STRING, position),
                position,
            )
        print(parser.parse(tokens).toString())
        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

    @Test
    fun `test 002 - declaration without keyword`() {
        val tokens: List<Token> =
            listOf(
                Token(position, "x", TokenType.SYMBOL),
                Token(position, ":", TokenType.DECLARATION),
                Token(position, "string", TokenType.TYPE),
                Token(position, "", TokenType.SEMICOLON),
            )
        assertThrows(ParserError::class.java) {
            parser.parse(tokens)
        }
    }

    @Test
    fun `test 003 - declaration without identifier`() {
        val tokens: List<Token> =
            listOf(
                Token(position, "let", TokenType.KEYWORD),
                Token(position, ":", TokenType.DECLARATION),
                Token(position, "string", TokenType.TYPE),
                Token(position, "", TokenType.SEMICOLON),
            )
        assertThrows(ParserError::class.java) {
            parser.parse(tokens)
        }
    }

    @Test
    fun `test 004 - declaration without declaration`() {
        val tokens: List<Token> =
            listOf(
                Token(position, "let", TokenType.KEYWORD),
                Token(position, "x", TokenType.SYMBOL),
                Token(position, "string", TokenType.TYPE),
                Token(position, "", TokenType.SEMICOLON),
            )
        assertThrows(ParserError::class.java) {
            parser.parse(tokens)
        }
    }

    @Test
    fun `test 005 - declaration without type`() {
        val tokens: List<Token> =
            listOf(
                Token(position, "let", TokenType.KEYWORD),
                Token(position, "x", TokenType.SYMBOL),
                Token(position, ":", TokenType.DECLARATION),
                Token(position, "", TokenType.SEMICOLON),
            )
        assertThrows(ParserError::class.java) {
            parser.parse(tokens)
        }
    }

    @Test
    fun `test 006 - declaration without type and declaration`() {
        val tokens: List<Token> =
            listOf(
                Token(position, "let", TokenType.KEYWORD),
                Token(position, "x", TokenType.SYMBOL),
                Token(position, "", TokenType.SEMICOLON),
            )
        assertThrows(ParserError::class.java) {
            parser.parse(tokens)
        }
    }

    @Test
    fun `test 007 - assignation to AST`() {
        val tokens: List<Token> =
            listOf(
                Token(position, "x", TokenType.SYMBOL),
                Token(position, "=", TokenType.ASSIGNATION),
                Token(position, "8", TokenType.SYMBOL),
                Token(position, "", TokenType.SEMICOLON),
            )
        val astExpected: Statement =
            Assignation(
                position,
                Variable("x", position),
                SingleValue(
                    Token(position, "8", TokenType.INTEGER),
                ),
            )
        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

    @Test
    fun `test 008 - 8 plus 3`() {
        val tokens: List<Token> =
            listOf(
                Token(position, "x", TokenType.SYMBOL),
                Token(position, "=", TokenType.ASSIGNATION),
                Token(position, "8", TokenType.SYMBOL),
                Token(position, "+", TokenType.OPERATOR),
                Token(position, "3", TokenType.SYMBOL),
                Token(position, ";", TokenType.SEMICOLON),
            )
        val astExpected: Statement =
            Assignation(
                position,
                Variable("x", position),
                Operator(
                    Token(position, "+", TokenType.OPERATOR),
                    SingleValue(Token(position, "8", TokenType.INTEGER)),
                    SingleValue(Token(position, "3", TokenType.INTEGER)),
                ),
            )
        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

    @Test
    fun `test 009 - let x integer = 8`() {
        val tokens: List<Token> =
            listOf(
                Token(position, "let", TokenType.KEYWORD),
                Token(position, "x", TokenType.SYMBOL),
                Token(position, ":", TokenType.DECLARATION),
                Token(position, "number", TokenType.TYPE),
                Token(position, "=", TokenType.ASSIGNATION),
                Token(position, "8", TokenType.SYMBOL),
                Token(position, ";", TokenType.SEMICOLON),
            )
        val astExpected: Statement =
            CompoundAssignation(
                position,
                Declaration(
                    Keyword(Modifier.MUTABLE, "let", position),
                    Variable("x", position),
                    Type(TokenType.INTEGER, position),
                    position,
                ),
                SingleValue(
                    Token(position, "8", TokenType.INTEGER),
                ),
            )
        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

    @Test
    fun `test 010 - 8 + 3 mul 2`() {
        val tokens: List<Token> =
            listOf(
                Token(position, "x", TokenType.SYMBOL),
                Token(position, "=", TokenType.ASSIGNATION),
                Token(position, "8", TokenType.SYMBOL),
                Token(position, "+", TokenType.OPERATOR),
                Token(position, "3", TokenType.SYMBOL),
                Token(position, "*", TokenType.OPERATOR),
                Token(position, "2", TokenType.SYMBOL),
                Token(position, "x", TokenType.SEMICOLON),
            )
        val astExpected: Statement =
            Assignation(
                position,
                Variable("x", position),
                Operator(
                    Token(position, "+", TokenType.OPERATOR),
                    SingleValue(
                        Token(position, "8", TokenType.INTEGER),
                    ),
                    Operator(
                        Token(position, "*", TokenType.OPERATOR),
                        SingleValue(Token(position, "3", TokenType.INTEGER)),
                        SingleValue(Token(position, "2", TokenType.INTEGER)),
                    ),
                ),
            )
        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

    @Test
    fun `test 011 - 8 mul 3 + 2`() {
        val tokens: List<Token> =
            listOf(
                Token(position, "x", TokenType.SYMBOL),
                Token(position, "=", TokenType.ASSIGNATION),
                Token(position, "8", TokenType.SYMBOL),
                Token(position, "*", TokenType.OPERATOR),
                Token(position, "3", TokenType.SYMBOL),
                Token(position, "+", TokenType.OPERATOR),
                Token(position, "2", TokenType.SYMBOL),
                Token(position, ";", TokenType.SEMICOLON),
            )
        val astExpected: Statement =
            Assignation(
                position,
                Variable("x", position),
                Operator(
                    Token(position, "+", TokenType.OPERATOR),
                    Operator(
                        Token(position, "*", TokenType.OPERATOR),
                        SingleValue(Token(position, "8", TokenType.INTEGER)),
                        SingleValue(Token(position, "3", TokenType.INTEGER)),
                    ),
                    SingleValue(
                        Token(position, "2", TokenType.INTEGER),
                    ),
                ),
            )
        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

    @Test
    fun `test 012 - let x integer = 8 + 3 mul 2`() {
        val tokens: List<Token> =
            listOf(
                Token(position, "let", TokenType.KEYWORD),
                Token(position, "x", TokenType.SYMBOL),
                Token(position, ":", TokenType.DECLARATION),
                Token(position, "number", TokenType.TYPE),
                Token(position, "=", TokenType.ASSIGNATION),
                Token(position, "8", TokenType.SYMBOL),
                Token(position, "+", TokenType.OPERATOR),
                Token(position, "3", TokenType.SYMBOL),
                Token(position, "*", TokenType.OPERATOR),
                Token(position, "2", TokenType.SYMBOL),
                Token(position, "", TokenType.SEMICOLON),
            )
        val astExpected: Statement =
            CompoundAssignation(
                position,
                Declaration(
                    Keyword(Modifier.MUTABLE, "let", position),
                    Variable("x", position),
                    Type(TokenType.INTEGER, position),
                    position,
                ),
                Operator(
                    Token(position, "+", TokenType.OPERATOR),
                    SingleValue(Token(position, "8", TokenType.INTEGER)),
                    Operator(
                        Token(position, "*", TokenType.OPERATOR),
                        SingleValue(Token(position, "3", TokenType.INTEGER)),
                        SingleValue(Token(position, "2", TokenType.INTEGER)),
                    ),
                ),
            )

        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

    @Test
    fun `test 013 - declaration And Assignation With Semicolon In The Middle Of Value`() {
        val tokens: List<Token> =
            listOf(
                Token(position, "let", TokenType.KEYWORD),
                Token(position, "x", TokenType.SYMBOL),
                Token(position, ":", TokenType.DECLARATION),
                Token(position, "string", TokenType.TYPE),
                Token(position, "=", TokenType.ASSIGNATION),
                Token(position, ";", TokenType.SEMICOLON),
                Token(position, "'hello'", TokenType.SYMBOL),
                Token(position, "+", TokenType.OPERATOR),
                Token(position, "'bye'", TokenType.SYMBOL),
                Token(position, ";", TokenType.SEMICOLON),
            )
        assertThrows(ParserError::class.java) {
            parser.parse(tokens)
        }
    }

    @Test
    fun `test 014 - is Calling Println Method without Close Parenthesis`() {
        val tokens: List<Token> =
            listOf(
                Token(position, "println", TokenType.FUNCTION),
                Token(position, "(", TokenType.PARENTHESIS),
                Token(position, "\"hello\"", TokenType.SYMBOL),
                Token(position, "", TokenType.SEMICOLON),
            )
        assertThrows(ParserError::class.java) {
            parser.parse(tokens)
        }
    }

    @Test
    fun `test 015 - is Calling Println Method without semicolon`() {
        val tokens: List<Token> =
            listOf(
                Token(position, "println", TokenType.FUNCTION),
                Token(position, "(", TokenType.PARENTHESIS),
                Token(position, "\"hello\"", TokenType.SYMBOL),
                Token(position, ")", TokenType.PARENTHESIS),
            )
        assertThrows(ParserError::class.java) {
            parser.parse(tokens)
        }
    }

    @Test
    fun `test 016 - a long assignation 3 div 2 + 3 mul 2`() {
        val tokens: List<Token> =
            listOf(
                Token(position, "let", TokenType.KEYWORD),
                Token(position, "x", TokenType.SYMBOL),
                Token(position, ":", TokenType.DECLARATION),
                Token(position, "number", TokenType.TYPE),
                Token(position, "=", TokenType.ASSIGNATION),
                Token(position, "3", TokenType.SYMBOL),
                Token(position, "/", TokenType.OPERATOR),
                Token(position, "2", TokenType.SYMBOL),
                Token(position, "+", TokenType.OPERATOR),
                Token(position, "3", TokenType.SYMBOL),
                Token(position, "*", TokenType.OPERATOR),
                Token(position, "2", TokenType.SYMBOL),
                Token(position, "", TokenType.SEMICOLON),
            )
        val astExpected: Statement =
            CompoundAssignation(
                position,
                Declaration(
                    Keyword(Modifier.MUTABLE, "let", position),
                    Variable("x", position),
                    Type(TokenType.INTEGER, position),
                    position,
                ),
                Operator(
                    Token(position, "+", TokenType.OPERATOR),
                    Operator(
                        Token(position, "/", TokenType.OPERATOR),
                        SingleValue(Token(position, "3", TokenType.INTEGER)),
                        SingleValue(Token(position, "2", TokenType.INTEGER)),
                    ),
                    Operator(
                        Token(position, "*", TokenType.OPERATOR),
                        SingleValue(Token(position, "3", TokenType.INTEGER)),
                        SingleValue(Token(position, "2", TokenType.INTEGER)),
                    ),
                ),
            )
        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

    @Test
    fun `test 017 - an assignation with parenthesis`() {
        val tokens: List<Token> =
            listOf(
                Token(position, "let", TokenType.KEYWORD),
                Token(position, "x", TokenType.SYMBOL),
                Token(position, ":", TokenType.DECLARATION),
                Token(position, "number", TokenType.TYPE),
                Token(position, "=", TokenType.ASSIGNATION),
                Token(position, "3", TokenType.SYMBOL),
                Token(position, "/", TokenType.OPERATOR),
                Token(position, "(", TokenType.PARENTHESIS),
                Token(position, "2", TokenType.SYMBOL),
                Token(position, "+", TokenType.OPERATOR),
                Token(position, "3", TokenType.SYMBOL),
                Token(position, ")", TokenType.PARENTHESIS),
                Token(position, "", TokenType.SEMICOLON),
            )
        val astExpected: Statement =
            CompoundAssignation(
                position,
                Declaration(
                    Keyword(Modifier.MUTABLE, "let", position),
                    Variable("x", position),
                    Type(TokenType.INTEGER, position),
                    position,
                ),
                Operator(
                    Token(position, "/", TokenType.OPERATOR),
                    SingleValue(
                        Token(position, "3", TokenType.INTEGER),
                    ),
                    Operator(
                        Token(position, "+", TokenType.OPERATOR),
                        SingleValue(Token(position, "2", TokenType.INTEGER)),
                        SingleValue(Token(position, "3", TokenType.INTEGER)),
                    ),
                ),
            )

        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

    @Test
    fun `test 018 - an assignation with multiple parenthesis 3 div (2 + (3 div(2+3)))`() {
        val tokens: List<Token> =
            listOf(
                Token(position, "let", TokenType.KEYWORD),
                Token(position, "x", TokenType.SYMBOL),
                Token(position, ":", TokenType.DECLARATION),
                Token(position, "number", TokenType.TYPE),
                Token(position, "=", TokenType.ASSIGNATION),
                Token(position, "3", TokenType.SYMBOL),
                Token(position, "/", TokenType.OPERATOR),
                Token(position, "(", TokenType.PARENTHESIS),
                Token(position, "2", TokenType.SYMBOL),
                Token(position, "+", TokenType.OPERATOR),
                Token(position, "(", TokenType.PARENTHESIS),
                Token(position, "3", TokenType.SYMBOL),
                Token(position, "/", TokenType.OPERATOR),
                Token(position, "(", TokenType.PARENTHESIS),
                Token(position, "2", TokenType.SYMBOL),
                Token(position, "+", TokenType.OPERATOR),
                Token(position, "3", TokenType.SYMBOL),
                Token(position, ")", TokenType.PARENTHESIS),
                Token(position, ")", TokenType.PARENTHESIS),
                Token(position, ")", TokenType.PARENTHESIS),
                Token(position, "", TokenType.SEMICOLON),
            )
        val astExpected: Statement =
            CompoundAssignation(
                position,
                Declaration(
                    Keyword(Modifier.MUTABLE, "let", position),
                    Variable("x", position),
                    Type(TokenType.INTEGER, position),
                    position,
                ),
                Operator(
                    Token(position, "/", TokenType.OPERATOR),
                    SingleValue(
                        Token(position, "3", TokenType.INTEGER),
                    ),
                    Operator(
                        Token(position, "+", TokenType.OPERATOR),
                        SingleValue(Token(position, "2", TokenType.INTEGER)),
                        Operator(
                            Token(position, "/", TokenType.OPERATOR),
                            SingleValue(
                                Token(position, "3", TokenType.INTEGER),
                            ),
                            Operator(
                                Token(position, "+", TokenType.OPERATOR),
                                SingleValue(Token(position, "2", TokenType.INTEGER)),
                                SingleValue(Token(position, "3", TokenType.INTEGER)),
                            ),
                        ),
                    ),
                ),
            )

        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

    @Test
    fun `test 019 - a print function with identifier`() {
        val tokens: List<Token> =
            listOf(
                Token(position, "println", TokenType.FUNCTION),
                Token(position, "(", TokenType.PARENTHESIS),
                Token(position, "c", TokenType.SYMBOL),
                Token(position, ")", TokenType.PARENTHESIS),
                Token(position, "", TokenType.SEMICOLON),
            )
        val astExpected: Statement =
            PrintLine(
                position,
                SingleValue(Token(position, "c", TokenType.IDENTIFIER)),
            )
        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

    @Test
    fun `test 020 - a print function with SYMBOL + value`() {
        val tokens: List<Token> =
            listOf(
                Token(position, "println", TokenType.FUNCTION),
                Token(position, "(", TokenType.PARENTHESIS),
                Token(position, "8", TokenType.SYMBOL),
                Token(position, "+", TokenType.OPERATOR),
                Token(position, "c", TokenType.SYMBOL),
                Token(position, ")", TokenType.PARENTHESIS),
                Token(position, "", TokenType.SEMICOLON),
            )
        val astExpected: Statement =
            PrintLine(
                position,
                Operator(
                    Token(position, "+", TokenType.OPERATOR),
                    SingleValue(Token(position, "8", TokenType.INTEGER)),
                    SingleValue(Token(position, "c", TokenType.IDENTIFIER)),
                ),
            )
        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

    @Test
    fun `test 021 - a print function with multiple parenthesis`() {
        val tokens: List<Token> =
            listOf(
                Token(position, "println", TokenType.FUNCTION),
                Token(position, "(", TokenType.PARENTHESIS),
                Token(position, "8", TokenType.SYMBOL),
                Token(position, "+", TokenType.OPERATOR),
                Token(position, "(", TokenType.PARENTHESIS),
                Token(position, "c", TokenType.SYMBOL),
                Token(position, "*", TokenType.OPERATOR),
                Token(position, "3", TokenType.SYMBOL),
                Token(position, ")", TokenType.PARENTHESIS),
                Token(position, ")", TokenType.PARENTHESIS),
                Token(position, "", TokenType.SEMICOLON),
            )
        val astExpected: Statement =
            PrintLine(
                position,
                Operator(
                    Token(position, "+", TokenType.OPERATOR),
                    SingleValue(Token(position, "8", TokenType.INTEGER)),
                    Operator(
                        Token(position, "*", TokenType.OPERATOR),
                        SingleValue(Token(position, "c", TokenType.IDENTIFIER)),
                        SingleValue(Token(position, "3", TokenType.INTEGER)),
                    ),
                ),
            )
        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

    @Test
    fun `test 022 - a print function with one parenthesis missing`() {
        val tokens: List<Token> =
            listOf(
                Token(position, "println", TokenType.FUNCTION),
                Token(position, "(", TokenType.PARENTHESIS),
                Token(position, "8", TokenType.SYMBOL),
                Token(position, "+", TokenType.OPERATOR),
                Token(position, "(", TokenType.PARENTHESIS),
                Token(position, "c", TokenType.SYMBOL),
                Token(position, "*", TokenType.OPERATOR),
                Token(position, "3", TokenType.SYMBOL),
                Token(position, ")", TokenType.PARENTHESIS),
                Token(position, "", TokenType.SEMICOLON),
            )
        assertThrows(ParserError::class.java) {
            parser.parse(tokens)
        }
    }

    @Test
    fun `test 023 - assignation without value`() {
        val tokens: List<Token> =
            listOf(
                Token(position, "let", TokenType.KEYWORD),
                Token(position, "x", TokenType.SYMBOL),
                Token(position, ":", TokenType.DECLARATION),
                Token(position, "string", TokenType.TYPE),
                Token(position, "=", TokenType.ASSIGNATION),
                Token(position, "", TokenType.SEMICOLON),
            )
        assertThrows(ParserError::class.java) {
            parser.parse(tokens)
        }
    }

    @Test
    fun `test 024 - assignation with an operation without operators`() {
        val tokens: List<Token> =
            listOf(
                Token(position, "let", TokenType.KEYWORD),
                Token(position, "x", TokenType.SYMBOL),
                Token(position, ":", TokenType.DECLARATION),
                Token(position, "string", TokenType.TYPE),
                Token(position, "=", TokenType.ASSIGNATION),
                Token(position, "c", TokenType.SYMBOL),
                Token(position, "\"3\"", TokenType.SYMBOL),
                Token(position, "", TokenType.SEMICOLON),
            )
        assertThrows(ParserError::class.java) {
            parser.parse(tokens)
        }
    }

    @Test
    fun `test 025 - code that PrintScript doesnt recognize`() {
        val tokens: List<Token> =
            listOf(
                Token(position, "sum", TokenType.TYPE),
                Token(position, "(", TokenType.PARENTHESIS),
                Token(position, "c", TokenType.SYMBOL),
                Token(position, "+", TokenType.OPERATOR),
                Token(position, "'3'", TokenType.SYMBOL),
                Token(position, ")", TokenType.PARENTHESIS),
                Token(position, "", TokenType.SEMICOLON),
            )
        assertThrows(ParserError::class.java) {
            parser.parse(tokens)
        }
    }

    @Test
    fun `test 026 - x = 8 + (3 mul 2)`() {
        val tokens: List<Token> =
            listOf(
                Token(position, "x", TokenType.SYMBOL),
                Token(position, "=", TokenType.ASSIGNATION),
                Token(position, "8", TokenType.SYMBOL),
                Token(position, "+", TokenType.OPERATOR),
                Token(position, "(", TokenType.PARENTHESIS),
                Token(position, "\"3\"", TokenType.SYMBOL),
                Token(position, "*", TokenType.OPERATOR),
                Token(position, "2", TokenType.SYMBOL),
                Token(position, ")", TokenType.PARENTHESIS),
                Token(position, "", TokenType.SEMICOLON),
            )
        val astExpected: Statement =
            Assignation(
                position,
                Variable("x", position),
                Operator(
                    Token(position, "+", TokenType.OPERATOR),
                    SingleValue(
                        Token(position, "8", TokenType.INTEGER),
                    ),
                    Operator(
                        Token(position, "*", TokenType.OPERATOR),
                        SingleValue(Token(position, "3", TokenType.STRING)),
                        SingleValue(Token(position, "2", TokenType.INTEGER)),
                    ),
                ),
            )
        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

    @Test
    fun `test 027 - declaration of x as string with equals sign`() {
        val tokens: List<Token> =
            listOf(
                Token(position, "let", TokenType.KEYWORD),
                Token(position, "x", TokenType.SYMBOL),
                Token(position, "=", TokenType.ASSIGNATION),
                Token(position, "string", TokenType.TYPE),
                Token(position, "", TokenType.SEMICOLON),
            )
        assertThrows(ParserError::class.java) {
            parser.parse(tokens)
        }
    }

    @Test
    fun `test 028 - declaration of x as string without declaration sign`() {
        val tokens: List<Token> =
            listOf(
                Token(position, "let", TokenType.KEYWORD),
                Token(position, "x", TokenType.SYMBOL),
                Token(position, "string", TokenType.TYPE),
                Token(position, "=", TokenType.ASSIGNATION),
                Token(position, "", TokenType.SEMICOLON),
            )
        try {
            parser.parse(tokens)
        } catch (e: ParserError) {
            assertEquals(
                "error: to declare a variable, it's expected to do it by 'let <name of the variable>: <type of the variable>'",
                e.localizedMessage,
            )
        }
    }

    @Test
    fun `test 029 - declaration of x as string without declaration sign and type`() {
        val tokens: List<Token> =
            listOf(
                Token(position, "let", TokenType.KEYWORD),
                Token(position, "x", TokenType.SYMBOL),
                Token(position, "=", TokenType.ASSIGNATION),
                Token(position, "8", TokenType.SYMBOL),
                Token(position, "", TokenType.SEMICOLON),
            )
        try {
            parser.parse(tokens)
        } catch (e: ParserError) {
            assertEquals(
                "error: to declare a variable, it's expected to do it by 'let <name of the variable>: <type of the variable>'",
                e.localizedMessage,
            )
        }
    }
}
