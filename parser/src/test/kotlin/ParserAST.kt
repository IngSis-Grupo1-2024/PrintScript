package modules.parser

import components.Position
import components.Token
import components.TokenType
import components.statement.*
import error.ParserError
import ingsis.parser.Parser
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import scan.ScanAssignation
import scan.ScanDeclaration

class ParserAST {
    private val parser = Parser(listOf(ScanDeclaration(), ScanAssignation()))
    private val position = Position()
    @Test
    fun `test 001 - declaration to AST`() {
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
    fun `test 008 - x = 8+3 to AST`() {
        val tokens: List<Token> =
            listOf(
                Token(position, "x", TokenType.IDENTIFIER),
                Token(position, "=", TokenType.ASSIGNATION),
                Token(position, "8", TokenType.INTEGER),
                Token(position, "+", TokenType.OPERATOR),
                Token(position, "3", TokenType.INTEGER),
                Token(position, "", TokenType.SEMICOLON),
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
    fun `x = 8+3 mul 2 to AST`() {
        val tokens: List<Token> =
            listOf(
                Token(position, "x", TokenType.IDENTIFIER),
                Token(position, "=", TokenType.ASSIGNATION),
                Token(position, "8", TokenType.INTEGER),
                Token(position, "+", TokenType.OPERATOR),
                Token(position, "3", TokenType.INTEGER),
                Token(position, "*", TokenType.OPERATOR),
                Token(position, "2", TokenType.INTEGER),
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

    @Test
    fun `x = 8 + (3 mul 2)`() {
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

    @Test
    fun `let x number = 8`() {
        val tokens: List<Token> =
            listOf(
                Token(position, "let", TokenType.KEYWORD),
                Token(position, "x", TokenType.IDENTIFIER),
                Token(position, ":", TokenType.DECLARATION),
                Token(position, "number", TokenType.TYPE),
                Token(position, "=", TokenType.ASSIGNATION),
                Token(position, "8", TokenType.INTEGER),
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
                SingleValue(
                    Token(position, "8", TokenType.INTEGER)),
            )
        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
    }

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
