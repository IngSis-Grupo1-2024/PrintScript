import components.Position
import components.Token
import components.TokenType
import components.ast.AST
import ingsis.interpreter.Interpreter
import ingsis.utils.Variable
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import scaRules.Rule

class InterpreterTest {
    @Test
    fun testDeclareNumericVariable() {
        val interpreter = Interpreter()
        val position = Position()
        val ast =
            AST(
                Token(position, "let", TokenType.DECLARATION),
                listOf(
                    AST(Token(position, "a", TokenType.IDENTIFIER)),
                    AST(Token(position, "number", TokenType.INTEGER)),
                ),
            )
        val variableMap = interpreter.interpret(ast)
        assertEquals(1, variableMap.size)
        assertEquals(Variable(TokenType.INTEGER), variableMap["a"])
    }

    @Test
    fun testDeclareStringVariable() {
        val interpreter = Interpreter()
        val position = Position()
        val ast =
            AST(
                Token(position, "let", TokenType.DECLARATION),
                listOf(
                    AST(Token(position, "a", TokenType.IDENTIFIER)),
                    AST(Token(position, "string", TokenType.STRING)),
                ),
            )
        val variableMap = interpreter.interpret(ast)
        assertEquals(1, variableMap.size)
        assertEquals(Variable(TokenType.STRING), variableMap["a"])
    }

    @Test
    fun testDeclareAndAssignNumericVariable() {
        val interpreter = Interpreter()
        val position = Position()
        val leftAst =
            AST(
                Token(position, "let", TokenType.DECLARATION),
                listOf(
                    AST(Token(position, "a", TokenType.IDENTIFIER)),
                    AST(Token(position, "number", TokenType.INTEGER)),
                ),
            )
        val rightAst = AST(Token(position, "5", TokenType.INTEGER))
        val ast = AST(Token(position, "=", TokenType.ASSIGNATION), listOf(leftAst, rightAst))
        val variableMap = interpreter.interpret(ast)
        assertEquals(1, variableMap.size)
        assertEquals(Variable(TokenType.INTEGER, "5"), variableMap["a"])
    }

    @Test
    fun testDeclareAndAssignNumericWithSumVariable() {
        val interpreter = Interpreter()
        val position = Position()
        val leftAst =
            AST(
                Token(position, "let", TokenType.DECLARATION),
                listOf(
                    AST(Token(position, "a", TokenType.IDENTIFIER)),
                    AST(Token(position, "number", TokenType.INTEGER)),
                ),
            )
        val rightAst =
            AST(
                Token(position, "+", TokenType.OPERATOR),
                listOf(AST(Token(position, "5", TokenType.INTEGER)), AST(Token(position, "3", TokenType.INTEGER))),
            )
        val ast = AST(Token(position, "=", TokenType.ASSIGNATION), listOf(leftAst, rightAst))
        val variableMap = interpreter.interpret(ast)
        assertEquals(1, variableMap.size)
        assertEquals(Variable(TokenType.INTEGER, "8"), variableMap["a"])
    }

    @Test
    fun testDeclareAndAssignNumericWithSubtractionVariable() {
        val interpreter = Interpreter()
        val position = Position()
        val leftAst =
            AST(
                Token(position, "let", TokenType.DECLARATION),
                listOf(
                    AST(Token(position, "a", TokenType.IDENTIFIER)),
                    AST(Token(position, "number", TokenType.INTEGER)),
                ),
            )
        val rightAst =
            AST(
                Token(position, "-", TokenType.OPERATOR),
                listOf(AST(Token(position, "5", TokenType.INTEGER)), AST(Token(position, "3", TokenType.INTEGER))),
            )
        val ast = AST(Token(position, "=", TokenType.ASSIGNATION), listOf(leftAst, rightAst))
        val variableMap = interpreter.interpret(ast)
        assertEquals(1, variableMap.size)
        assertEquals(Variable(TokenType.INTEGER, "2"), variableMap["a"])
    }

    @Test
    fun testDeclareAndAssignNumericWithMultiplicationVariable() {
        val interpreter = Interpreter()
        val position = Position()
        val leftAst =
            AST(
                Token(position, "let", TokenType.DECLARATION),
                listOf(
                    AST(Token(position, "a", TokenType.IDENTIFIER)),
                    AST(Token(position, "number", TokenType.INTEGER)),
                ),
            )
        val rightAst =
            AST(
                Token(position, "*", TokenType.OPERATOR),
                listOf(AST(Token(position, "5", TokenType.INTEGER)), AST(Token(position, "3", TokenType.INTEGER))),
            )
        val ast = AST(Token(position, "=", TokenType.ASSIGNATION), listOf(leftAst, rightAst))
        val variableMap = interpreter.interpret(ast)
        assertEquals(1, variableMap.size)
        assertEquals(Variable(TokenType.INTEGER, "15"), variableMap["a"])
    }

