package modules.parser

import Parser
import components.Position
import components.Token
import components.TokenType
import components.ast.AST
import components.ast.ASTInterface
import error.ParserError
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


class ParserTest {
    @Test
    fun `test 001 - declaration of x as string`() {
        val parser = Parser()
        val position = Position()
        val tokens: List<Token> = listOf(
            Token(position, "let", TokenType.KEYWORD),
            Token(position, "x", TokenType.IDENTIFIER),
            Token(position, ":", TokenType.DECLARATION),
            Token(position, "string", TokenType.STRING),
            Token(position, "", TokenType.SEMICOLON),
        )
        val astExpected: ASTInterface =
            AST(
                Token(position, ":", TokenType.DECLARATION),
                listOf(
                    AST(Token(position, "x", TokenType.IDENTIFIER)),
                    AST(Token(position, "string", TokenType.STRING))
                )
            )
        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

    @Test
    fun `test 002 - 8 plus 3`() {
        val parser = Parser()
        val position = Position()
        val tokens: List<Token> = listOf(
            Token(position, "x", TokenType.IDENTIFIER),
            Token(position, "=", TokenType.ASSIGNATION),
            Token(position, "8", TokenType.VALUE),
            Token(position, "+", TokenType.OPERATOR),
            Token(position, "3", TokenType.VALUE),
            Token(position, ";", TokenType.SEMICOLON)
        )
        val astExpected: ASTInterface =
            AST(
                Token(position, "=", TokenType.ASSIGNATION),
                listOf(
                    AST(Token(position, "x", TokenType.IDENTIFIER)),
                    AST(
                        Token(position, "+", TokenType.OPERATOR),
                        listOf(
                            AST(Token(position, "8", TokenType.VALUE)),
                            AST(Token(position, "3", TokenType.VALUE))
                        )
                    )
                )
            )
        println(parser.parse(tokens))
        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

    @Test
    fun `test 003 - x = 8 plus 3`() {
        val parser = Parser()
        val position = Position()
        val tokens: List<Token> = listOf(
            Token(position, "x", TokenType.IDENTIFIER),
            Token(position, "=", TokenType.ASSIGNATION),
            Token(position, "8", TokenType.VALUE),
            Token(position, "+", TokenType.OPERATOR),
            Token(position, "3", TokenType.VALUE),
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
                            AST(Token(position, "8", TokenType.VALUE)),
                            AST(Token(position, "3", TokenType.VALUE))
                        )
                    )
                )
            )
        println(parser.parse(tokens))
        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

