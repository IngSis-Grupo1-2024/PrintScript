import components.Position
import components.Token
import components.TokenType
import components.ast.AST
import modules.interpreter.Interpreter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class InterpreterTest {

    @Test
    fun testDeclareNumericVariable() {
        val interpreter = Interpreter()
        val position = Position()
        val ast = AST(
            Token(position, "let", TokenType.DECLARATION),
            listOf(
                AST(Token(position, "a", TokenType.IDENTIFIER)), AST(Token(position, "number", TokenType.TYPE))
            )
        )
        val variableMap = interpreter.addVariableToMap(ast)
        assertEquals(1, variableMap.size)
        assertEquals(Variable(VariableType.NUMBER, null), variableMap["a"])
    }

    @Test
    fun testDeclareStringVariable() {
        val interpreter = Interpreter()
        val position = Position()
        val ast = AST(
            Token(position, "let", TokenType.DECLARATION),
            listOf(
                AST(Token(position, "a", TokenType.IDENTIFIER)), AST(Token(position, "string", TokenType.TYPE))
            )
        )
        val variableMap = interpreter.addVariableToMap(ast)
        assertEquals(1, variableMap.size)
        assertEquals(Variable(VariableType.STRING, null), variableMap["a"])
    }

    @Test
    fun testDeclareAndAssignNumericVariable() {
        val interpreter = Interpreter()
        val position = Position()
        val leftAst = AST(
            Token(position, "let", TokenType.DECLARATION),
            listOf(
                AST(Token(position, "a", TokenType.IDENTIFIER)), AST(Token(position, "number", TokenType.TYPE))
            )
        )
        val rightAst = AST(Token(position, "5", TokenType.VALUE))
        val ast = AST(Token(position, "=", TokenType.ASSIGNATION), listOf(leftAst, rightAst))
        val variableMap = interpreter.addVariableToMap(ast)
        assertEquals(1, variableMap.size)
        assertEquals(Variable(VariableType.NUMBER, "5"), variableMap["a"])
    }

    @Test
    fun testDeclareAndAssignNumericWithSumVariable() {
        val interpreter = Interpreter()
        val position = Position()
        val leftAst = AST(
            Token(position, "let", TokenType.DECLARATION),
            listOf(
                AST(Token(position, "a", TokenType.IDENTIFIER)), AST(Token(position, "number", TokenType.TYPE))
            )
        )
        val rightAst = AST(
            Token(position, "+", TokenType.OPERATOR),
            listOf(AST(Token(position, "5", TokenType.VALUE)), AST(Token(position, "3", TokenType.VALUE)))
        )
        val ast = AST(Token(position, "=", TokenType.ASSIGNATION), listOf(leftAst, rightAst))
        val variableMap = interpreter.addVariableToMap(ast)
        assertEquals(1, variableMap.size)
        assertEquals(Variable(VariableType.NUMBER, "8"), variableMap["a"])
    }

    @Test
    fun testDeclareAndAssignNumericWithSubtractionVariable() {
        val interpreter = Interpreter()
        val position = Position()
        val leftAst = AST(
            Token(position, "let", TokenType.DECLARATION),
            listOf(
                AST(Token(position, "a", TokenType.IDENTIFIER)), AST(Token(position, "number", TokenType.TYPE))
            )
        )
        val rightAst = AST(
            Token(position, "-", TokenType.OPERATOR),
            listOf(AST(Token(position, "5", TokenType.VALUE)), AST(Token(position, "3", TokenType.VALUE)))
        )
        val ast = AST(Token(position, "=", TokenType.ASSIGNATION), listOf(leftAst, rightAst))
        val variableMap = interpreter.addVariableToMap(ast)
        assertEquals(1, variableMap.size)
        assertEquals(Variable(VariableType.NUMBER, "2"), variableMap["a"])
    }

    @Test
    fun testDeclareAndAssignNumericWithMultiplicationVariable() {
        val interpreter = Interpreter()
        val position = Position()
        val leftAst = AST(
            Token(position, "let", TokenType.DECLARATION),
            listOf(
                AST(Token(position, "a", TokenType.IDENTIFIER)), AST(Token(position, "number", TokenType.TYPE))
            )
        )
        val rightAst = AST(
            Token(position, "*", TokenType.OPERATOR),
            listOf(AST(Token(position, "5", TokenType.VALUE)), AST(Token(position, "3", TokenType.VALUE)))
        )
        val ast = AST(Token(position, "=", TokenType.ASSIGNATION), listOf(leftAst, rightAst))
        val variableMap = interpreter.addVariableToMap(ast)
        assertEquals(1, variableMap.size)
        assertEquals(Variable(VariableType.NUMBER, "15"), variableMap["a"])
    }

    @Test
    fun testDeclareAndAssignNumericWithDivisionVariable() {
        val interpreter = Interpreter()
        val position = Position()
        val leftAst = AST(
            Token(position, "let", TokenType.DECLARATION),
            listOf(
                AST(Token(position, "a", TokenType.IDENTIFIER)), AST(Token(position, "number", TokenType.TYPE))
            )
        )
        val rightAst = AST(
            Token(position, "/", TokenType.OPERATOR),
            listOf(AST(Token(position, "10", TokenType.VALUE)), AST(Token(position, "2", TokenType.VALUE)))
        )
        val ast = AST(Token(position, "=", TokenType.ASSIGNATION), listOf(leftAst, rightAst))
        val variableMap = interpreter.addVariableToMap(ast)
        assertEquals(1, variableMap.size)
        assertEquals(Variable(VariableType.NUMBER, "5"), variableMap["a"])
    }

    @Test
    fun testDeclareAndAssignStringVariable() {
        val interpreter = Interpreter()
        val position = Position()
        val leftAst = AST(
            Token(position, "let", TokenType.DECLARATION),
            listOf(
                AST(Token(position, "a", TokenType.IDENTIFIER)), AST(Token(position, "string", TokenType.TYPE))
            )
        )
        val rightAst = AST(Token(position, "hello", TokenType.VALUE))
        val ast = AST(Token(position, "=", TokenType.ASSIGNATION), listOf(leftAst, rightAst))
        val variableMap = interpreter.addVariableToMap(ast)
        assertEquals(1, variableMap.size)
        assertEquals(Variable(VariableType.STRING, "hello"), variableMap["a"])
    }

    @Test
    fun testDeclareAndAssignStringWithSumVariable() {
        val interpreter = Interpreter()
        val position = Position()
        val leftAst = AST(
            Token(position, "let", TokenType.DECLARATION),
            listOf(
                AST(Token(position, "a", TokenType.IDENTIFIER)), AST(Token(position, "string", TokenType.TYPE))
            )
        )
        val rightAst = AST(
            Token(position, "+", TokenType.OPERATOR),
            listOf(AST(Token(position, "hello", TokenType.VALUE)), AST(Token(position, "world", TokenType.VALUE)))
        )
        val ast = AST(Token(position, "=", TokenType.ASSIGNATION), listOf(leftAst, rightAst))
        val variableMap = interpreter.addVariableToMap(ast)
        assertEquals(1, variableMap.size)
        assertEquals(Variable(VariableType.STRING, "helloworld"), variableMap["a"])
    }

    @Test
    fun testDeclareAndAssignStringWithSumAndMultiplicationNumericVariables() {
        val interpreter = Interpreter()
        val position = Position()
        val leftAst = AST(
            Token(position, "let", TokenType.DECLARATION),
            listOf(
                AST(Token(position, "a", TokenType.IDENTIFIER)), AST(Token(position, "number", TokenType.TYPE))
            )
        )
        val multiplicationAST = AST(
            Token(position, "*", TokenType.OPERATOR),
            listOf(AST(Token(position, "5", TokenType.VALUE)), AST(Token(position, "3", TokenType.VALUE)))
        )
        val rightAst = AST(
            Token(position, "+", TokenType.OPERATOR),
            listOf(AST(Token(position, "5", TokenType.VALUE)), multiplicationAST)
        )
        val ast = AST(Token(position, "=", TokenType.ASSIGNATION), listOf(leftAst, rightAst))
        val variableMap = interpreter.addVariableToMap(ast)
        assertEquals(1, variableMap.size)
        assertEquals(Variable(VariableType.NUMBER, "20"), variableMap["a"])
    }

}