    @Test
    fun testDeclareAndAssignNumericWithDivisionVariable() {
        val interpreter = Interpreter()
        val position = Position()
        val leftAst =
            AST(
                Token(position, "let", TokenType.DECLARATION),
                listOf(
                    AST(Token(position, "a", TokenType.IDENTIFIER)),
                    AST(Token(position, "number", TokenType.INTEGER)),
                ),
            )
        val rightAst =
            AST(
                Token(position, "/", TokenType.OPERATOR),
                listOf(AST(Token(position, "10", TokenType.INTEGER)), AST(Token(position, "2", TokenType.INTEGER))),
            )
        val ast = AST(Token(position, "=", TokenType.ASSIGNATION), listOf(leftAst, rightAst))
        val variableMap = interpreter.interpret(ast)
        assertEquals(1, variableMap.size)
        assertEquals(Variable(TokenType.INTEGER, "5"), variableMap["a"])
    }

    @Test
    fun testDeclareAndAssignStringVariable() {
        val interpreter = Interpreter()
        val position = Position()
        val leftAst =
            AST(
                Token(position, "let", TokenType.DECLARATION),
                listOf(
                    AST(Token(position, "a", TokenType.IDENTIFIER)),
                    AST(Token(position, "string", TokenType.STRING)),
                ),
            )
        val rightAst = AST(Token(position, "hello", TokenType.STRING))
        val ast = AST(Token(position, "=", TokenType.ASSIGNATION), listOf(leftAst, rightAst))
        val variableMap = interpreter.interpret(ast)
        assertEquals(1, variableMap.size)
        assertEquals(Variable(TokenType.STRING, "hello"), variableMap["a"])
    }

    @Test
    fun testDeclareAndAssignStringWithSumVariable() {
        val interpreter = Interpreter()
        val position = Position()
        val leftAst =
            AST(
                Token(position, "let", TokenType.DECLARATION),
                listOf(
                    AST(Token(position, "a", TokenType.IDENTIFIER)),
                    AST(Token(position, "string", TokenType.STRING)),
                ),
            )
        val rightAst =
            AST(
                Token(position, "+", TokenType.OPERATOR),
                listOf(AST(Token(position, "hello", TokenType.STRING)), AST(Token(position, "world", TokenType.STRING))),
            )
        val ast = AST(Token(position, "=", TokenType.ASSIGNATION), listOf(leftAst, rightAst))
        val variableMap = interpreter.interpret(ast)
        assertEquals(1, variableMap.size)
        assertEquals(Variable(TokenType.STRING, "helloworld"), variableMap["a"])
    }

    @Test
    fun testDeclareAndAssignStringWithSumAndMultiplicationNumericVariables() {
        val interpreter = Interpreter()
        val position = Position()
        val leftAst =
            AST(
                Token(position, "let", TokenType.DECLARATION),
                listOf(
                    AST(Token(position, "a", TokenType.IDENTIFIER)),
                    AST(Token(position, "number", TokenType.INTEGER)),
                ),
            )
        val multiplicationAST =
            AST(
                Token(position, "*", TokenType.OPERATOR),
                listOf(AST(Token(position, "5", TokenType.INTEGER)), AST(Token(position, "3", TokenType.INTEGER))),
            )
        val rightAst =
            AST(
                Token(position, "+", TokenType.OPERATOR),
                listOf(AST(Token(position, "5", TokenType.INTEGER)), multiplicationAST),
            )
        val ast = AST(Token(position, "=", TokenType.ASSIGNATION), listOf(leftAst, rightAst))
        val variableMap = interpreter.interpret(ast)
        assertEquals(1, variableMap.size)
        assertEquals(Variable(TokenType.INTEGER, "20"), variableMap["a"])
    }

    @Test
    fun testPrintFunctionWithOnlyOneValue() {
        val interpreter = Interpreter()
        val position = Position()
        val childAst =
            AST(
                Token(position, "8", TokenType.INTEGER),
            )
        val ast = AST(Token(position, "println", TokenType.FUNCTION), listOf(childAst))
        interpreter.interpret(ast)
    }

    @Test
    fun testPrintFunctionWithTwoValuesAndAnOperator() {
        val interpreter = Interpreter()
        val sca = Sca(ArrayList<Rule>())
        val position = Position()
        val rightAst =
            AST(
                Token(position, "+", TokenType.OPERATOR),
                listOf(
                    AST(Token(position, "5", TokenType.INTEGER)),
                    AST(
                        Token(position, "8", TokenType.INTEGER),
                    ),
                ),
            )
        val ast = AST(Token(position, "println", TokenType.FUNCTION), listOf(rightAst))
        interpreter.interpret(ast)
    }

