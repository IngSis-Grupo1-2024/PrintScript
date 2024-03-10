package modules.parser

import components.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ParserDeclaration {
    @Test
    fun `declaration of x as string`() {
        val parser = Parser()
        val position = Position()
        val tokens: List<Token> = listOf(
            Token(position, "", TokenType.LET_KEYWORD),
            Token(position, "x", TokenType.IDENTIFIER),
            Token(position, ":", TokenType.ASSIGNATION),
            Token(position, "", TokenType.STRING_TYPE),
            Token(position, "", TokenType.SEMICOLON),
        )
        val astExpected: ASTInterface =
            AST(
                Token(position, "", TokenType.SEMICOLON),
                    AST(Token(position, "", TokenType.LET_KEYWORD),
                        AST(Token(position, ":", TokenType.ASSIGNATION),
                            AST(Token(position, "x", TokenType.IDENTIFIER), null, null),
                            AST(Token(position, "x", TokenType.STRING_TYPE), null, null)),
                    null),
                null)
        println(parser.parse(tokens))
        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }
}