package modules.parser

import components.Position
import components.Token
import components.TokenType
import components.ast.AST
import components.ast.ASTInterface
import components.statement.*
import error.ParserError
import ingsis.parser.Parser
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import scan.ScanAssignation
import scan.ScanDeclaration

class ParserTest {
    private val parser = Parser(listOf(ScanDeclaration(), ScanAssignation()))
    private val position = Position()
    @Test
    fun `test 001 - declaration of x as string`() {
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
    fun `test 005 - declaration without type`() {
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
    fun `test 006 - declaration without type and declaration`() {
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

    @Test
    fun `test 007 - assignation to AST`() {
        val tokens: List<Token> =
            listOf(
                Token(position, "x", TokenType.IDENTIFIER),
                Token(position, "=", TokenType.ASSIGNATION),
                Token(position, "8", TokenType.INTEGER),
                Token(position, "", TokenType.SEMICOLON),
            )
        val astExpected: Statement =
            Assignation(
                position,
                Variable("x",position),
                SingleValue(
                    Token(position, "8", TokenType.INTEGER)),
            )
        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

    @Test
    fun `test 008 - 8 plus 3`() {
        val tokens: List<Token> =
            listOf(
                Token(position, "x", TokenType.IDENTIFIER),
                Token(position, "=", TokenType.ASSIGNATION),
                Token(position, "8", TokenType.INTEGER),
                Token(position, "+", TokenType.OPERATOR),
                Token(position, "3", TokenType.INTEGER),
                Token(position, ";", TokenType.SEMICOLON),
            )
        val astExpected: Statement =
            Assignation(
                position,
                Variable("x", position),
                Operator(
                    Token(position, "+", TokenType.OPERATOR),
                    SingleValue(Token(position, "8", TokenType.INTEGER)),
                    SingleValue(Token(position, "3", TokenType.INTEGER))
                )
            )
        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

    @Test
    fun `test 009 - let x number = 8`() {
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
        val astExpected: Statement =
            CompoundAssignation(
                position,
                Declaration(
                    Keyword(Modifier.MUTABLE, "let", position),
                    Variable("x", position),
                    Type("number", position),
                    position
                ),
                SingleValue(
                    Token(position, "8", TokenType.INTEGER)),
            )
        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

    @Test
    fun `test 010 - 8 + 3 mul 2`() {
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
        val astExpected: Statement =
            Assignation(
                position,
                Variable("x", position),
                Operator(
                    Token(position, "+", TokenType.OPERATOR),
                    SingleValue(
                        Token(position, "8", TokenType.INTEGER)
                    ),
                    Operator(
                        Token(position, "*", TokenType.OPERATOR),
                        SingleValue(Token(position, "3", TokenType.INTEGER)),
                        SingleValue(Token(position, "2", TokenType.INTEGER)),
                    )
                )
            )
        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

    @Test
    fun `test 011 - 8 mul 3 + 2`() {
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
                        Token(position, "2", TokenType.INTEGER)
                    )
                )
            )
        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

    @Test
    fun `test 012 - let x number = 8 + 3 mul 2`() {
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
        val astExpected: Statement =
            CompoundAssignation(
                position,
                Declaration(
                    Keyword(Modifier.MUTABLE, "let", position),
                    Variable("x", position),
                    Type("number", position),
                    position
                ),
                Operator(
                    Token(position, "+", TokenType.OPERATOR),
                    Operator(
                        Token(position, "*", TokenType.OPERATOR),
                        SingleValue(Token(position, "8", TokenType.INTEGER)),
                        SingleValue(Token(position, "3", TokenType.INTEGER)),
                    ),
                    SingleValue(
                        Token(position, "2", TokenType.INTEGER)
                    )
                )
            )

        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

    @Test
    fun `test 013 - declaration And Assignation With Semicolon In The Middle Of Value`() {
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
    fun `test 014 - is Calling Println Method without Close Parenthesis`() {
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
    fun `test 015 - is Calling Println Method without semicolon`() {
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
    fun `test 016 - a long assignation 3 div 2 + 3 mul 2`() {
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
        val astExpected: Statement =
            CompoundAssignation(
                position,
                Declaration(
                    Keyword(Modifier.MUTABLE, "let", position),
                    Variable("x", position),
                    Type("number", position),
                    position
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
                    )
                )
            )
        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

    @Test
    fun `test 017 - an assignation with parenthesis`() {
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
        val astExpected: Statement =
            CompoundAssignation(
                position,
                Declaration(
                    Keyword(Modifier.MUTABLE, "let", position),
                    Variable("x", position),
                    Type("number", position),
                    position
                ),
                Operator(
                    Token(position, "/", TokenType.OPERATOR),
                    SingleValue(
                        Token(position, "3", TokenType.INTEGER)),
                    Operator(
                        Token(position, "+", TokenType.OPERATOR),
                        SingleValue(Token(position, "2", TokenType.INTEGER)),
                        SingleValue(Token(position, "3", TokenType.INTEGER))
                    )
            ))

        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

    @Test
    fun `test 018 - an assignation with multiple parenthesis 3 div (2 + (3 div(2+3)))`() {
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
        val astExpected: Statement =
            CompoundAssignation(
                position,
                Declaration(
                    Keyword(Modifier.MUTABLE, "let", position),
                    Variable("x", position),
                    Type("number", position),
                    position
                ),
                Operator(
                    Token(position, "/", TokenType.OPERATOR),
                    SingleValue(
                        Token(position, "3", TokenType.INTEGER)),
                    Operator(
                        Token(position, "+", TokenType.OPERATOR),
                        SingleValue(Token(position, "2", TokenType.INTEGER)),
                        Operator(
                            Token(position, "/", TokenType.OPERATOR),
                            SingleValue(
                                Token(position, "3", TokenType.INTEGER)),
                            Operator(
                                Token(position, "+", TokenType.OPERATOR),
                                SingleValue(Token(position, "2", TokenType.INTEGER)),
                                SingleValue(Token(position, "3", TokenType.INTEGER))
                            )
                    )
            )))

        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

//    @Test
//    fun `test 019 - a print function with identifier`() {
//        val tokens: List<Token> =
//            listOf(
//                Token(position, "println", TokenType.FUNCTION),
//                Token(position, "(", TokenType.PARENTHESIS),
//                Token(position, "c", TokenType.IDENTIFIER),
//                Token(position, ")", TokenType.PARENTHESIS),
//                Token(position, "", TokenType.SEMICOLON),
//            )
//        val astExpected =
//            AST(
//                Token(position, "println", TokenType.FUNCTION),
//                listOf(
//                    AST(Token(position, "c", TokenType.IDENTIFIER)),
//                ),
//            )
//        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
//    }

//    @Test
//    fun `test 020 - a print function with identifier + value`() {
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
//
//    @Test
//    fun `test 021 - a print function with multiple parenthesis`() {
//        val tokens: List<Token> =
//            listOf(
//                Token(position, "println", TokenType.FUNCTION),
//                Token(position, "(", TokenType.PARENTHESIS),
//                Token(position, "8", TokenType.INTEGER),
//                Token(position, "+", TokenType.OPERATOR),
//                Token(position, "(", TokenType.PARENTHESIS),
//                Token(position, "c", TokenType.IDENTIFIER),
//                Token(position, "*", TokenType.OPERATOR),
//                Token(position, "3", TokenType.INTEGER),
//                Token(position, ")", TokenType.PARENTHESIS),
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
//                            AST(
//                                Token(position, "*", TokenType.OPERATOR),
//                                listOf(
//                                    AST(Token(position, "c", TokenType.IDENTIFIER)),
//                                    AST(Token(position, "3", TokenType.INTEGER)),
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
//    fun `test 022 - a print function with one parenthesis missing`() {
//        val tokens: List<Token> =
//            listOf(
//                Token(position, "println", TokenType.FUNCTION),
//                Token(position, "(", TokenType.PARENTHESIS),
//                Token(position, "8", TokenType.INTEGER),
//                Token(position, "+", TokenType.OPERATOR),
//                Token(position, "(", TokenType.PARENTHESIS),
//                Token(position, "c", TokenType.IDENTIFIER),
//                Token(position, "*", TokenType.OPERATOR),
//                Token(position, "3", TokenType.INTEGER),
//                Token(position, ")", TokenType.PARENTHESIS),
//                Token(position, "", TokenType.SEMICOLON),
//            )
//        assertThrows(ParserError::class.java) {
//            parser.parse(tokens)
//        }
//    }

    @Test
    fun `test 023 - assignation without value`() {
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
    fun `test 024 - assignation with an operation without operators`() {
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
    fun `test 025 - code that PrintScript doesnt recognize`() {
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

    @Test
    fun `test 026 - x = 8 + (3 mul 2)`() {
        val tokens: List<Token> =
            listOf(
                Token(position, "x", TokenType.IDENTIFIER),
                Token(position, "=", TokenType.ASSIGNATION),
                Token(position, "8", TokenType.INTEGER),
                Token(position, "+", TokenType.OPERATOR),
                Token(position, "(", TokenType.PARENTHESIS),
                Token(position, "3", TokenType.INTEGER),
                Token(position, "*", TokenType.OPERATOR),
                Token(position, "2", TokenType.INTEGER),
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
                        Token(position, "8", TokenType.INTEGER)
                    ),
                    Operator(
                        Token(position, "*", TokenType.OPERATOR),
                        SingleValue(Token(position, "3", TokenType.INTEGER)),
                        SingleValue(Token(position, "2", TokenType.INTEGER)),
                    )
                )
            )
        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

}