    @Test
    fun testPrintFunctionWithOneIntegerAndOneString() {
        val interpreter = Interpreter()
        val position = Position()
        val rightAst =
            AST(
                Token(position, "+", TokenType.OPERATOR),
                listOf(
                    AST(Token(position, "Result: ", TokenType.STRING)),
                    AST(
                        Token(position, "8", TokenType.INTEGER),
                    ),
                ),
            )
        val ast = AST(Token(position, "println", TokenType.FUNCTION), listOf(rightAst))
        interpreter.interpret(ast)
    }

    @Test
    fun testPrintFunctionWithOneIntegerAndOneStringButBothAppearIntegers() {
        val interpreter = Interpreter()
        val position = Position()
        val rightAst =
            AST(
                Token(position, "+", TokenType.OPERATOR),
                listOf(
                    AST(Token(position, "5", TokenType.STRING)),
                    AST(
                        Token(position, "8", TokenType.INTEGER),
                    ),
                ),
            )
        val ast = AST(Token(position, "println", TokenType.FUNCTION), listOf(rightAst))
        interpreter.interpret(ast)
    }

    @Test
    fun testPrintFunctionWithFourIntegerAndTwoSums() {
        val interpreter = Interpreter()
        val sca = Sca(ArrayList<Rule>())
        val position = Position()
        val rightAst =
            AST(
                Token(position, "+", TokenType.OPERATOR),
                children =
                    listOf(
                        AST(
                            Token(position, "+", TokenType.OPERATOR),
                            children =
                                listOf(
                                    AST((Token(position, "1", TokenType.INTEGER))),
                                    AST((Token(position, "2", TokenType.INTEGER))),
                                ),
                        ),
                        AST(
                            Token(position, "8", TokenType.INTEGER),
                        ),
                    ),
            )
        val ast = AST(Token(position, "println", TokenType.FUNCTION), listOf(rightAst))
        interpreter.interpret(ast)
    }

    @Test
    fun testPrintFunctionWithThreeIntegerAndOneStringAndTwoSums() {
        val interpreter = Interpreter()
        val position = Position()
        val leftAst =
            AST(
                Token(position, "let", TokenType.DECLARATION),
                listOf(
                    AST(Token(position, "a", TokenType.IDENTIFIER)),
                    AST(Token(position, "number", TokenType.INTEGER)),
                ),
            )
        val rightAst2 = AST(Token(position, "5", TokenType.INTEGER))
        val astLetVariable = AST(Token(position, "=", TokenType.ASSIGNATION), listOf(leftAst, rightAst2))
        val rightAst =
            AST(
                Token(position, "+", TokenType.OPERATOR),
                children =
                    listOf(
                        AST(
                            Token(position, "+", TokenType.OPERATOR),
                            children =
                                listOf(
                                    AST((Token(position, "a", TokenType.IDENTIFIER))),
                                    AST((Token(position, "2", TokenType.INTEGER))),
                                ),
                        ),
                        AST(
                            Token(position, "8", TokenType.INTEGER),
                        ),
                    ),
            )
        val variableMap = interpreter.interpret(astLetVariable)
        val ast = AST(Token(position, "println", TokenType.FUNCTION), listOf(rightAst))
        interpreter.printLine(ast, variableMap)
        // The output is 15
    }

    @Test
    fun testPrintFunctionWithOneStringVariableAndOneNumber() {
        val interpreter = Interpreter()
        val position = Position()
        val leftAst =
            AST(
                Token(position, "let", TokenType.DECLARATION),
                listOf(
                    AST(Token(position, "a", TokenType.IDENTIFIER)),
                    AST(Token(position, "string", TokenType.STRING)),
                ),
            )
        val rightAst2 = AST(Token(position, "Result: ", TokenType.STRING))
        val astLetVariable = AST(Token(position, "=", TokenType.ASSIGNATION), listOf(leftAst, rightAst2))
        val rightAst =
            AST(
                Token(position, "+", TokenType.OPERATOR),
                children =
                    listOf(
                        AST(
                            Token(position, "a", TokenType.IDENTIFIER),
                        ),
                        AST(
                            Token(position, "8", TokenType.INTEGER),
                        ),
                    ),
            )
        val variableMap = interpreter.interpret(astLetVariable)
        val ast = AST(Token(position, "println", TokenType.FUNCTION), listOf(rightAst))
        interpreter.printLine(ast, variableMap)
        // The output is Result: 8
    }
}
