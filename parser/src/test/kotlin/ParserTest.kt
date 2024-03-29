package modules.parser

class ParserTest {
//    @Test
//    fun `declaration of x as string`() {
//        val parser = Parser()
//        val position = Position()
//        val tokens: List<Token> = listOf(
//            Token(position, "let", TokenType.KEYWORD),
//            Token(position, "x", TokenType.IDENTIFIER),
//            Token(position, ":", TokenType.ASSIGNATION),
//            Token(position, "string", TokenType.TYPE),
//            Token(position, "", TokenType.SEMICOLON),
//        )
//        val astExpected: ASTInterface =
//            AST(Token(position, ":", TokenType.ASSIGNATION),
//                listOf(AST(Token(position, "x", TokenType.IDENTIFIER)),
//                AST(Token(position, "string", TokenType.TYPE))))
//        println(parser.parse(tokens))
//        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
//    }
//    @Test
//    fun `8 plus 3`() {
//        val parser = Parser()
//        val position = Position()
//        val tokens: List<Token> = listOf(
//            Token(position, "8", TokenType.VALUE),
//            Token(position, "+", TokenType.OPERATOR),
//            Token(position, "3", TokenType.VALUE),
//        )
//        val astExpected: ASTInterface =
//            AST(
//                Token(position, "+", TokenType.OPERATOR),
//                listOf(AST(Token(position, "8", TokenType.VALUE)),
//                AST(Token(position, "3", TokenType.VALUE))
//            ))
//        println(parser.parse(tokens))
//        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
//    }
//    @Test
//    fun `x = 8 plus 3`() {
//        val parser = Parser()
//        val position = Position()
//        val tokens: List<Token> = listOf(
//            Token(position, "x", TokenType.IDENTIFIER),
//            Token(position, "=", TokenType.ASSIGNATION),
//            Token(position, "8", TokenType.VALUE),
//            Token(position, "+", TokenType.OPERATOR),
//            Token(position, "3", TokenType.VALUE),
//        )
//        val astExpected: ASTInterface =
//            AST(
//                Token(position, "=", TokenType.ASSIGNATION),
//                listOf(AST(Token(position, "x", TokenType.IDENTIFIER)),
//                AST(
//                    Token(position, "+", TokenType.OPERATOR),
//                    listOf(AST(Token(position, "8", TokenType.VALUE)),
//                    AST(Token(position, "3", TokenType.VALUE))
//                    ))
//                ))
//        println(parser.parse(tokens))
//        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
//    }
//
//    @Test
//    fun `let x number = 8`() {
//        val parser = Parser()
//        val position = Position()
//        val tokens: List<Token> = listOf(
//            Token(position, "x", TokenType.IDENTIFIER),
//            Token(position, ":", TokenType.ASSIGNATION),
//            Token(position, "number", TokenType.TYPE),
//            Token(position, "=", TokenType.ASSIGNATION),
//            Token(position, "8", TokenType.VALUE),
//        )
//        val astExpected: ASTInterface =
//            AST(
//                Token(position, "=", TokenType.ASSIGNATION),
//                listOf(AST(Token(position, "x", TokenType.IDENTIFIER)),
//                        AST(Token(position, "8", TokenType.VALUE))
//                ))
// //        println(parser.parse(tokens))
//        println(astExpected)
//        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
//    }
//
//    @Test
//    fun `8 + 3 mul 2`() {
//        val parser = Parser()
//        val position = Position()
//        val tokens: List<Token> = listOf(
//            Token(position, "8", TokenType.VALUE),
//            Token(position, "+", TokenType.OPERATOR),
//            Token(position, "3", TokenType.VALUE),
//            Token(position, "*", TokenType.OPERATOR),
//            Token(position, "2", TokenType.VALUE),
//        )
//        val astExpected: ASTInterface =
//            AST(
//                Token(position, "+", TokenType.OPERATOR),
//                listOf(AST(
//                    Token(position, "8", TokenType.VALUE)),
//                AST(Token(position, "*", TokenType.OPERATOR),
//                    listOf(AST(Token(position, "3", TokenType.VALUE)),
//                    AST(Token(position, "2", TokenType.VALUE)))
//                )
//            ))
//        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
//    }
//
//    @Test
//    fun `8 mul 3 + 2`() {
//        val parser = Parser()
//        val position = Position()
//        val tokens: List<Token> = listOf(
//            Token(position, "8", TokenType.VALUE),
//            Token(position, "*", TokenType.OPERATOR),
//            Token(position, "3", TokenType.VALUE),
//            Token(position, "+", TokenType.OPERATOR),
//            Token(position, "2", TokenType.VALUE),
//        )
//        val astExpected: ASTInterface =
//            AST(
//                Token(position, "+", TokenType.OPERATOR),
//                listOf(AST(Token(position, "*", TokenType.OPERATOR),
//                    listOf(AST(Token(position, "8", TokenType.VALUE)),
//                    AST(Token(position, "3", TokenType.VALUE)))
//                ),
//                AST(
//                    Token(position, "2", TokenType.VALUE))
//            ))
//        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
//    }
//    @Test
//    fun `let x number = 8 + 3 mul 2`() {
//        val parser = Parser()
//        val position = Position()
//        val tokens: List<Token> = listOf(
//            Token(position, "let", TokenType.KEYWORD),
//            Token(position, "x", TokenType.IDENTIFIER),
//            Token(position, ":", TokenType.ASSIGNATION),
//            Token(position, "number", TokenType.TYPE),
//            Token(position, "=", TokenType.ASSIGNATION),
//            Token(position, "8", TokenType.VALUE),
//            Token(position, "+", TokenType.OPERATOR),
//            Token(position, "3", TokenType.VALUE),
//            Token(position, "*", TokenType.OPERATOR),
//            Token(position, "2", TokenType.VALUE),
//            Token(position, "", TokenType.SEMICOLON)
//        )
//        val astExpected: ASTInterface =
//            AST(
//                Token(position, "+", TokenType.OPERATOR),
//                listOf(AST(
//                    Token(position, "8", TokenType.VALUE)),
//                    AST(Token(position, "*", TokenType.OPERATOR),
//                        listOf(AST(Token(position, "3", TokenType.VALUE)),
//                            AST(Token(position, "2", TokenType.VALUE)))
//                    )
//                ))
//
//        assertEquals(astExpected.toString(), parser.parse(tokens).toString())
//    }
//
//    @Test
//    fun testIsDeclarationShouldReturnTrue() {
//        val parser = Parser()
//        val position = Position()
//        val tokens: List<Token> = listOf(
//            Token(position, "let", TokenType.KEYWORD),
//            Token(position, "x", TokenType.IDENTIFIER),
//            Token(position, ":", TokenType.DECLARATION),
//            Token(position, "string", TokenType.TYPE),
//            Token(position, ";", TokenType.SEMICOLON),
//        )
//        assertEquals(true, parser.isDeclaration(tokens))
//    }
//
//    @Test
//    fun testIsAssignationShouldReturnTrue() {
//        val parser = Parser()
//        val position = Position()
//        val tokens: List<Token> = listOf(
//            Token(position, "let", TokenType.KEYWORD),
//            Token(position, "x", TokenType.IDENTIFIER),
//            Token(position, ":", TokenType.DECLARATION),
//            Token(position, "string", TokenType.TYPE),
//            Token(position, "=", TokenType.ASSIGNATION),
//            Token(position, "'hello'", TokenType.STRING),
//            Token(position, ";", TokenType.SEMICOLON),
//        )
//        assertEquals(true, parser.isAssignation(tokens))
//    }
//
//    @Test
//    fun testIsAssignationShouldReturnFalse() {
//        val parser = Parser()
//        val position = Position()
//        val tokens: List<Token> = listOf(
//            Token(position, "let", TokenType.KEYWORD),
//            Token(position, "x", TokenType.IDENTIFIER),
//            Token(position, "=", TokenType.ASSIGNATION),
//            Token(position, "'hello'", TokenType.VALUE),
//            Token(position, "+", TokenType.OPERATOR),
//            Token(position, ";", TokenType.SEMICOLON),
//        )
//        assertEquals(false, parser.isAssignation(tokens))
//    }
//
//    @Test
//    fun testIsAssignationWithSemicolonAfterDeclarationShouldReturnFalse() {
//        val parser = Parser()
//        val position = Position()
//        val tokens: List<Token> = listOf(
//            Token(position, "let", TokenType.KEYWORD),
//            Token(position, "x", TokenType.IDENTIFIER),
//            Token(position, ":", TokenType.DECLARATION),
//            Token(position, "string", TokenType.TYPE),
//            Token(position, "=", TokenType.ASSIGNATION),
//            Token(position, ";", TokenType.SEMICOLON),
//            Token(position, "'hello'", TokenType.VALUE),
//            Token(position, "+", TokenType.OPERATOR),
//            Token(position, "bye", TokenType.VALUE),
//            Token(position, ";", TokenType.SEMICOLON),
//        )
//        assertEquals(false, parser.isAssignation(tokens))
//    }
//
//    @Test
//    fun isCallingPrintlnMethodShouldReturnTrue(){
//        val parser = Parser()
//        val position = Position()
//        val tokens: List<Token> = listOf(
//            Token(position, "println", TokenType.KEYWORD),
//            Token(position, "(", TokenType.PARENTHESIS),
//            Token(position, "'hello'", TokenType.STRING),
//            Token(position, ")", TokenType.PARENTHESIS),
//            Token(position, ";", TokenType.SEMICOLON),
//        )
//        assertEquals(true, parser.isCallingMethod(tokens))
//    }
}
