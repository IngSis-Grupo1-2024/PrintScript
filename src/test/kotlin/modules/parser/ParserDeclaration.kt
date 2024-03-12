package modules.parser

import components.Position
import components.Token
import components.TokenType
import components.ast.AST
import components.ast.ASTInterface
import kotlin.test.Test
import kotlin.test.assertEquals

class ParserDeclaration {
    @Test
    fun `declaration of x as string`() {
        val parser = Parser()
        val position = Position()
        val tokens: List<Token> = listOf(
            Token(position, "let", TokenType.KEYWORD),
            Token(position, "x", TokenType.IDENTIFIER),
            Token(position, ":", TokenType.ASSIGNATION),
            Token(position, "string", TokenType.TYPE),
            Token(position, "", TokenType.SEMICOLON),
        )
        val astExpected: ASTInterface =
            AST(
                Token(position, "", TokenType.SEMICOLON),
                    AST(Token(position, "let", TokenType.KEYWORD),
                        AST(Token(position, ":", TokenType.ASSIGNATION),
                            AST(Token(position, "x", TokenType.IDENTIFIER)),
                            AST(Token(position, "string", TokenType.TYPE))),
                    null),
                null)
        println(parser.parse(tokens))
        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }
    @Test
    fun `8 plus 3`() {
        val parser = Parser()
        val position = Position()
        val tokens: List<Token> = listOf(
            Token(position, "8", TokenType.VALUE),
            Token(position, "+", TokenType.OPERATOR),
            Token(position, "3", TokenType.VALUE),
        )
        val astExpected: ASTInterface =
            AST(
                Token(position, "+", TokenType.OPERATOR),
                AST(Token(position, "8", TokenType.VALUE)),
                AST(Token(position, "3", TokenType.VALUE))
            )
        println(parser.parse(tokens))
        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }
    @Test
    fun `x = 8 plus 3`() {
        val parser = Parser()
        val position = Position()
        val tokens: List<Token> = listOf(
            Token(position, "8", TokenType.VALUE),
            Token(position, "+", TokenType.OPERATOR),
            Token(position, "3", TokenType.VALUE),
        )
        val astExpected: ASTInterface =
            AST(
                Token(position, "+", TokenType.OPERATOR),
                AST(Token(position, "8", TokenType.VALUE)),
                AST(Token(position, "3", TokenType.VALUE))
            )
        println(parser.parse(tokens))
        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }
}