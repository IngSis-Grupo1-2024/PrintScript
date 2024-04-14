import components.Position
import components.Token
import components.TokenType
import ingsis.lexer.Lexer
import ingsis.lexer.TokenAssignator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LexerTest {

    val lexer = Lexer(
        Position(),
        listOf(TokenAssignator(TokenType.KEYWORD, listOf("let")),
            TokenAssignator(TokenType.FUNCTION, listOf("println")),
            TokenAssignator(TokenType.TYPE, listOf("number", "string", "boolean")),
            TokenAssignator(TokenType.OPERATOR, listOf("+", "-", "/", "*")),
            TokenAssignator(TokenType.DELIMITER, listOf(";")),
            TokenAssignator(TokenType.PARENTHESIS, listOf("(", ")")),
            TokenAssignator(TokenType.DECLARATION, listOf(":")),
            TokenAssignator(TokenType.ASSIGNATION, listOf("=")),
            )
    )

    @Test
    fun testTokenizeADeclaration() {
        val input = "let a:string;"
        val tokenList = listOf(Token(Position(1,4,1,1,1,4), "let", TokenType.KEYWORD),
                                Token(Position(5,6,1,1,5,6), "a", TokenType.SYMBOL),
                                Token(Position(6,7,1,1,6,7), ":", TokenType.DECLARATION),
                                Token(Position(7,13,1,1,7,13), "string", TokenType.TYPE),
                                Token(Position(13,14,1,1,13,14), ";", TokenType.DELIMITER),
            )
        val result = lexer.tokenize(input)
        var i = 0
        while (i < result.size) {
            assertEquals(tokenList[i].getType(), result[i].getType())
            assertEquals(tokenList[i].getValue(), result[i].getValue())
            assertEquals(tokenList[i].getPosition(), result[i].getPosition())
            i++
        }
    }

    @Test
    fun testTokenizeAnAssignation() {
        val input = "a = 5;"
        val tokenList = listOf(Token(Position(1,2,1,1,1,2), "a", TokenType.SYMBOL),
                                Token(Position(3,4,1,1,3,4), "=", TokenType.ASSIGNATION),
                                Token(Position(5,6,1,1,5,6), "5", TokenType.SYMBOL),
                                Token(Position(6,7,1,1,6,7), ";", TokenType.DELIMITER),
            )
        val result = lexer.tokenize(input)
        var i = 0
        while (i < result.size) {
            assertEquals(tokenList[i].getType(), result[i].getType())
            assertEquals(tokenList[i].getValue(), result[i].getValue())
            assertEquals(tokenList[i].getPosition(), result[i].getPosition())
            i++
        }
    }

    @Test
    fun testTokenizeAnAssignationWithMultipleSpaces() {
        val input = "a  = 5;"
        val tokenList = listOf(Token(Position(1,2,1,1,1,2), "a", TokenType.SYMBOL),
            Token(Position(4,5,1,1,4,5), "=", TokenType.ASSIGNATION),
            Token(Position(6,7,1,1,6,7), "5", TokenType.SYMBOL),
            Token(Position(7,8,1,1,7,8), ";", TokenType.DELIMITER),
        )
        val result = lexer.tokenize(input)
        var i = 0
        while (i < result.size) {
            assertEquals(tokenList[i].getType(), result[i].getType())
            assertEquals(tokenList[i].getValue(), result[i].getValue())
            assertEquals(tokenList[i].getPosition(), result[i].getPosition())
            i++
        }
    }

    @Test
    fun testTokenizeAnAssignationWithMultipleSpaces2() {
        val input = " a  = 5  ;"
        val tokenList = listOf(Token(Position(2,3,1,1,2,3), "a", TokenType.SYMBOL),
            Token(Position(5,6,1,1,5,6), "=", TokenType.ASSIGNATION),
            Token(Position(7,8,1,1,7,8), "5", TokenType.SYMBOL),
            Token(Position(10,11,1,1,10,11), ";", TokenType.DELIMITER),
        )
        val result = lexer.tokenize(input)
        var i = 0
        while (i < result.size) {
            assertEquals(tokenList[i].getType(), result[i].getType())
            assertEquals(tokenList[i].getValue(), result[i].getValue())
            assertEquals(tokenList[i].getPosition(), result[i].getPosition())
            i++
        }
    }

    @Test
    fun testDeclareAndAssignANumericVariable(){
        val input = "let a:number = 8;"
        val tokenList = listOf(Token(Position(1,4,1,1,1,4), "let", TokenType.KEYWORD),
            Token(Position(5,6,1,1,5,6), "a", TokenType.SYMBOL),
            Token(Position(6,7,1,1,6,7), ":", TokenType.DECLARATION),
            Token(Position(7,13,1,1,7,13), "number", TokenType.TYPE),
            Token(Position(14, 15, 1,1, 14, 15), "=", TokenType.ASSIGNATION),
            Token(Position(16, 17, 1,1, 16, 17), "8", TokenType.SYMBOL),
            Token(Position(17, 18, 1,1, 17, 18), ";", TokenType.DELIMITER),


        )
        val result = lexer.tokenize(input)
        var i = 0
        while (i < result.size) {
            assertEquals(tokenList[i].getType(), result[i].getType())
            assertEquals(tokenList[i].getValue(), result[i].getValue())
            assertEquals(tokenList[i].getPosition(), result[i].getPosition())
            i++
        }
    }

    @Test
    fun testDeclareAndAssignAStringVariable(){
        val input = "let a:string = \"hello\";"
        val tokenList = listOf(Token(Position(1,4,1,1,1,4), "let", TokenType.KEYWORD),
            Token(Position(5,6,1,1,5,6), "a", TokenType.SYMBOL),
            Token(Position(6,7,1,1,6,7), ":", TokenType.DECLARATION),
            Token(Position(7,13,1,1,7,13), "string", TokenType.TYPE),
            Token(Position(14, 15, 1,1, 14, 15), "=", TokenType.ASSIGNATION),
            Token(Position(16, 23, 1,1, 16, 23), "\"hello\"", TokenType.SYMBOL),
            Token(Position(23, 24, 1,1, 23, 24), ";", TokenType.DELIMITER),
            )
        val result = lexer.tokenize(input)
        var i = 0
        while (i < result.size) {
            assertEquals(tokenList[i].getType(), result[i].getType())
            assertEquals(tokenList[i].getValue(), result[i].getValue())
            assertEquals(tokenList[i].getPosition(), result[i].getPosition())
            i++
        }
    }

    @Test
    fun declareAndAssignStringAndNumberOperation() {
        val input = "let a : string = \"hello\" + 8 ;"
        val tokenList = listOf(Token(Position(1,4,1,1,1,4), "let", TokenType.KEYWORD),
            Token(Position(5,6,1,1,5,6), "a", TokenType.SYMBOL),
            Token(Position(7,8,1,1,7,8), ":", TokenType.DECLARATION),
            Token(Position(9,15,1,1,9,15), "string", TokenType.TYPE),
            Token(Position(16, 17, 1,1, 16, 17), "=", TokenType.ASSIGNATION),
            Token(Position(18, 25, 1,1, 18, 25), "\"hello\"", TokenType.SYMBOL),
            Token(Position(26, 27, 1,1, 26, 27), "+", TokenType.OPERATOR),
            Token(Position(28, 29, 1,1, 28, 29), "8", TokenType.SYMBOL),
            Token(Position(30, 31, 1,1, 30, 31), ";", TokenType.DELIMITER)
        )
        val result = lexer.tokenize(input)
        var i = 0
        while (i < result.size) {
            assertEquals(tokenList[i].getType(), result[i].getType())
            assertEquals(tokenList[i].getValue(), result[i].getValue())
            assertEquals(tokenList[i].getPosition(), result[i].getPosition())
            i++
        }
    }

    @Test
    fun testPrintlnFunctionWithNumber() {
        val input = "println(8);"
        val tokenList = listOf(
            Token(Position(1, 8, 1, 1, 1, 8), "println", TokenType.FUNCTION),
            Token(Position(8, 9, 1, 1, 8, 9), "(", TokenType.PARENTHESIS),
            Token(Position(9, 10, 1, 1, 9, 10), "8", TokenType.SYMBOL),
            Token(Position(10, 11, 1, 1, 10, 11), ")", TokenType.PARENTHESIS),
            Token(Position(11, 12, 1, 1, 11, 12), ";", TokenType.DELIMITER)
        )
        val result = lexer.tokenize(input)
        var i = 0
        while (i < result.size) {
            assertEquals(tokenList[i].getType(), result[i].getType())
            assertEquals(tokenList[i].getValue(), result[i].getValue())
            assertEquals(tokenList[i].getPosition(), result[i].getPosition())
            i++
        }
    }

    @Test
    fun testPrintlnFunctionWithOperationAndSpaces() {
        val input = "println(8 + 9)  ;"
        val tokenList = listOf(
            Token(Position(1, 8, 1, 1, 1, 8), "println", TokenType.FUNCTION),
            Token(Position(8, 9, 1, 1, 8, 9), "(", TokenType.PARENTHESIS),
            Token(Position(9, 10, 1, 1, 9, 10), "8", TokenType.SYMBOL),
            Token(Position(11, 12, 1, 1, 11, 12), "+", TokenType.OPERATOR),
            Token(Position(13, 14, 1, 1, 13, 14), "9", TokenType.SYMBOL),
            Token(Position(14, 15, 1, 1, 14, 15), ")", TokenType.PARENTHESIS),
            Token(Position(17, 18, 1, 1, 17, 18), ";", TokenType.DELIMITER)

        )
        val result = lexer.tokenize(input)
        var i = 0
        while (i < result.size) {
            assertEquals(tokenList[i].getType(), result[i].getType())
            assertEquals(tokenList[i].getValue(), result[i].getValue())
            assertEquals(tokenList[i].getPosition(), result[i].getPosition())
            i++
        }
    }

    @Test
    fun testPrintlnFunctionWithOperationAndSpaces2() {
        val input = " println ( 8 + 9 )  ;"
        val tokenList = listOf(
            Token(Position(2, 9, 1, 1, 2, 9), "println", TokenType.FUNCTION),
            Token(Position(10, 11, 1, 1, 10, 11), "(", TokenType.PARENTHESIS),
            Token(Position(12, 13, 1, 1, 12, 13), "8", TokenType.SYMBOL),
            Token(Position(14, 15, 1, 1, 14, 15), "+", TokenType.OPERATOR),
            Token(Position(16, 17, 1, 1, 16, 17), "9", TokenType.SYMBOL),
            Token(Position(18, 19, 1, 1, 18, 19), ")", TokenType.PARENTHESIS),
            Token(Position(21, 22, 1, 1, 21, 22), ";", TokenType.DELIMITER)

        )
        val result = lexer.tokenize(input)
        var i = 0
        while (i < result.size) {
            assertEquals(tokenList[i].getType(), result[i].getType())
            assertEquals(tokenList[i].getValue(), result[i].getValue())
            assertEquals(tokenList[i].getPosition(), result[i].getPosition())
            i++
        }
    }

    @Test
    fun testPrintlnFunctionWithOperationAndSpaces3() {
        val input = " println ( 8 * 9 )  ;"
        val tokenList = listOf(
            Token(Position(2, 9, 1, 1, 2, 9), "println", TokenType.FUNCTION),
            Token(Position(10, 11, 1, 1, 10, 11), "(", TokenType.PARENTHESIS),
            Token(Position(12, 13, 1, 1, 12, 13), "8", TokenType.SYMBOL),
            Token(Position(14, 15, 1, 1, 14, 15), "*", TokenType.OPERATOR),
            Token(Position(16, 17, 1, 1, 16, 17), "9", TokenType.SYMBOL),
            Token(Position(18, 19, 1, 1, 18, 19), ")", TokenType.PARENTHESIS),
            Token(Position(21, 22, 1, 1, 21, 22), ";", TokenType.DELIMITER)

        )
        val result = lexer.tokenize(input)
        var i = 0
        while (i < result.size) {
            assertEquals(tokenList[i].getType(), result[i].getType())
            assertEquals(tokenList[i].getValue(), result[i].getValue())
            assertEquals(tokenList[i].getPosition(), result[i].getPosition())
            i++
        }
    }

    @Test
    fun testPrintlnFunctionWithOperationAndSpaces4() {
        val input = " println ( 8 / 9 )  ;"
        val tokenList = listOf(
            Token(Position(2, 9, 1, 1, 2, 9), "println", TokenType.FUNCTION),
            Token(Position(10, 11, 1, 1, 10, 11), "(", TokenType.PARENTHESIS),
            Token(Position(12, 13, 1, 1, 12, 13), "8", TokenType.SYMBOL),
            Token(Position(14, 15, 1, 1, 14, 15), "/", TokenType.OPERATOR),
            Token(Position(16, 17, 1, 1, 16, 17), "9", TokenType.SYMBOL),
            Token(Position(18, 19, 1, 1, 18, 19), ")", TokenType.PARENTHESIS),
            Token(Position(21, 22, 1, 1, 21, 22), ";", TokenType.DELIMITER)

        )
        val result = lexer.tokenize(input)
        var i = 0
        while (i < result.size) {
            assertEquals(tokenList[i].getType(), result[i].getType())
            assertEquals(tokenList[i].getValue(), result[i].getValue())
            assertEquals(tokenList[i].getPosition(), result[i].getPosition())
            i++
        }
    }

    @Test
    fun testPrintlnFunctionWithOperationAndSpaces5() {
        val input = " println ( 8 - 9 )  ;"
        val tokenList = listOf(
            Token(Position(2, 9, 1, 1, 2, 9), "println", TokenType.FUNCTION),
            Token(Position(10, 11, 1, 1, 10, 11), "(", TokenType.PARENTHESIS),
            Token(Position(12, 13, 1, 1, 12, 13), "8", TokenType.SYMBOL),
            Token(Position(14, 15, 1, 1, 14, 15), "-", TokenType.OPERATOR),
            Token(Position(16, 17, 1, 1, 16, 17), "9", TokenType.SYMBOL),
            Token(Position(18, 19, 1, 1, 18, 19), ")", TokenType.PARENTHESIS),
            Token(Position(21, 22, 1, 1, 21, 22), ";", TokenType.DELIMITER)

        )
        val result = lexer.tokenize(input)
        var i = 0
        while (i < result.size) {
            assertEquals(tokenList[i].getType(), result[i].getType())
            assertEquals(tokenList[i].getValue(), result[i].getValue())
            assertEquals(tokenList[i].getPosition(), result[i].getPosition())
            i++
        }
    }

    @Test
    fun testPrintlnFunctionWithString() {
        val input = "println(\"hello\" + \"world\");"
        val tokenList = listOf(
            Token(Position(1, 8, 1, 1, 1, 8), "println", TokenType.FUNCTION),
            Token(Position(8, 9, 1, 1, 8, 9), "(", TokenType.PARENTHESIS),
            Token(Position(9, 16, 1, 1, 9, 16), "\"hello\"", TokenType.SYMBOL),
            Token(Position(17, 18, 1, 1, 17, 18), "+", TokenType.OPERATOR),
            Token(Position(19, 26, 1, 1, 19, 26), "\"world\"", TokenType.SYMBOL),
            Token(Position(26, 27, 1, 1, 26, 27), ")", TokenType.PARENTHESIS),
            Token(Position(27, 28, 1, 1, 27, 28), ";", TokenType.DELIMITER)
        )
        val result = lexer.tokenize(input)
        var i = 0
        while (i < result.size) {
            assertEquals(tokenList[i].getType(), result[i].getType())
            assertEquals(tokenList[i].getValue(), result[i].getValue())
            assertEquals(tokenList[i].getPosition(), result[i].getPosition())
            i++
        }
    }

    @Test
    fun testPrintlnFunctionWithVariable() {
        val input = "println(x);"
        val tokenList = listOf(
            Token(Position(1, 8, 1, 1, 1, 8), "println", TokenType.FUNCTION),
            Token(Position(8, 9, 1, 1, 8, 9), "(", TokenType.PARENTHESIS),
            Token(Position(9, 10, 1, 1, 9, 10), "x", TokenType.SYMBOL),
            Token(Position(10, 11, 1, 1, 10, 11), ")", TokenType.PARENTHESIS),
            Token(Position(11, 12, 1, 1, 11, 12), ";", TokenType.DELIMITER)
        )
        val result = lexer.tokenize(input)
        var i = 0
        while (i < result.size) {
            assertEquals(tokenList[i].getType(), result[i].getType())
            assertEquals(tokenList[i].getValue(), result[i].getValue())
            assertEquals(tokenList[i].getPosition(), result[i].getPosition())
            i++
        }
    }

    @Test
    fun testAssignationAndDeclarationInDifferentLines() {
        val input = "let\na\n:\nnumber\n=\n8\n;"
        val tokenList = listOf(
            Token(Position(1,4,1,1,1,4), "let", TokenType.KEYWORD),
            Token(Position(4,5,2,2,1,2), "a", TokenType.SYMBOL),
            Token(Position(5,6,3,3,1,2), ":", TokenType.DECLARATION),
            Token(Position(6,12,4,4,1,7), "number", TokenType.TYPE),
            Token(Position(12, 13, 5,5, 1, 2), "=", TokenType.ASSIGNATION),
            Token(Position(13, 14, 6,6, 1, 2), "8", TokenType.SYMBOL),
            Token(Position(14, 15, 7,7, 1, 2), ";", TokenType.DELIMITER),
            )
        val result = lexer.tokenize(input)
        var i = 0
        while (i < result.size) {
            assertEquals(tokenList[i].getType(), result[i].getType())
            assertEquals(tokenList[i].getValue(), result[i].getValue())
            assertEquals(tokenList[i].getPosition(), result[i].getPosition())
            i++
        }
    }

    @Test
    fun testSameLineAssignationAndDeclaration() {
        val input = "let a:number = 8;let b:number = 9;"
        val tokenList = listOf(
            Token(Position(1,4,1,1,1,4), "let", TokenType.KEYWORD),
            Token(Position(5,6,1,1,5,6), "a", TokenType.SYMBOL),
            Token(Position(6,7,1,1,6,7), ":", TokenType.DECLARATION),
            Token(Position(7,13,1,1,7,13), "number", TokenType.TYPE),
            Token(Position(14, 15, 1,1, 14, 15), "=", TokenType.ASSIGNATION),
            Token(Position(16, 17, 1,1, 16, 17), "8", TokenType.SYMBOL),
            Token(Position(17, 18, 1,1, 17, 18), ";", TokenType.DELIMITER),
            Token(Position(18,21,1,1,18,21), "let", TokenType.KEYWORD),
            Token(Position(22,23,1,1,22,23), "b", TokenType.SYMBOL),
            Token(Position(23,24,1,1,23,24), ":", TokenType.DECLARATION),
            Token(Position(24,30,1,1,24,30), "number", TokenType.TYPE),
            Token(Position(31, 32, 1,1, 31, 32), "=", TokenType.ASSIGNATION),
            Token(Position(33, 34, 1,1, 33, 34), "9", TokenType.SYMBOL),
            Token(Position(34, 35, 1,1, 34, 35), ";", TokenType.DELIMITER),
            )
        val result = lexer.tokenize(input)
        var i = 0
        while (i < result.size) {
            assertEquals(tokenList[i].getType(), result[i].getType())
            assertEquals(tokenList[i].getValue(), result[i].getValue())
            assertEquals(tokenList[i].getPosition(), result[i].getPosition())
            i++
        }
    }


}
