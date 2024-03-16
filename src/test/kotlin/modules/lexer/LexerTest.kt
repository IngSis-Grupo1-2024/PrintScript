package modules.lexer

import components.Position
import components.TokenType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LexerTest {


    //Test tokenizing of "let a:number = 12";
    @Test
    fun testNumberVariableTokenizePositionAndSize(){
        val lexer = Lexer(Position())
        val tokenList = lexer.tokenize("let a:number = 1;")

        assertEquals(7, tokenList.size)
        assertEquals(Position(1, 3,1,1,1,3), tokenList[0].position)
        assertEquals(Position(5, 5,1,1,5,5), tokenList[1].position)
        assertEquals(Position(6, 6,1,1,6,6), tokenList[2].position)
        assertEquals(Position(7, 12,1,1,7,12), tokenList[3].position)
        assertEquals(Position(14, 14,1,1,14,14), tokenList[4].position)
        assertEquals(Position(16, 16,1,1,16,16), tokenList[5].position)
        assertEquals(Position(17, 17,1,1,17,17), tokenList[6].position)

    }

    @Test
    fun testNumberVariableTokenizeValue(){
        val lexer = Lexer(Position())
        val tokenList = lexer.tokenize("let a:number = 12;")

        assertEquals("let", tokenList[0].value)
        assertEquals("a", tokenList[1].value)
        assertEquals(":", tokenList[2].value)
        assertEquals("number", tokenList[3].value)
        assertEquals("=", tokenList[4].value)
        assertEquals("12", tokenList[5].value)
        assertEquals(";", tokenList[6].value)
    }

    @Test
    fun testNumberVariableTokenizeTokenType(){
        val lexer = Lexer(Position())
        val tokenList = lexer.tokenize("let a:number = 12;")

        assertEquals(TokenType.KEYWORD, tokenList[0].type)
        assertEquals(TokenType.IDENTIFIER, tokenList[1].type)
        assertEquals(TokenType.DECLARATION, tokenList[2].type)
        assertEquals(TokenType.TYPE, tokenList[3].type)
        assertEquals(TokenType.ASSIGNATION, tokenList[4].type) //There are two different assignations token types
        assertEquals(TokenType.VALUE, tokenList[5].type)
        assertEquals(TokenType.SEMICOLON, tokenList[6].type)
    }




    //Test tokenizing of "let a:number = 12 + 4;"
    @Test
    fun testVariableCreationWithOperatorPosition() {
        val lexer = Lexer(Position())
        val tokenList = lexer.tokenize("let a:number = 12 + 4;")

        assertEquals(Position(1, 3,1,1,1,3), tokenList[0].position)
        assertEquals(Position(5, 5,1,1,5,5), tokenList[1].position)
        assertEquals(Position(6, 6,1,1,6,6), tokenList[2].position)
        assertEquals(Position(7, 12,1,1,7,12), tokenList[3].position)
        assertEquals(Position(14, 14,1,1,14,14), tokenList[4].position)
        assertEquals(Position(16, 17,1,1,16,17), tokenList[5].position)
        assertEquals(Position(19, 19,1,1,19,19), tokenList[6].position)
        assertEquals(Position(21, 21,1,1,21,21), tokenList[7].position)
        assertEquals(Position(22, 22,1,1,22,22), tokenList[8].position)
    }

    @Test
    fun testVariableCreationWithOperatorValue(){
        val lexer = Lexer(Position())
        val tokenList = lexer.tokenize("let a:number = 12 + 4;")

        assertEquals("let", tokenList[0].value)
        assertEquals("a", tokenList[1].value)
        assertEquals(":", tokenList[2].value)
        assertEquals("number", tokenList[3].value)
        assertEquals("=", tokenList[4].value)
        assertEquals("12", tokenList[5].value)
        assertEquals("+", tokenList[6].value)
        assertEquals("4", tokenList[7].value)
        assertEquals(";", tokenList[8].value)
    }

    @Test
    fun testVariableCreationWithOperatorType(){
        val lexer = Lexer(Position())
        val tokenList = lexer.tokenize("let a:number = 12 + 4;")

        assertEquals(TokenType.KEYWORD, tokenList[0].type)
        assertEquals(TokenType.IDENTIFIER, tokenList[1].type)
        assertEquals(TokenType.DECLARATION, tokenList[2].type)//Is it weird that it is assignation?
        assertEquals(TokenType.TYPE, tokenList[3].type)
        assertEquals(TokenType.ASSIGNATION, tokenList[4].type) //There are two different assignations token types
        assertEquals(TokenType.VALUE, tokenList[5].type)
        assertEquals(TokenType.OPERATOR, tokenList[6].type)
        assertEquals(TokenType.VALUE, tokenList[7].type)
        assertEquals(TokenType.SEMICOLON, tokenList[8].type)



    }


    @Test
    fun testNumberVariableTokenizePositionAndSizeWithMultipleSpaces(){
        val lexer = Lexer(Position())
        val tokenList = lexer.tokenize("let   a  : number   = 1   ;")

        assertEquals(7, tokenList.size)
        assertEquals(Position(1, 3,1,1,1,3), tokenList[0].position)
        assertEquals(Position(7, 7,1,1,7,7), tokenList[1].position)
        assertEquals(Position(10, 10,1,1,10,10), tokenList[2].position)
        assertEquals(Position(12, 17,1,1,12,17), tokenList[3].position)
        assertEquals(Position(21, 21,1,1,21,21), tokenList[4].position)
        assertEquals(Position(23, 23,1,1,23,23), tokenList[5].position)
        assertEquals(Position(27, 27,1,1,27,27), tokenList[6].position)

    }

    @Test
    fun testTokenizeTwoLines(){
        val lexer = Lexer(Position())
        val tokenList = lexer.tokenize("let a: number = 1;\nlet b: number = 2;")

        assertEquals("let", tokenList[0].value)
        assertEquals("a", tokenList[1].value)
        assertEquals(":", tokenList[2].value)
        assertEquals("number", tokenList[3].value)
        assertEquals("=", tokenList[4].value)
        assertEquals("1", tokenList[5].value)
        assertEquals(";", tokenList[6].value)
        assertEquals("let", tokenList[7].value)
        assertEquals("b", tokenList[8].value)
        assertEquals(":", tokenList[9].value)
        assertEquals("number", tokenList[10].value)
        assertEquals("=", tokenList[11].value)
        assertEquals("2", tokenList[12].value)
        assertEquals(";", tokenList[13].value)
    }

    @Test
    fun testTokenizeTwoLinesPosition(){
        val lexer = Lexer(Position())
        val tokenList = lexer.tokenize("let a: number = 1;\nlet b: number = 2;")

        assertEquals(Position(1, 3,1,1,1,3), tokenList[0].position)
        assertEquals(Position(5, 5,1,1,5,5), tokenList[1].position)
        assertEquals(Position(6, 6,1,1,6,6), tokenList[2].position)
        assertEquals(Position(8, 13,1,1,8,13), tokenList[3].position)
        assertEquals(Position(15, 15,1,1,15,15), tokenList[4].position)
        assertEquals(Position(17, 17,1,1,17,17), tokenList[5].position)
        assertEquals(Position(18, 18,1,1,18,18), tokenList[6].position)
        assertEquals(Position(19, 21,2,2,1,3), tokenList[7].position)
        assertEquals(Position(23, 23,2,2,5,5), tokenList[8].position)
        assertEquals(Position(24, 24,2,2,6,6), tokenList[9].position)
        assertEquals(Position(26, 31,2,2,8,13), tokenList[10].position)
        assertEquals(Position(33, 33,2,2,15,15), tokenList[11].position)
        assertEquals(Position(35, 35,2,2,17,17), tokenList[12].position)
        assertEquals(Position(36, 36,2,2,18,18), tokenList[13].position)
    }

    @Test
    fun testTokenizeTwoLinesType(){
        val lexer = Lexer(Position())
        val tokenList = lexer.tokenize("let a: number = 1;\nlet b: number = 2;")

        assertEquals(TokenType.KEYWORD, tokenList[0].type)
        assertEquals(TokenType.IDENTIFIER, tokenList[1].type)
        assertEquals(TokenType.DECLARATION, tokenList[2].type)
        assertEquals(TokenType.TYPE, tokenList[3].type)
        assertEquals(TokenType.ASSIGNATION, tokenList[4].type)
        assertEquals(TokenType.VALUE, tokenList[5].type)
        assertEquals(TokenType.SEMICOLON, tokenList[6].type)
        assertEquals(TokenType.KEYWORD, tokenList[7].type)
        assertEquals(TokenType.IDENTIFIER, tokenList[8].type)
        assertEquals(TokenType.DECLARATION, tokenList[9].type)
        assertEquals(TokenType.TYPE, tokenList[10].type)
        assertEquals(TokenType.ASSIGNATION, tokenList[11].type)
        assertEquals(TokenType.VALUE, tokenList[12].type)
        assertEquals(TokenType.SEMICOLON, tokenList[13].type)
    }
}