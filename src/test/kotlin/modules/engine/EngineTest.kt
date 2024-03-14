package modules.engine

import components.Position
import components.Token
import components.TokenType
import components.ast.AST
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class EngineTest {
//    @Test
//    fun testSplitLines() {
//        val lines = "let x:int = 4;\nlet y:int = x;"
//        val main = Engine(lines)
//        val list = main.splitLines()
//        assertEquals(2, list.size)
//        assertEquals("let x:int = 4;", list[0])
//        assertEquals("let y:int = x;", list[1])
//    }
//
//
//    //By testing the parser, we are testing the lexer tokenization connected with the parser
//    @Test
//    fun testParserWithStringDefinition() {
//        val engine = Engine("let x:string;")
//        val position = Position()
//        val astList = listOf(
//            AST(
//                Token(position, ";", TokenType.SEMICOLON),
//                AST(
//                    Token(position, "let", TokenType.KEYWORD),
//                    AST(
//                        Token(position, ":", TokenType.ASSIGNATION),
//                        AST(Token(position, "x", TokenType.IDENTIFIER)),
//                        AST(Token(position, "string", TokenType.TYPE))
//                    ),
//                ),
//            )
//        )
//        assertEquals(astList[0].toString(), engine.parse()[0].toString())
//    }
//
//
//    @Test
//    fun testParserWithNumberWithValueDefinition() {
//        val engine = Engine("let x:number = 8;")
//        val position = Position()
//        val astList = listOf(
//            AST(
//                Token(position, ";", TokenType.SEMICOLON),
//                AST(
//                    Token(position, "let", TokenType.KEYWORD),
//                    AST(
//                        Token(position, "=", TokenType.ASSIGNATION),
//                        left = AST(
//                            Token(position, ":", TokenType.ASSIGNATION),
//                            left = AST(Token(position, "x", TokenType.IDENTIFIER)),
//                            right = AST(Token(position, "number", TokenType.TYPE))
//                        ),
//                        right = AST(
//                            Token(position, "8", TokenType.VALUE)
//                        )
//                    )
//                )
//            )
//        )
//        assertEquals(astList[0].toString(), engine.parse()[0].toString())
//    }
//
//    @Test
//    fun testParserWithNumberWithValueAndOperatorDefinition() {
//        val engine = Engine("let x:number = 8 + 3;")
//        val position = Position()
//        val astList = listOf(
//            AST(
//                Token(position, ";", TokenType.SEMICOLON),
//                AST(
//                    Token(position, "let", TokenType.KEYWORD),
//                    AST(
//                        Token(position, "=", TokenType.ASSIGNATION),
//                        left = AST(
//                            Token(position, ":", TokenType.ASSIGNATION),
//                            left = AST(Token(position, "x", TokenType.IDENTIFIER)),
//                            right = AST(Token(position, "number", TokenType.TYPE))
//                        ),
//                        right = AST(
//                            Token(position, "+", TokenType.OPERATOR),
//                            left = AST(
//                                Token(position, "8", TokenType.VALUE)
//                            ),
//                            right = AST(
//                                Token(position, "3", TokenType.VALUE)
//                            )
//                        ),
//                    )
//                )
//            )
//        )
//        assertEquals(astList[0].toString(), engine.parse()[0].toString())
//    }

}