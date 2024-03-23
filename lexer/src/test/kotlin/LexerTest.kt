
import components.Position
import components.TokenType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class LexerTest {


    //Test tokenizing of "let a:number = 12";
    @Test
    fun testNumberVariableTokenizePositionAndSize(){
        val lexer = Lexer(Position())
        val tokenList = lexer.tokenize("let a:number = 12;")

        assertEquals(7, tokenList.size)
        assertEquals(Position(1, 3,1,1,1,3), tokenList[0].getPosition())
        assertEquals(Position(5, 5,1,1,5,5), tokenList[1].getPosition())
        assertEquals(Position(6, 6,1,1,6,6), tokenList[2].getPosition())
        assertEquals(Position(7, 12,1,1,7,12), tokenList[3].getPosition())
        assertEquals(Position(14, 14,1,1,14,14), tokenList[4].getPosition())
        assertEquals(Position(16, 17,1,1,16,17), tokenList[5].getPosition())
        assertEquals(Position(18, 18,1,1,18,18), tokenList[6].getPosition())

    }

    @Test
    fun testNumberVariableTokenizeValue(){
        val lexer = Lexer(Position())
        val tokenList = lexer.tokenize("let a:number = 12;")

        assertEquals("let", tokenList[0].getValue())
        assertEquals("a", tokenList[1].getValue())
        assertEquals(":", tokenList[2].getValue())
        assertEquals("number", tokenList[3].getValue())
        assertEquals("=", tokenList[4].getValue())
        assertEquals("12", tokenList[5].getValue())
        assertEquals(";", tokenList[6].getValue())
    }

    @Test
    fun testNumberVariableTokenizeTokenType(){
        val lexer = Lexer(Position())
        val tokenList = lexer.tokenize("let a:number = \"12\";")

        assertEquals(TokenType.KEYWORD, tokenList[0].getType())
        assertEquals(TokenType.IDENTIFIER, tokenList[1].getType())
        assertEquals(TokenType.DECLARATION, tokenList[2].getType())
        assertEquals(TokenType.TYPE, tokenList[3].getType())
        assertEquals(TokenType.ASSIGNATION, tokenList[4].getType()) //There are two different assignations token types
        assertEquals(TokenType.STRING, tokenList[5].getType())
        assertEquals(TokenType.SEMICOLON, tokenList[6].getType())
    }




    //Test tokenizing of "let a:number = 12 + 4;"
    @Test
    fun testVariableCreationWithOperatorPosition() {
        val lexer = Lexer(Position())
        val tokenList = lexer.tokenize("let a:number = 12 + 4;")

        assertEquals(Position(1, 3,1,1,1,3), tokenList[0].getPosition())
        assertEquals(Position(5, 5,1,1,5,5), tokenList[1].getPosition())
        assertEquals(Position(6, 6,1,1,6,6), tokenList[2].getPosition())
        assertEquals(Position(7, 12,1,1,7,12), tokenList[3].getPosition())
        assertEquals(Position(14, 14,1,1,14,14), tokenList[4].getPosition())
        assertEquals(Position(16, 17,1,1,16,17), tokenList[5].getPosition())
        assertEquals(Position(19, 19,1,1,19,19), tokenList[6].getPosition())
        assertEquals(Position(21, 21,1,1,21,21), tokenList[7].getPosition())
        assertEquals(Position(22, 22,1,1,22,22), tokenList[8].getPosition())
    }

    @Test
    fun testVariableCreationWithOperatorValue(){
        val lexer = Lexer(Position())
        val tokenList = lexer.tokenize("let a:number = 12 + 4;")

        assertEquals("let", tokenList[0].getValue())
        assertEquals("a", tokenList[1].getValue())
        assertEquals(":", tokenList[2].getValue())
        assertEquals("number", tokenList[3].getValue())
        assertEquals("=", tokenList[4].getValue())
        assertEquals("12", tokenList[5].getValue())
        assertEquals("+", tokenList[6].getValue())
        assertEquals("4", tokenList[7].getValue())
        assertEquals(";", tokenList[8].getValue())
    }

    @Test
    fun testVariableCreationWithOperatorType(){
        val lexer = Lexer(Position())
        val tokenList = lexer.tokenize("let a:number = 12 + 4;")

        assertEquals(TokenType.KEYWORD, tokenList[0].getType())
        assertEquals(TokenType.IDENTIFIER, tokenList[1].getType())
        assertEquals(TokenType.DECLARATION, tokenList[2].getType())//Is it weird that it is assignation?
        assertEquals(TokenType.TYPE, tokenList[3].getType())
        assertEquals(TokenType.ASSIGNATION, tokenList[4].getType()) //There are two different assignations token types
        assertEquals(TokenType.INTEGER, tokenList[5].getType())
        assertEquals(TokenType.OPERATOR, tokenList[6].getType())
        assertEquals(TokenType.INTEGER, tokenList[7].getType())
        assertEquals(TokenType.SEMICOLON, tokenList[8].getType())



    }


    @Test
    fun testNumberVariableTokenizePositionAndSizeWithMultipleSpaces(){
        val lexer = Lexer(Position())
        val tokenList = lexer.tokenize("let   a  : number   = 1   ;")

        assertEquals(7, tokenList.size)
        assertEquals(Position(1, 3,1,1,1,3), tokenList[0].getPosition())
        assertEquals(Position(7, 7,1,1,7,7), tokenList[1].getPosition())
        assertEquals(Position(10, 10,1,1,10,10), tokenList[2].getPosition())
        assertEquals(Position(12, 17,1,1,12,17), tokenList[3].getPosition())
        assertEquals(Position(21, 21,1,1,21,21), tokenList[4].getPosition())
        assertEquals(Position(23, 23,1,1,23,23), tokenList[5].getPosition())
        assertEquals(Position(27, 27,1,1,27,27), tokenList[6].getPosition())

    }

    @Test
    fun testTokenizeTwoLines(){
        val lexer = Lexer(Position())
        val tokenList = lexer.tokenize("let a: number = 1;\nlet b: number = 2;")

        assertEquals("let", tokenList[0].getValue())
        assertEquals("a", tokenList[1].getValue())
        assertEquals(":", tokenList[2].getValue())
        assertEquals("number", tokenList[3].getValue())
        assertEquals("=", tokenList[4].getValue())
        assertEquals("1", tokenList[5].getValue())
        assertEquals(";", tokenList[6].getValue())
        assertEquals("let", tokenList[7].getValue())
        assertEquals("b", tokenList[8].getValue())
        assertEquals(":", tokenList[9].getValue())
        assertEquals("number", tokenList[10].getValue())
        assertEquals("=", tokenList[11].getValue())
        assertEquals("2", tokenList[12].getValue())
        assertEquals(";", tokenList[13].getValue())
    }

    @Test
    fun testTokenizeTwoLinesPosition(){
        val lexer = Lexer(Position())
        val tokenList = lexer.tokenize("let a: number = 1;\nlet b: number = 2;")

        assertEquals(Position(1, 3,1,1,1,3), tokenList[0].getPosition())
        assertEquals(Position(5, 5,1,1,5,5), tokenList[1].getPosition())
        assertEquals(Position(6, 6,1,1,6,6), tokenList[2].getPosition())
        assertEquals(Position(8, 13,1,1,8,13), tokenList[3].getPosition())
        assertEquals(Position(15, 15,1,1,15,15), tokenList[4].getPosition())
        assertEquals(Position(17, 17,1,1,17,17), tokenList[5].getPosition())
        assertEquals(Position(18, 18,1,1,18,18), tokenList[6].getPosition())
        assertEquals(Position(19, 21,2,2,1,3), tokenList[7].getPosition())
        assertEquals(Position(23, 23,2,2,5,5), tokenList[8].getPosition())
        assertEquals(Position(24, 24,2,2,6,6), tokenList[9].getPosition())
        assertEquals(Position(26, 31,2,2,8,13), tokenList[10].getPosition())
        assertEquals(Position(33, 33,2,2,15,15), tokenList[11].getPosition())
        assertEquals(Position(35, 35,2,2,17,17), tokenList[12].getPosition())
        assertEquals(Position(36, 36,2,2,18,18), tokenList[13].getPosition())
    }

    @Test
    fun testTokenizeTwoLinesType(){
        val lexer = Lexer(Position())
        val tokenList = lexer.tokenize("let a: number = 1;\nlet b: number = 2;")

        assertEquals(TokenType.KEYWORD, tokenList[0].getType())
        assertEquals(TokenType.IDENTIFIER, tokenList[1].getType())
        assertEquals(TokenType.DECLARATION, tokenList[2].getType())
        assertEquals(TokenType.TYPE, tokenList[3].getType())
        assertEquals(TokenType.ASSIGNATION, tokenList[4].getType())
        assertEquals(TokenType.INTEGER, tokenList[5].getType())
        assertEquals(TokenType.SEMICOLON, tokenList[6].getType())
        assertEquals(TokenType.KEYWORD, tokenList[7].getType())
        assertEquals(TokenType.IDENTIFIER, tokenList[8].getType())
        assertEquals(TokenType.DECLARATION, tokenList[9].getType())
        assertEquals(TokenType.TYPE, tokenList[10].getType())
        assertEquals(TokenType.ASSIGNATION, tokenList[11].getType())
        assertEquals(TokenType.INTEGER, tokenList[12].getType())
        assertEquals(TokenType.SEMICOLON, tokenList[13].getType())
    }
}