    @Test
    fun `test 004 - let x number = 8`() {
        val parser = Parser()
        val position = Position()
        val tokens: List<Token> = listOf(
            Token(position, "let", TokenType.KEYWORD),
            Token(position, "x", TokenType.IDENTIFIER),
            Token(position, ":", TokenType.DECLARATION),
            Token(position, "number", TokenType.INTEGER),
            Token(position, "=", TokenType.ASSIGNATION),
            Token(position, "8", TokenType.VALUE),
            Token(position, ";", TokenType.SEMICOLON)
        )
        val astExpected: ASTInterface =
            AST(
                Token(position, "=", TokenType.ASSIGNATION),
                listOf(
                    AST(
                        Token(position, ":", TokenType.DECLARATION),
                        listOf(
                            AST(Token(position, "x", TokenType.IDENTIFIER)),
                            AST(Token(position, "number", TokenType.INTEGER))
                        )
                    ),
                    AST(Token(position, "8", TokenType.VALUE))
                )
            )
        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

    @Test
    fun `test 005 - 8 + 3 mul 2`() {
        val parser = Parser()
        val position = Position()
        val tokens: List<Token> = listOf(
            Token(position, "x", TokenType.IDENTIFIER),
            Token(position, "=", TokenType.ASSIGNATION),
            Token(position, "8", TokenType.VALUE),
            Token(position, "+", TokenType.OPERATOR),
            Token(position, "3", TokenType.VALUE),
            Token(position, "*", TokenType.OPERATOR),
            Token(position, "2", TokenType.VALUE),
            Token(position, "x", TokenType.SEMICOLON)
        )
        val astExpected: ASTInterface =
            AST(
                Token(position, "=", TokenType.ASSIGNATION),
                listOf(
                    AST(
                        Token(position, "x", TokenType.IDENTIFIER)
                    ),

                    AST(
                        Token(position, "+", TokenType.OPERATOR),
                        listOf(
                            AST(
                                Token(position, "8", TokenType.VALUE)
                            ),
                            AST(
                                Token(position, "*", TokenType.OPERATOR),
                                listOf(
                                    AST(Token(position, "3", TokenType.VALUE)),
                                    AST(Token(position, "2", TokenType.VALUE))
                                )
                            )
                        )
                    )
                )
            )
        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

    @Test
    fun `test 006 - 8 mul 3 + 2`() {
        val parser = Parser()
        val position = Position()
        val tokens: List<Token> = listOf(
            Token(position, "x", TokenType.IDENTIFIER),
            Token(position, "=", TokenType.ASSIGNATION),
            Token(position, "8", TokenType.VALUE),
            Token(position, "*", TokenType.OPERATOR),
            Token(position, "3", TokenType.VALUE),
            Token(position, "+", TokenType.OPERATOR),
            Token(position, "2", TokenType.VALUE),
            Token(position, ";", TokenType.SEMICOLON),
        )
        val astExpected: ASTInterface =
            AST(
                Token(position, "=", TokenType.ASSIGNATION),
                listOf(
                    AST(
                        Token(position, "x", TokenType.IDENTIFIER)
                    ),

                    AST(
                        Token(position, "+", TokenType.OPERATOR),
                        listOf(
                            AST(
                                Token(position, "*", TokenType.OPERATOR),
                                listOf(
                                    AST(Token(position, "8", TokenType.VALUE)),
                                    AST(Token(position, "3", TokenType.VALUE))
                                )
                            ),
                            AST(
                                Token(position, "2", TokenType.VALUE)
                            ),
                        )
                    )
                )
            )
        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

    @Test
    fun `test 007 - let x number = 8 + 3 mul 2`() {
        val parser = Parser()
        val position = Position()
        val tokens: List<Token> = listOf(
            Token(position, "let", TokenType.KEYWORD),
            Token(position, "x", TokenType.IDENTIFIER),
            Token(position, ":", TokenType.DECLARATION),
            Token(position, "number", TokenType.INTEGER),
            Token(position, "=", TokenType.ASSIGNATION),
            Token(position, "8", TokenType.VALUE),
            Token(position, "+", TokenType.OPERATOR),
            Token(position, "3", TokenType.VALUE),
            Token(position, "*", TokenType.OPERATOR),
            Token(position, "2", TokenType.VALUE),
            Token(position, "", TokenType.SEMICOLON)
        )
        val astExpected: ASTInterface =
            AST(
                Token(position, "=", TokenType.ASSIGNATION),
                listOf(
                    AST(
                        Token(position, ":", TokenType.DECLARATION),
                        listOf(
                            AST(Token(position, "x", TokenType.IDENTIFIER)),
                            AST(Token(position, "number", TokenType.INTEGER))
                        )
                    ),

                    AST(
                        Token(position, "+", TokenType.OPERATOR),
                        listOf(
                            AST(
                                Token(position, "8", TokenType.VALUE)
                            ),
                            AST(
                                Token(position, "*", TokenType.OPERATOR),
                                listOf(
                                    AST(Token(position, "3", TokenType.VALUE)),
                                    AST(Token(position, "2", TokenType.VALUE))
                                )
                            ),
                        )
                    )
                )
            )

        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }


    @Test
    fun `test 008 - declaration And Assignation With Semicolon In The Middle Of Value`() {
        val parser = Parser()
        val position = Position()
        val tokens: List<Token> = listOf(
            Token(position, "let", TokenType.KEYWORD),
            Token(position, "x", TokenType.IDENTIFIER),
            Token(position, ":", TokenType.DECLARATION),
            Token(position, "string", TokenType.TYPE),
            Token(position, "=", TokenType.ASSIGNATION),
            Token(position, ";", TokenType.SEMICOLON),
            Token(position, "'hello'", TokenType.VALUE),
            Token(position, "+", TokenType.OPERATOR),
            Token(position, "bye", TokenType.VALUE),
            Token(position, ";", TokenType.SEMICOLON)
        )
        assertThrows(ParserError::class.java) {
            parser.parse(tokens)
        }
    }

    @Test
    fun `test 009 - is Calling Println Method without Close Parenthesis`() {
        val parser = Parser()
        val position = Position()
        val tokens: List<Token> = listOf(
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
        val tokens: List<Token> = listOf(
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
        val tokens: List<Token> = listOf(
            Token(position, "let", TokenType.KEYWORD),
            Token(position, "x", TokenType.IDENTIFIER),
            Token(position, ":", TokenType.DECLARATION),
            Token(position, "number", TokenType.INTEGER),
            Token(position, "=", TokenType.ASSIGNATION),
            Token(position, "3", TokenType.VALUE),
            Token(position, "/", TokenType.OPERATOR),
            Token(position, "2", TokenType.VALUE),
            Token(position, "+", TokenType.OPERATOR),
            Token(position, "3", TokenType.VALUE),
            Token(position, "*", TokenType.OPERATOR),
            Token(position, "2", TokenType.VALUE),
            Token(position, "", TokenType.SEMICOLON)
        )
        val astExpected: ASTInterface =
            AST(
                Token(position, "=", TokenType.ASSIGNATION),
                listOf(
                    AST(
                        Token(position, ":", TokenType.DECLARATION),
                        listOf(
                            AST(Token(position, "x", TokenType.IDENTIFIER)),
                            AST(Token(position, "number", TokenType.INTEGER))
                        )
                    ),

                    AST(
                        Token(position, "+", TokenType.OPERATOR),
                        listOf(
                            AST(
                                Token(position, "/", TokenType.OPERATOR),
                                listOf(
                                    AST(Token(position, "3", TokenType.VALUE)),
                                    AST(Token(position, "2", TokenType.VALUE))
                                )
                            ),
                            AST(
                                Token(position, "*", TokenType.OPERATOR),
                                listOf(
                                    AST(Token(position, "3", TokenType.VALUE)),
                                    AST(Token(position, "2", TokenType.VALUE))
                                )
                            ),
                        )
                    )
                )
            )

        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

    @Test
    fun `test 012 - another long assignation`() {
        val parser = Parser()
        val position = Position()
        val tokens: List<Token> = listOf(
            Token(position, "let", TokenType.KEYWORD),
            Token(position, "x", TokenType.IDENTIFIER),
            Token(position, ":", TokenType.DECLARATION),
            Token(position, "number", TokenType.INTEGER),
            Token(position, "=", TokenType.ASSIGNATION),
            Token(position, "3", TokenType.VALUE),
            Token(position, "/", TokenType.OPERATOR),
            Token(position, "2", TokenType.VALUE),
            Token(position, "+", TokenType.OPERATOR),
            Token(position, "3", TokenType.VALUE),
            Token(position, "*", TokenType.OPERATOR),
            Token(position, "2", TokenType.VALUE),
            Token(position, "-", TokenType.OPERATOR),
            Token(position, "3", TokenType.VALUE),
            Token(position, "/", TokenType.OPERATOR),
            Token(position, "2", TokenType.VALUE),
            Token(position, "+", TokenType.OPERATOR),
            Token(position, "3", TokenType.VALUE),
            Token(position, "*", TokenType.OPERATOR),
            Token(position, "2", TokenType.VALUE),
            Token(position, "", TokenType.SEMICOLON)
        )
        val astExpected: ASTInterface =
            AST(
                Token(position, "=", TokenType.ASSIGNATION),
                listOf(
                    AST(
                        Token(position, ":", TokenType.DECLARATION),
                        listOf(
                            AST(Token(position, "x", TokenType.IDENTIFIER)),
                            AST(Token(position, "number", TokenType.INTEGER))
                        )
                    ),
                    AST(
                        Token(position, "-", TokenType.OPERATOR),
                        listOf(
                            AST(
                                Token(position, "+", TokenType.OPERATOR),
                                listOf(
                                    AST(
                                        Token(position, "/", TokenType.OPERATOR),
                                        listOf(
                                            AST(Token(position, "3", TokenType.VALUE)),
                                            AST(Token(position, "2", TokenType.VALUE))
                                        )
                                    ),
                                    AST(
                                        Token(position, "*", TokenType.OPERATOR),
                                        listOf(
                                            AST(Token(position, "3", TokenType.VALUE)),
                                            AST(Token(position, "2", TokenType.VALUE))
                                        )
                                    ),
                                )
                            ),
                            AST(
                                Token(position, "+", TokenType.OPERATOR),
                                listOf(
                                    AST(
                                        Token(position, "/", TokenType.OPERATOR),
                                        listOf(
                                            AST(Token(position, "3", TokenType.VALUE)),
                                            AST(Token(position, "2", TokenType.VALUE))
                                        )
                                    ),
                                    AST(
                                        Token(position, "*", TokenType.OPERATOR),
                                        listOf(
                                            AST(Token(position, "3", TokenType.VALUE)),
                                            AST(Token(position, "2", TokenType.VALUE))
                                        )
                                    ),
                                )
                            )
                        )
                    )
                )
            )

        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

}