package modules.parser

import Parser
import components.Position
import components.Token
import components.TokenType
import components.ast.AST
import components.ast.ASTInterface
import error.ParserError
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ParserAST {
    @Test
    fun `test 001 - declaration to AST`(){
        val parser = Parser()
        val position = Position()
        val tokens: List<Token> = listOf(
            Token(position, "let", TokenType.KEYWORD),
            Token(position, "x", TokenType.IDENTIFIER),
            Token(position, ":", TokenType.DECLARATION),
            Token(position, "string", TokenType.STRING),
            Token(position, "", TokenType.SEMICOLON)
        )
        val astExpected: ASTInterface =
            AST(Token(position, ":", TokenType.DECLARATION),
                listOf(
                    AST(Token(position, "x", TokenType.IDENTIFIER)),
                    AST(Token(position, "string", TokenType.STRING))
                ))
        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

    @Test
    fun `test 002 - declaration without keyword`() {
        val parser = Parser()
        val position = Position()
        val tokens: List<Token> = listOf(
            Token(position, "x", TokenType.IDENTIFIER),
            Token(position, ":", TokenType.DECLARATION),
            Token(position, "string", TokenType.STRING),
            Token(position, "", TokenType.SEMICOLON)
        )
        Assertions.assertThrows(ParserError::class.java) {
            parser.parse(tokens)
        }
    }
    @Test
    fun `test 003 - declaration without identifier`() {
        val parser = Parser()
        val position = Position()
        val tokens: List<Token> = listOf(
            Token(position, "let", TokenType.KEYWORD),
            Token(position, ":", TokenType.DECLARATION),
            Token(position, "string", TokenType.STRING),
            Token(position, "", TokenType.SEMICOLON)
        )
        Assertions.assertThrows(ParserError::class.java) {
            parser.parse(tokens)
        }
    }
    @Test
    fun `test 004 - declaration without declaration`() {
        val parser = Parser()
        val position = Position()
        val tokens: List<Token> = listOf(
            Token(position, "let", TokenType.KEYWORD),
            Token(position, "x", TokenType.IDENTIFIER),
            Token(position, "string", TokenType.STRING),
            Token(position, "", TokenType.SEMICOLON)
        )
        Assertions.assertThrows(ParserError::class.java) {
            parser.parse(tokens)
        }
    }
    @Test
    fun `test 004 - declaration without type`() {
        val parser = Parser()
        val position = Position()
        val tokens: List<Token> = listOf(
            Token(position, "let", TokenType.KEYWORD),
            Token(position, "x", TokenType.IDENTIFIER),
            Token(position, ":", TokenType.DECLARATION),
            Token(position, "", TokenType.SEMICOLON)
        )
        Assertions.assertThrows(ParserError::class.java) {
            parser.parse(tokens)
        }
    }
    @Test
    fun `assignation to AST`(){
        val parser = Parser()
        val position = Position()
        val tokens: List<Token> = listOf(
            Token(position, "x", TokenType.IDENTIFIER),
            Token(position, "=", TokenType.ASSIGNATION),
            Token(position, "8", TokenType.VALUE),
            Token(position, "", TokenType.SEMICOLON)
        )
        val astExpected: ASTInterface =
            AST(Token(position, "=", TokenType.ASSIGNATION),
                listOf(
                    AST(Token(position, "x", TokenType.IDENTIFIER)),
                    AST(Token(position, "8", TokenType.VALUE))
                ))
        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

    @Test
    fun `x = 8+3 to AST`(){
        val parser = Parser()
        val position = Position()
        val tokens: List<Token> = listOf(
            Token(position, "x", TokenType.IDENTIFIER),
            Token(position, "=", TokenType.ASSIGNATION),
            Token(position, "8", TokenType.VALUE),
            Token(position, "+", TokenType.OPERATOR),
            Token(position, "3", TokenType.VALUE),
            Token(position, "", TokenType.SEMICOLON)
        )
        val astExpected: ASTInterface =
            AST(Token(position, "=", TokenType.ASSIGNATION),
                listOf(
                    AST(Token(position, "x", TokenType.IDENTIFIER)),
                    AST(Token(position, "+", TokenType.OPERATOR),
                        listOf(
                            AST(Token(position, "8", TokenType.VALUE)),
                            AST(Token(position, "3", TokenType.VALUE)))
                        ))
                )
        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

    @Test
    fun `x = 8+3 mul 2 to AST`(){
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
            Token(position, "", TokenType.SEMICOLON)
        )
        val astExpected: ASTInterface =
            AST(Token(position, "=", TokenType.ASSIGNATION),
                listOf(
                    AST(Token(position, "x", TokenType.IDENTIFIER)),
                    AST(Token(position, "+", TokenType.OPERATOR),
                        listOf(
                            AST(Token(position, "8", TokenType.VALUE)),
                            AST(Token(position, "*", TokenType.OPERATOR),
                                listOf(
                                    AST(Token(position, "3", TokenType.VALUE)),
                                        AST(Token(position, "2", TokenType.VALUE))
                                ))
                        ))
                ))
        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

    @Test
    fun `x = 8 + (3 mul 2)`(){
        val parser = Parser()
        val position = Position()
        val tokens: List<Token> = listOf(
            Token(position, "x", TokenType.IDENTIFIER),
            Token(position, "=", TokenType.ASSIGNATION),
            Token(position, "8", TokenType.VALUE),
            Token(position, "+", TokenType.OPERATOR),
            Token(position, "(", TokenType.PARENTHESIS),
            Token(position, "3", TokenType.VALUE),
            Token(position, "*", TokenType.OPERATOR),
            Token(position, "2", TokenType.VALUE),
            Token(position, ")", TokenType.PARENTHESIS),
            Token(position, "", TokenType.SEMICOLON)
        )
        val astExpected: ASTInterface =
            AST(Token(position, "=", TokenType.ASSIGNATION),
                listOf(
                    AST(Token(position, "x", TokenType.IDENTIFIER)),
                    AST(Token(position, "+", TokenType.OPERATOR),
                        listOf(
                            AST(Token(position, "8", TokenType.VALUE)),
                            AST(Token(position, "*", TokenType.OPERATOR),
                                listOf(
                                    AST(Token(position, "3", TokenType.VALUE)),
                                    AST(Token(position, "2", TokenType.VALUE))
                                ))
                        ))
                ))
        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

    @Test
    fun `let x number = 8`(){
        val parser = Parser()
        val position = Position()
        val tokens: List<Token> = listOf(
            Token(position, "let", TokenType.KEYWORD),
            Token(position, "x", TokenType.IDENTIFIER),
            Token(position, ":", TokenType.DECLARATION),
            Token(position, "number", TokenType.INTEGER),
            Token(position, "=", TokenType.ASSIGNATION),
            Token(position, "8", TokenType.VALUE),
            Token(position, "", TokenType.SEMICOLON)
        )
        val astExpected = AST(
            Token(position, "=", TokenType.ASSIGNATION),
            listOf(
                AST(Token(position, ":", TokenType.DECLARATION),
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
    fun `println(8 + c)`(){
        val parser = Parser()
        val position = Position()
        val tokens: List<Token> = listOf(
            Token(position, "println", TokenType.FUNCTION),
            Token(position, "(", TokenType.PARENTHESIS),
            Token(position, "8", TokenType.VALUE),
            Token(position, "+", TokenType.OPERATOR),
            Token(position, "c", TokenType.IDENTIFIER),
            Token(position, ")", TokenType.PARENTHESIS),
            Token(position, "", TokenType.SEMICOLON)
        )
        val astExpected = AST(
            Token(position, "println", TokenType.FUNCTION),
            listOf(
                AST(Token(position, "+", TokenType.OPERATOR),
                    listOf(
                        AST(Token(position, "8", TokenType.VALUE)),
                        AST(Token(position, "c", TokenType.IDENTIFIER)),
                    )
                )
            )
        )
        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }
}