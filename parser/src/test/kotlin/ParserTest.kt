package modules.parser

import components.Position
import components.Token
import components.TokenType
import components.ast.AST
import components.ast.ASTInterface
import error.ParserError
import ingsis.parser.Parser
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ParserTest {
    @Test
    fun `test 001 - declaration of x as string`() {
        val parser = Parser()
        val position = Position()
        val tokens: List<Token> =
            listOf(
                Token(position, "let", TokenType.KEYWORD),
                Token(position, "x", TokenType.IDENTIFIER),
                Token(position, ":", TokenType.DECLARATION),
                Token(position, "string", TokenType.TYPE),
                Token(position, "", TokenType.SEMICOLON),
            )
        val astExpected: ASTInterface =
            AST(
                Token(position, ":", TokenType.DECLARATION),
                listOf(
                    AST(Token(position, "let", TokenType.KEYWORD)),
                    AST(Token(position, "x", TokenType.IDENTIFIER)),
                    AST(Token(position, "string", TokenType.TYPE)),
                ),
            )
        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

    @Test
    fun `test 002 - 8 plus 3`() {
        val parser = Parser()
        val position = Position()
        val tokens: List<Token> =
            listOf(
                Token(position, "x", TokenType.IDENTIFIER),
                Token(position, "=", TokenType.ASSIGNATION),
                Token(position, "8", TokenType.INTEGER),
                Token(position, "+", TokenType.OPERATOR),
                Token(position, "3", TokenType.INTEGER),
                Token(position, ";", TokenType.SEMICOLON),
            )
        val astExpected: ASTInterface =
            AST(
                Token(position, "=", TokenType.ASSIGNATION),
                listOf(
                    AST(Token(position, "x", TokenType.IDENTIFIER)),
                    AST(
                        Token(position, "+", TokenType.OPERATOR),
                        listOf(
                            AST(Token(position, "8", TokenType.INTEGER)),
                            AST(Token(position, "3", TokenType.INTEGER)),
                        ),
                    ),
                ),
            )
        println(parser.parse(tokens))
        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

    @Test
    fun `test 003 - x = 8 plus 3`() {
        val parser = Parser()
        val position = Position()
        val tokens: List<Token> =
            listOf(
                Token(position, "x", TokenType.IDENTIFIER),
                Token(position, "=", TokenType.ASSIGNATION),
                Token(position, "8", TokenType.INTEGER),
                Token(position, "+", TokenType.OPERATOR),
                Token(position, "3", TokenType.INTEGER),
                Token(position, ";", TokenType.SEMICOLON),
            )
        val astExpected: ASTInterface =
            AST(
                Token(position, "=", TokenType.ASSIGNATION),
                listOf(
                    AST(Token(position, "x", TokenType.IDENTIFIER)),
                    AST(
                        Token(position, "+", TokenType.OPERATOR),
                        listOf(
                            AST(Token(position, "8", TokenType.INTEGER)),
                            AST(Token(position, "3", TokenType.INTEGER)),
                        ),
                    ),
                ),
            )
        println(parser.parse(tokens))
        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

    @Test
    fun `test 004 - let x number = 8`() {
        val parser = Parser()
        val position = Position()
        val tokens: List<Token> =
            listOf(
                Token(position, "let", TokenType.KEYWORD),
                Token(position, "x", TokenType.IDENTIFIER),
                Token(position, ":", TokenType.DECLARATION),
                Token(position, "number", TokenType.TYPE),
                Token(position, "=", TokenType.ASSIGNATION),
                Token(position, "8", TokenType.INTEGER),
                Token(position, ";", TokenType.SEMICOLON),
            )
        val astExpected: ASTInterface =
            AST(
                Token(position, "=", TokenType.ASSIGNATION),
                listOf(
                    AST(
                        Token(position, ":", TokenType.DECLARATION),
                        listOf(
                            AST(Token(position, "let", TokenType.KEYWORD)),
                            AST(Token(position, "x", TokenType.IDENTIFIER)),
                            AST(Token(position, "number", TokenType.TYPE)),
                        ),
                    ),
                    AST(Token(position, "8", TokenType.INTEGER)),
                ),
            )
        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

    @Test
    fun `test 005 - 8 + 3 mul 2`() {
        val parser = Parser()
        val position = Position()
        val tokens: List<Token> =
            listOf(
                Token(position, "x", TokenType.IDENTIFIER),
                Token(position, "=", TokenType.ASSIGNATION),
                Token(position, "8", TokenType.INTEGER),
                Token(position, "+", TokenType.OPERATOR),
                Token(position, "3", TokenType.INTEGER),
                Token(position, "*", TokenType.OPERATOR),
                Token(position, "2", TokenType.INTEGER),
                Token(position, "x", TokenType.SEMICOLON),
            )
        val astExpected: ASTInterface =
            AST(
                Token(position, "=", TokenType.ASSIGNATION),
                listOf(
                    AST(
                        Token(position, "x", TokenType.IDENTIFIER),
                    ),
                    AST(
                        Token(position, "+", TokenType.OPERATOR),
                        listOf(
                            AST(
                                Token(position, "8", TokenType.INTEGER),
                            ),
                            AST(
                                Token(position, "*", TokenType.OPERATOR),
                                listOf(
                                    AST(Token(position, "3", TokenType.INTEGER)),
                                    AST(Token(position, "2", TokenType.INTEGER)),
                                ),
                            ),
                        ),
                    ),
                ),
            )
        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

    @Test
    fun `test 006 - 8 mul 3 + 2`() {
        val parser = Parser()
        val position = Position()
        val tokens: List<Token> =
            listOf(
                Token(position, "x", TokenType.IDENTIFIER),
                Token(position, "=", TokenType.ASSIGNATION),
                Token(position, "8", TokenType.INTEGER),
                Token(position, "*", TokenType.OPERATOR),
                Token(position, "3", TokenType.INTEGER),
                Token(position, "+", TokenType.OPERATOR),
                Token(position, "2", TokenType.INTEGER),
                Token(position, ";", TokenType.SEMICOLON),
            )
        val astExpected: ASTInterface =
            AST(
                Token(position, "=", TokenType.ASSIGNATION),
                listOf(
                    AST(
                        Token(position, "x", TokenType.IDENTIFIER),
                    ),
                    AST(
                        Token(position, "+", TokenType.OPERATOR),
                        listOf(
                            AST(
                                Token(position, "*", TokenType.OPERATOR),
                                listOf(
                                    AST(Token(position, "8", TokenType.INTEGER)),
                                    AST(Token(position, "3", TokenType.INTEGER)),
                                ),
                            ),
                            AST(
                                Token(position, "2", TokenType.INTEGER),
                            ),
                        ),
                    ),
                ),
            )
        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

    @Test
    fun `test 007 - let x number = 8 + 3 mul 2`() {
        val parser = Parser()
        val position = Position()
        val tokens: List<Token> =
            listOf(
                Token(position, "let", TokenType.KEYWORD),
                Token(position, "x", TokenType.IDENTIFIER),
                Token(position, ":", TokenType.DECLARATION),
                Token(position, "number", TokenType.TYPE),
                Token(position, "=", TokenType.ASSIGNATION),
                Token(position, "8", TokenType.INTEGER),
                Token(position, "+", TokenType.OPERATOR),
                Token(position, "3", TokenType.INTEGER),
                Token(position, "*", TokenType.OPERATOR),
                Token(position, "2", TokenType.INTEGER),
                Token(position, "", TokenType.SEMICOLON),
            )
        val astExpected: ASTInterface =
            AST(
                Token(position, "=", TokenType.ASSIGNATION),
                listOf(
                    AST(
                        Token(position, ":", TokenType.DECLARATION),
                        listOf(
                            AST(Token(position, "let", TokenType.KEYWORD)),
                            AST(Token(position, "x", TokenType.IDENTIFIER)),
                            AST(Token(position, "number", TokenType.TYPE)),
                        ),
                    ),
                    AST(
                        Token(position, "+", TokenType.OPERATOR),
                        listOf(
                            AST(
                                Token(position, "8", TokenType.INTEGER),
                            ),
                            AST(
                                Token(position, "*", TokenType.OPERATOR),
                                listOf(
                                    AST(Token(position, "3", TokenType.INTEGER)),
                                    AST(Token(position, "2", TokenType.INTEGER)),
                                ),
                            ),
                        ),
                    ),
                ),
            )

        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

    @Test
    fun `test 008 - declaration And Assignation With Semicolon In The Middle Of Value`() {
        val parser = Parser()
        val position = Position()
        val tokens: List<Token> =
            listOf(
                Token(position, "let", TokenType.KEYWORD),
                Token(position, "x", TokenType.IDENTIFIER),
                Token(position, ":", TokenType.DECLARATION),
                Token(position, "string", TokenType.TYPE),
                Token(position, "=", TokenType.ASSIGNATION),
                Token(position, ";", TokenType.SEMICOLON),
                Token(position, "'hello'", TokenType.STRING),
                Token(position, "+", TokenType.OPERATOR),
                Token(position, "bye", TokenType.STRING),
                Token(position, ";", TokenType.SEMICOLON),
            )
        assertThrows(ParserError::class.java) {
            parser.parse(tokens)
        }
    }

    @Test
    fun `test 009 - is Calling Println Method without Close Parenthesis`() {
        val parser = Parser()
        val position = Position()
        val tokens: List<Token> =
            listOf(
                Token(position, "println", TokenType.FUNCTION),
                Token(position, "(", TokenType.PARENTHESIS),
                Token(position, "'hello'", TokenType.STRING),
                Token(position, "", TokenType.SEMICOLON),
            )
        assertThrows(ParserError::class.java) {
            parser.parse(tokens)
        }
    }

    @Test
    fun `test 010 - is Calling Println Method without semicolon`() {
        val parser = Parser()
        val position = Position()
        val tokens: List<Token> =
            listOf(
                Token(position, "println", TokenType.FUNCTION),
                Token(position, "(", TokenType.PARENTHESIS),
                Token(position, "'hello'", TokenType.STRING),
                Token(position, ")", TokenType.PARENTHESIS),
            )
        assertThrows(ParserError::class.java) {
            parser.parse(tokens)
        }
    }

    @Test
    fun `test 011 - a long assignation`() {
        val parser = Parser()
        val position = Position()
        val tokens: List<Token> =
            listOf(
                Token(position, "let", TokenType.KEYWORD),
                Token(position, "x", TokenType.IDENTIFIER),
                Token(position, ":", TokenType.DECLARATION),
                Token(position, "number", TokenType.TYPE),
                Token(position, "=", TokenType.ASSIGNATION),
                Token(position, "3", TokenType.INTEGER),
                Token(position, "/", TokenType.OPERATOR),
                Token(position, "2", TokenType.INTEGER),
                Token(position, "+", TokenType.OPERATOR),
                Token(position, "3", TokenType.INTEGER),
                Token(position, "*", TokenType.OPERATOR),
                Token(position, "2", TokenType.INTEGER),
                Token(position, "", TokenType.SEMICOLON),
            )
        val astExpected: ASTInterface =
            AST(
                Token(position, "=", TokenType.ASSIGNATION),
                listOf(
                    AST(
                        Token(position, ":", TokenType.DECLARATION),
                        listOf(
                            AST(Token(position, "let", TokenType.KEYWORD)),
                            AST(Token(position, "x", TokenType.IDENTIFIER)),
                            AST(Token(position, "number", TokenType.TYPE)),
                        ),
                    ),
                    AST(
                        Token(position, "+", TokenType.OPERATOR),
                        listOf(
                            AST(
                                Token(position, "/", TokenType.OPERATOR),
                                listOf(
                                    AST(Token(position, "3", TokenType.INTEGER)),
                                    AST(Token(position, "2", TokenType.INTEGER)),
                                ),
                            ),
                            AST(
                                Token(position, "*", TokenType.OPERATOR),
                                listOf(
                                    AST(Token(position, "3", TokenType.INTEGER)),
                                    AST(Token(position, "2", TokenType.INTEGER)),
                                ),
                            ),
                        ),
                    ),
                ),
            )

        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

    @Test
    fun `test 012 - another long assignation`() {
        val parser = Parser()
        val position = Position()
        val tokens: List<Token> =
            listOf(
                Token(position, "let", TokenType.KEYWORD),
                Token(position, "x", TokenType.IDENTIFIER),
                Token(position, ":", TokenType.DECLARATION),
                Token(position, "number", TokenType.TYPE),
                Token(position, "=", TokenType.ASSIGNATION),
                Token(position, "3", TokenType.INTEGER),
                Token(position, "/", TokenType.OPERATOR),
                Token(position, "2", TokenType.INTEGER),
                Token(position, "+", TokenType.OPERATOR),
                Token(position, "3", TokenType.INTEGER),
                Token(position, "*", TokenType.OPERATOR),
                Token(position, "2", TokenType.INTEGER),
                Token(position, "-", TokenType.OPERATOR),
                Token(position, "3", TokenType.INTEGER),
                Token(position, "/", TokenType.OPERATOR),
                Token(position, "2", TokenType.INTEGER),
                Token(position, "+", TokenType.OPERATOR),
                Token(position, "3", TokenType.INTEGER),
                Token(position, "*", TokenType.OPERATOR),
                Token(position, "2", TokenType.INTEGER),
                Token(position, "", TokenType.SEMICOLON),
            )
        val astExpected: ASTInterface =
            AST(
                Token(position, "=", TokenType.ASSIGNATION),
                listOf(
                    AST(
                        Token(position, ":", TokenType.DECLARATION),
                        listOf(
                            AST(Token(position, "let", TokenType.KEYWORD)),
                            AST(Token(position, "x", TokenType.IDENTIFIER)),
                            AST(Token(position, "number", TokenType.TYPE)),
                        ),
                    ),
                    AST(
                        Token(position, "+", TokenType.OPERATOR),
                        listOf(
                            AST(
                                Token(position, "/", TokenType.OPERATOR),
                                listOf(
                                    AST(Token(position, "3", TokenType.INTEGER)),
                                    AST(Token(position, "2", TokenType.INTEGER)),
                                ),
                            ),
                            AST(
                                Token(position, "-", TokenType.OPERATOR),
                                listOf(
                                    AST(
                                        Token(position, "*", TokenType.OPERATOR),
                                        listOf(
                                            AST(Token(position, "3", TokenType.INTEGER)),
                                            AST(Token(position, "2", TokenType.INTEGER)),
                                        ),
                                    ),
                                    AST(
                                        Token(position, "+", TokenType.OPERATOR),
                                        listOf(
                                            AST(
                                                Token(position, "/", TokenType.OPERATOR),
                                                listOf(
                                                    AST(Token(position, "3", TokenType.INTEGER)),
                                                    AST(Token(position, "2", TokenType.INTEGER)),
                                                ),
                                            ),
                                            AST(
                                                Token(position, "*", TokenType.OPERATOR),
                                                listOf(
                                                    AST(Token(position, "3", TokenType.INTEGER)),
                                                    AST(Token(position, "2", TokenType.INTEGER)),
                                                ),
                                            ),
                                        ),
                                    ),
                                ),
                            ),
                        ),
                    ),
                ),
            )
        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

    @Test
    fun `test 013 - an assignation with parenthesis`() {
        val parser = Parser()
        val position = Position()
        val tokens: List<Token> =
            listOf(
                Token(position, "let", TokenType.KEYWORD),
                Token(position, "x", TokenType.IDENTIFIER),
                Token(position, ":", TokenType.DECLARATION),
                Token(position, "number", TokenType.TYPE),
                Token(position, "=", TokenType.ASSIGNATION),
                Token(position, "3", TokenType.INTEGER),
                Token(position, "/", TokenType.OPERATOR),
                Token(position, "(", TokenType.PARENTHESIS),
                Token(position, "2", TokenType.INTEGER),
                Token(position, "+", TokenType.OPERATOR),
                Token(position, "3", TokenType.INTEGER),
                Token(position, ")", TokenType.PARENTHESIS),
                Token(position, "", TokenType.SEMICOLON),
            )
        val astExpected: ASTInterface =
            AST(
                Token(position, "=", TokenType.ASSIGNATION),
                listOf(
                    AST(
                        Token(position, ":", TokenType.DECLARATION),
                        listOf(
                            AST(Token(position, "let", TokenType.KEYWORD)),
                            AST(Token(position, "x", TokenType.IDENTIFIER)),
                            AST(Token(position, "number", TokenType.TYPE)),
                        ),
                    ),
                    AST(
                        Token(position, "/", TokenType.OPERATOR),
                        listOf(
                            AST(Token(position, "3", TokenType.INTEGER)),
                            AST(
                                Token(position, "+", TokenType.OPERATOR),
                                listOf(
                                    AST(Token(position, "2", TokenType.INTEGER)),
                                    AST(Token(position, "3", TokenType.INTEGER)),
                                ),
                            ),
                        ),
                    ),
                ),
            )

        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

    @Test
    fun `test 014 - an assignation with multiple parenthesis`() {
        val parser = Parser()
        val position = Position()
        val tokens: List<Token> =
            listOf(
                Token(position, "let", TokenType.KEYWORD),
                Token(position, "x", TokenType.IDENTIFIER),
                Token(position, ":", TokenType.DECLARATION),
                Token(position, "number", TokenType.TYPE),
                Token(position, "=", TokenType.ASSIGNATION),
                Token(position, "3", TokenType.INTEGER),
                Token(position, "/", TokenType.OPERATOR),
                Token(position, "(", TokenType.PARENTHESIS),
                Token(position, "2", TokenType.INTEGER),
                Token(position, "+", TokenType.OPERATOR),
                Token(position, "(", TokenType.PARENTHESIS),
                Token(position, "3", TokenType.INTEGER),
                Token(position, "/", TokenType.OPERATOR),
                Token(position, "(", TokenType.PARENTHESIS),
                Token(position, "2", TokenType.INTEGER),
                Token(position, "+", TokenType.OPERATOR),
                Token(position, "3", TokenType.INTEGER),
                Token(position, ")", TokenType.PARENTHESIS),
                Token(position, ")", TokenType.PARENTHESIS),
                Token(position, ")", TokenType.PARENTHESIS),
                Token(position, "", TokenType.SEMICOLON),
            )
        val astExpected: ASTInterface =
            AST(
                Token(position, "=", TokenType.ASSIGNATION),
                listOf(
                    AST(
                        Token(position, ":", TokenType.DECLARATION),
                        listOf(
                            AST(Token(position, "let", TokenType.KEYWORD)),
                            AST(Token(position, "x", TokenType.IDENTIFIER)),
                            AST(Token(position, "number", TokenType.TYPE)),
                        ),
                    ),
                    AST(
                        Token(position, "/", TokenType.OPERATOR),
                        listOf(
                            AST(Token(position, "3", TokenType.INTEGER)),
                            AST(
                                Token(position, "+", TokenType.OPERATOR),
                                listOf(
                                    AST(Token(position, "2", TokenType.INTEGER)),
                                    AST(
                                        Token(position, "/", TokenType.OPERATOR),
                                        listOf(
                                            AST(Token(position, "3", TokenType.INTEGER)),
                                            AST(
                                                Token(position, "+", TokenType.OPERATOR),
                                                listOf(
                                                    AST(Token(position, "2", TokenType.INTEGER)),
                                                    AST(Token(position, "3", TokenType.INTEGER)),
                                                ),
                                            ),
                                        ),
                                    ),
                                ),
                            ),
                        ),
                    ),
                ),
            )

        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

    @Test
    fun `test 015 - a print function with identifier`() {
        val parser = Parser()
        val position = Position()
        val tokens: List<Token> =
            listOf(
                Token(position, "println", TokenType.FUNCTION),
                Token(position, "(", TokenType.PARENTHESIS),
                Token(position, "c", TokenType.IDENTIFIER),
                Token(position, ")", TokenType.PARENTHESIS),
                Token(position, "", TokenType.SEMICOLON),
            )
        val astExpected =
            AST(
                Token(position, "println", TokenType.FUNCTION),
                listOf(
                    AST(Token(position, "c", TokenType.IDENTIFIER)),
                ),
            )
        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

    @Test
    fun `test 016 - a print function with identifier + value`() {
        val parser = Parser()
        val position = Position()
        val tokens: List<Token> =
            listOf(
                Token(position, "println", TokenType.FUNCTION),
                Token(position, "(", TokenType.PARENTHESIS),
                Token(position, "8", TokenType.INTEGER),
                Token(position, "+", TokenType.OPERATOR),
                Token(position, "c", TokenType.IDENTIFIER),
                Token(position, ")", TokenType.PARENTHESIS),
                Token(position, "", TokenType.SEMICOLON),
            )
        val astExpected =
            AST(
                Token(position, "println", TokenType.FUNCTION),
                listOf(
                    AST(
                        Token(position, "+", TokenType.OPERATOR),
                        listOf(
                            AST(Token(position, "8", TokenType.INTEGER)),
                            AST(Token(position, "c", TokenType.IDENTIFIER)),
                        ),
                    ),
                ),
            )
        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

    @Test
    fun `test 017 - a print function with multiple parenthesis`() {
        val parser = Parser()
        val position = Position()
        val tokens: List<Token> =
            listOf(
                Token(position, "println", TokenType.FUNCTION),
                Token(position, "(", TokenType.PARENTHESIS),
                Token(position, "8", TokenType.INTEGER),
                Token(position, "+", TokenType.OPERATOR),
                Token(position, "(", TokenType.PARENTHESIS),
                Token(position, "c", TokenType.IDENTIFIER),
                Token(position, "*", TokenType.OPERATOR),
                Token(position, "3", TokenType.INTEGER),
                Token(position, ")", TokenType.PARENTHESIS),
                Token(position, ")", TokenType.PARENTHESIS),
                Token(position, "", TokenType.SEMICOLON),
            )
        val astExpected =
            AST(
                Token(position, "println", TokenType.FUNCTION),
                listOf(
                    AST(
                        Token(position, "+", TokenType.OPERATOR),
                        listOf(
                            AST(Token(position, "8", TokenType.INTEGER)),
                            AST(
                                Token(position, "*", TokenType.OPERATOR),
                                listOf(
                                    AST(Token(position, "c", TokenType.IDENTIFIER)),
                                    AST(Token(position, "3", TokenType.INTEGER)),
                                ),
                            ),
                        ),
                    ),
                ),
            )
        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

    @Test
    fun `test 018 - a print function with one parenthesis missing`() {
        val parser = Parser()
        val position = Position()
        val tokens: List<Token> =
            listOf(
                Token(position, "println", TokenType.FUNCTION),
                Token(position, "(", TokenType.PARENTHESIS),
                Token(position, "8", TokenType.INTEGER),
                Token(position, "+", TokenType.OPERATOR),
                Token(position, "(", TokenType.PARENTHESIS),
                Token(position, "c", TokenType.IDENTIFIER),
                Token(position, "*", TokenType.OPERATOR),
                Token(position, "3", TokenType.INTEGER),
                Token(position, ")", TokenType.PARENTHESIS),
                Token(position, "", TokenType.SEMICOLON),
            )
        assertThrows(ParserError::class.java) {
            parser.parse(tokens)
        }
    }

    @Test
    fun `test 019 - assignation without value`() {
        val parser = Parser()
        val position = Position()
        val tokens: List<Token> =
            listOf(
                Token(position, "let", TokenType.KEYWORD),
                Token(position, "x", TokenType.IDENTIFIER),
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
    fun `test 020 - assignation with an operation without operators`() {
        val parser = Parser()
        val position = Position()
        val tokens: List<Token> =
            listOf(
                Token(position, "let", TokenType.KEYWORD),
                Token(position, "x", TokenType.IDENTIFIER),
                Token(position, ":", TokenType.DECLARATION),
                Token(position, "string", TokenType.TYPE),
                Token(position, "=", TokenType.ASSIGNATION),
                Token(position, "c", TokenType.IDENTIFIER),
                Token(position, "3", TokenType.STRING),
                Token(position, "", TokenType.SEMICOLON),
            )
        assertThrows(ParserError::class.java) {
            parser.parse(tokens)
        }
    }

    @Test
    fun `test 020 - code that PrintScript doesnt recognize`() {
        val parser = Parser()
        val position = Position()
        val tokens: List<Token> =
            listOf(
                Token(position, "sum", TokenType.TYPE),
                Token(position, "(", TokenType.PARENTHESIS),
                Token(position, "c", TokenType.IDENTIFIER),
                Token(position, "+", TokenType.OPERATOR),
                Token(position, "3", TokenType.STRING),
                Token(position, ")", TokenType.PARENTHESIS),
                Token(position, "", TokenType.SEMICOLON),
            )
        assertThrows(ParserError::class.java) {
            parser.parse(tokens)
        }
    }
}
