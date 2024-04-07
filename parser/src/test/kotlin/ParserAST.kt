package modules.parser

import components.Position
import components.Token
import components.TokenType
import components.statement.*
import error.ParserError
import ingsis.parser.Parser
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import scan.ScanDeclaration

class ParserAST {
    @Test
    fun `test 001 - declaration to AST`() {
        val parser = Parser(listOf(ScanDeclaration()))
        val position = Position()
        val tokens: List<Token> =
            listOf(
                Token(position, "let", TokenType.KEYWORD),
                Token(position, "x", TokenType.IDENTIFIER),
                Token(position, ":", TokenType.DECLARATION),
                Token(position, "string", TokenType.TYPE),
                Token(position, "", TokenType.SEMICOLON),
            )
        val astExpected: Statement =
            Declaration(
                Keyword(Modifier.MUTABLE, "let", position),
                Variable("x", position),
                Type("string", position),
                position
            )
        print(parser.parse(tokens).toString())
        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

    @Test
    fun `test 002 - declaration without keyword`() {
        val parser = Parser(listOf(ScanDeclaration()))
        val position = Position()
        val tokens: List<Token> =
            listOf(
                Token(position, "x", TokenType.IDENTIFIER),
                Token(position, ":", TokenType.DECLARATION),
                Token(position, "string", TokenType.TYPE),
                Token(position, "", TokenType.SEMICOLON),
            )
        Assertions.assertThrows(ParserError::class.java) {
            parser.parse(tokens)
        }
    }

    @Test
    fun `test 003 - declaration without identifier`() {
        val parser = Parser(listOf(ScanDeclaration()))
        val position = Position()
        val tokens: List<Token> =
            listOf(
                Token(position, "let", TokenType.KEYWORD),
                Token(position, ":", TokenType.DECLARATION),
                Token(position, "string", TokenType.TYPE),
                Token(position, "", TokenType.SEMICOLON),
            )
        Assertions.assertThrows(ParserError::class.java) {
            parser.parse(tokens)
        }
    }

    @Test
    fun `test 004 - declaration without declaration`() {
        val parser = Parser(listOf(ScanDeclaration()))
        val position = Position()
        val tokens: List<Token> =
            listOf(
                Token(position, "let", TokenType.KEYWORD),
                Token(position, "x", TokenType.IDENTIFIER),
                Token(position, "string", TokenType.TYPE),
                Token(position, "", TokenType.SEMICOLON),
            )
        Assertions.assertThrows(ParserError::class.java) {
            parser.parse(tokens)
        }
    }

    @Test
    fun `test 004 - declaration without type`() {
        val parser = Parser(listOf(ScanDeclaration()))
        val position = Position()
        val tokens: List<Token> =
            listOf(
                Token(position, "let", TokenType.KEYWORD),
                Token(position, "x", TokenType.IDENTIFIER),
                Token(position, ":", TokenType.DECLARATION),
                Token(position, "", TokenType.SEMICOLON),
            )
        Assertions.assertThrows(ParserError::class.java) {
            parser.parse(tokens)
        }
    }

    @Test
    fun `test 004 - declaration without type and declaration`() {
        val parser = Parser(listOf(ScanDeclaration()))
        val position = Position()
        val tokens: List<Token> =
            listOf(
                Token(position, "let", TokenType.KEYWORD),
                Token(position, "x", TokenType.IDENTIFIER),
                Token(position, "", TokenType.SEMICOLON),
            )
        Assertions.assertThrows(ParserError::class.java) {
            parser.parse(tokens)
        }
    }
//
//    @Test
//    fun `assignation to AST`() {
//        val parser = Parser()
//        val position = Position()
//        val tokens: List<Token> =
//            listOf(
//                Token(position, "x", TokenType.IDENTIFIER),
//                Token(position, "=", TokenType.ASSIGNATION),
//                Token(position, "8", TokenType.INTEGER),
//                Token(position, "", TokenType.SEMICOLON),
//            )
//        val astExpected: ASTInterface =
//            AST(
//                Token(position, "=", TokenType.ASSIGNATION),
//                listOf(
//                    AST(Token(position, "x", TokenType.IDENTIFIER)),
//                    AST(Token(position, "8", TokenType.INTEGER)),
//                ),
//            )
//        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
//    }
//
//    @Test
//    fun `x = 8+3 to AST`() {
//        val parser = Parser()
//        val position = Position()
//        val tokens: List<Token> =
//            listOf(
//                Token(position, "x", TokenType.IDENTIFIER),
//                Token(position, "=", TokenType.ASSIGNATION),
//                Token(position, "8", TokenType.INTEGER),
//                Token(position, "+", TokenType.OPERATOR),
//                Token(position, "3", TokenType.INTEGER),
//                Token(position, "", TokenType.SEMICOLON),
//            )
//        val astExpected: ASTInterface =
//            AST(
//                Token(position, "=", TokenType.ASSIGNATION),
//                listOf(
//                    AST(Token(position, "x", TokenType.IDENTIFIER)),
//                    AST(
//                        Token(position, "+", TokenType.OPERATOR),
//                        listOf(
//                            AST(Token(position, "8", TokenType.INTEGER)),
//                            AST(Token(position, "3", TokenType.INTEGER)),
//                        ),
//                    ),
//                ),
//            )
//        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
//    }
//
//    @Test
//    fun `x = 8+3 mul 2 to AST`() {
//        val parser = Parser()
//        val position = Position()
//        val tokens: List<Token> =
//            listOf(
//                Token(position, "x", TokenType.IDENTIFIER),
//                Token(position, "=", TokenType.ASSIGNATION),
//                Token(position, "8", TokenType.INTEGER),
//                Token(position, "+", TokenType.OPERATOR),
//                Token(position, "3", TokenType.INTEGER),
//                Token(position, "*", TokenType.OPERATOR),
//                Token(position, "2", TokenType.INTEGER),
//                Token(position, "", TokenType.SEMICOLON),
//            )
//        val astExpected: ASTInterface =
//            AST(
//                Token(position, "=", TokenType.ASSIGNATION),
//                listOf(
//                    AST(Token(position, "x", TokenType.IDENTIFIER)),
//                    AST(
//                        Token(position, "+", TokenType.OPERATOR),
//                        listOf(
//                            AST(Token(position, "8", TokenType.INTEGER)),
//                            AST(
//                                Token(position, "*", TokenType.OPERATOR),
//                                listOf(
//                                    AST(Token(position, "3", TokenType.INTEGER)),
//                                    AST(Token(position, "2", TokenType.INTEGER)),
//                                ),
//                            ),
//                        ),
//                    ),
//                ),
//            )
//        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
//    }
//
//    @Test
//    fun `x = 8 + (3 mul 2)`() {
//        val parser = Parser()
//        val position = Position()
//        val tokens: List<Token> =
//            listOf(
//                Token(position, "x", TokenType.IDENTIFIER),
//                Token(position, "=", TokenType.ASSIGNATION),
//                Token(position, "8", TokenType.INTEGER),
//                Token(position, "+", TokenType.OPERATOR),
//                Token(position, "(", TokenType.PARENTHESIS),
//                Token(position, "3", TokenType.INTEGER),
//                Token(position, "*", TokenType.OPERATOR),
//                Token(position, "2", TokenType.INTEGER),
//                Token(position, ")", TokenType.PARENTHESIS),
//                Token(position, "", TokenType.SEMICOLON),
//            )
//        val astExpected: ASTInterface =
//            AST(
//                Token(position, "=", TokenType.ASSIGNATION),
//                listOf(
//                    AST(Token(position, "x", TokenType.IDENTIFIER)),
//                    AST(
//                        Token(position, "+", TokenType.OPERATOR),
//                        listOf(
//                            AST(Token(position, "8", TokenType.INTEGER)),
//                            AST(
//                                Token(position, "*", TokenType.OPERATOR),
//                                listOf(
//                                    AST(Token(position, "3", TokenType.INTEGER)),
//                                    AST(Token(position, "2", TokenType.INTEGER)),
//                                ),
//                            ),
//                        ),
//                    ),
//                ),
//            )
//        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
//    }
//
//    @Test
//    fun `let x number = 8`() {
//        val parser = Parser()
//        val position = Position()
//        val tokens: List<Token> =
//            listOf(
//                Token(position, "let", TokenType.KEYWORD),
//                Token(position, "x", TokenType.IDENTIFIER),
//                Token(position, ":", TokenType.DECLARATION),
//                Token(position, "number", TokenType.TYPE),
//                Token(position, "=", TokenType.ASSIGNATION),
//                Token(position, "8", TokenType.INTEGER),
//                Token(position, "", TokenType.SEMICOLON),
//            )
//        val astExpected =
//            AST(
//                Token(position, "=", TokenType.ASSIGNATION),
//                listOf(
//                    AST(
//                        Token(position, ":", TokenType.DECLARATION),
//                        listOf(
//                            AST(Token(position, "let", TokenType.KEYWORD)),
//                            AST(Token(position, "x", TokenType.IDENTIFIER)),
//                            AST(Token(position, "number", TokenType.TYPE)),
//                        ),
//                    ),
//                    AST(Token(position, "8", TokenType.INTEGER)),
//                ),
//            )
//        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
//    }
//
//    @Test
//    fun `println(8 + c)`() {
//        val parser = Parser()
//        val position = Position()
//        val tokens: List<Token> =
//            listOf(
//                Token(position, "println", TokenType.FUNCTION),
//                Token(position, "(", TokenType.PARENTHESIS),
//                Token(position, "8", TokenType.INTEGER),
//                Token(position, "+", TokenType.OPERATOR),
//                Token(position, "c", TokenType.IDENTIFIER),
//                Token(position, ")", TokenType.PARENTHESIS),
//                Token(position, "", TokenType.SEMICOLON),
//            )
//        val astExpected =
//            AST(
//                Token(position, "println", TokenType.FUNCTION),
//                listOf(
//                    AST(
//                        Token(position, "+", TokenType.OPERATOR),
//                        listOf(
//                            AST(Token(position, "8", TokenType.INTEGER)),
//                            AST(Token(position, "c", TokenType.IDENTIFIER)),
//                        ),
//                    ),
//                ),
//            )
//        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
//    }
}
