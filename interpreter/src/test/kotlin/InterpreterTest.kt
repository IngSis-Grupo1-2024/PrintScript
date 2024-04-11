import components.Position
import components.Token
import components.TokenType
import components.statement.*
import ingsis.interpreter.Interpreter
import ingsis.interpreter.interpretStatement.AssignationInterpreter
import ingsis.interpreter.interpretStatement.CompoundAssignationInterpreter
import ingsis.interpreter.interpretStatement.DeclarationInterpreter
import ingsis.interpreter.interpretStatement.PrintLineInterpreter
import ingsis.utils.Result
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class InterpreterTest {
    @Test
    fun testDeclareNumericVariable() {
        val interpreter =
            Interpreter(listOf(AssignationInterpreter(), DeclarationInterpreter(), CompoundAssignationInterpreter()))
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type("integer", position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val variableMap = interpreter.interpret(declarationStatement, HashMap())
        assertEquals(1, variableMap.size)
        assertEquals(Result(type, null), variableMap["a"])
    }

    @Test
    fun testDeclareAndThenAssignValueToAVariable() {
        val interpreter =
            Interpreter(listOf(AssignationInterpreter(), DeclarationInterpreter(), CompoundAssignationInterpreter()))
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type("integer", position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val variableMap1 = interpreter.interpret(declarationStatement, HashMap())
        val assignationStatement = Assignation(position, variable, SingleValue(Token(position, "5", TokenType.INTEGER)))
        val variableMap2 = interpreter.interpret(assignationStatement, variableMap1)
        assertEquals(1, variableMap2.size)
        assertEquals(Result(type, "5"), variableMap2["a"])
    }

    @Test
    fun testDeclareStringVariable() {
        val interpreter =
            Interpreter(listOf(AssignationInterpreter(), DeclarationInterpreter(), CompoundAssignationInterpreter()))
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type("string", position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val variableMap = interpreter.interpret(declarationStatement, HashMap())
        assertEquals(1, variableMap.size)
        assertEquals(Result(type, null), variableMap["a"])
    }

    @Test
    fun testDeclareAndAssignNumericVariable() {
        val interpreter =
            Interpreter(listOf(AssignationInterpreter(), DeclarationInterpreter(), CompoundAssignationInterpreter()))
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type("integer", position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(position, declarationStatement, SingleValue(Token(position, "5", TokenType.INTEGER)))
        val variableMap = interpreter.interpret(compoundAssignation, HashMap())
        assertEquals(1, variableMap.size)
        assertEquals(Result(type, "5"), variableMap["a"])
    }

    @Test
    fun testDeclareAndAssignANumericVariablesWithSum() {
        val interpreter =
            Interpreter(listOf(AssignationInterpreter(), DeclarationInterpreter(), CompoundAssignationInterpreter()))
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type("integer", position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(
                position,
                declarationStatement,
                Operator(
                    Token(position, "+", TokenType.OPERATOR),
                    SingleValue(Token(position, "5", TokenType.INTEGER)),
                    SingleValue(Token(position, "3", TokenType.INTEGER)),
                ),
            )
        val variableMap = interpreter.interpret(compoundAssignation, HashMap())
        assertEquals(1, variableMap.size)
        assertEquals(Result(type, "8"), variableMap["a"])
    }

    @Test
    fun testDeclareAndAssignANumericVariablesWithSubtraction() {
        val interpreter =
            Interpreter(listOf(AssignationInterpreter(), DeclarationInterpreter(), CompoundAssignationInterpreter()))
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type("integer", position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(
                position,
                declarationStatement,
                Operator(
                    Token(position, "-", TokenType.OPERATOR),
                    SingleValue(Token(position, "5", TokenType.INTEGER)),
                    SingleValue(Token(position, "3", TokenType.INTEGER)),
                ),
            )
        val variableMap = interpreter.interpret(compoundAssignation, HashMap())
        assertEquals(1, variableMap.size)
        assertEquals(Result(type, "2"), variableMap["a"])
    }

    @Test
    fun testDeclareAndAssignANumericVariablesWithMultiplication() {
        val interpreter =
            Interpreter(listOf(AssignationInterpreter(), DeclarationInterpreter(), CompoundAssignationInterpreter()))
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type("integer", position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(
                position,
                declarationStatement,
                Operator(
                    Token(position, "*", TokenType.OPERATOR),
                    SingleValue(Token(position, "5", TokenType.INTEGER)),
                    SingleValue(Token(position, "3", TokenType.INTEGER)),
                ),
            )
        val variableMap = interpreter.interpret(compoundAssignation, HashMap())
        assertEquals(1, variableMap.size)
        assertEquals(Result(type, "15"), variableMap["a"])
    }

    @Test
    fun testDeclareAndAssignANumericVariablesWithDivision() {
        val interpreter =
            Interpreter(listOf(AssignationInterpreter(), DeclarationInterpreter(), CompoundAssignationInterpreter()))
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type("integer", position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(
                position,
                declarationStatement,
                Operator(
                    Token(position, "/", TokenType.OPERATOR),
                    SingleValue(Token(position, "10", TokenType.INTEGER)),
                    SingleValue(Token(position, "2", TokenType.INTEGER)),
                ),
            )
        val variableMap = interpreter.interpret(compoundAssignation, HashMap())
        assertEquals(1, variableMap.size)
        assertEquals(Result(type, "5"), variableMap["a"])
    }

    @Test
    fun testDeclareAndAssignANumericVariablesWithMultipleSum() {
        val interpreter =
            Interpreter(listOf(AssignationInterpreter(), DeclarationInterpreter(), CompoundAssignationInterpreter()))
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type("integer", position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(
                position,
                declarationStatement,
                Operator(
                    Token(position, "+", TokenType.OPERATOR),
                    SingleValue(Token(position, "5", TokenType.INTEGER)),
                    Operator(
                        Token(position, "+", TokenType.OPERATOR),
                        SingleValue(Token(position, "5", TokenType.INTEGER)),
                        SingleValue(Token(position, "3", TokenType.INTEGER)),
                    ),
                ),
            )
        val variableMap = interpreter.interpret(compoundAssignation, HashMap())
        assertEquals(1, variableMap.size)
        assertEquals(Result(type, "13"), variableMap["a"])
    }

    @Test
    fun testDeclareAndAssignANumericVariablesWithMultipleSumAndMultiplication() {
        val interpreter =
            Interpreter(listOf(AssignationInterpreter(), DeclarationInterpreter(), CompoundAssignationInterpreter()))
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type("integer", position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(
                position,
                declarationStatement,
                Operator(
                    Token(position, "+", TokenType.OPERATOR),
                    SingleValue(Token(position, "5", TokenType.INTEGER)),
                    Operator(
                        Token(position, "*", TokenType.OPERATOR),
                        SingleValue(Token(position, "5", TokenType.INTEGER)),
                        SingleValue(Token(position, "3", TokenType.INTEGER)),
                    ),
                ),
            )
        val variableMap = interpreter.interpret(compoundAssignation, HashMap())
        assertEquals(1, variableMap.size)
        assertEquals(Result(type, "20"), variableMap["a"])
    }

    @Test
    fun testDeclareAndAssignANumericVariablesWithSumAndSubtraction() {
        val interpreter =
            Interpreter(listOf(AssignationInterpreter(), DeclarationInterpreter(), CompoundAssignationInterpreter()))
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type("integer", position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(
                position,
                declarationStatement,
                Operator(
                    Token(position, "+", TokenType.OPERATOR),
                    SingleValue(Token(position, "5", TokenType.INTEGER)),
                    Operator(
                        Token(position, "-", TokenType.OPERATOR),
                        SingleValue(Token(position, "5", TokenType.INTEGER)),
                        SingleValue(Token(position, "3", TokenType.INTEGER)),
                    ),
                ),
            )
        val variableMap = interpreter.interpret(compoundAssignation, HashMap())
        assertEquals(1, variableMap.size)
        assertEquals(Result(type, "7"), variableMap["a"])
    }

    @Test
    fun testDeclareAndAssignANumericVariablesWithSumAndDivision() {
        val interpreter =
            Interpreter(listOf(AssignationInterpreter(), DeclarationInterpreter(), CompoundAssignationInterpreter()))
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type("integer", position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(
                position,
                declarationStatement,
                Operator(
                    Token(position, "+", TokenType.OPERATOR),
                    SingleValue(Token(position, "5", TokenType.INTEGER)),
                    Operator(
                        Token(position, "+", TokenType.OPERATOR),
                        SingleValue(Token(position, "5", TokenType.INTEGER)),
                        Operator(
                            Token(position, "/", TokenType.OPERATOR),
                            SingleValue(Token(position, "10", TokenType.INTEGER)),
                            SingleValue(Token(position, "2", TokenType.INTEGER)),
                        ),
                    ),
                ),
            )
        val variableMap = interpreter.interpret(compoundAssignation, HashMap())
        assertEquals(1, variableMap.size)
        assertEquals(Result(type, "15"), variableMap["a"])
    }

    @Test
    fun testDeclareAndAssignANumericVariablesWithTwoSumsAndMultiplication() {
        val interpreter =
            Interpreter(listOf(AssignationInterpreter(), DeclarationInterpreter(), CompoundAssignationInterpreter()))
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type("integer", position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(
                position,
                declarationStatement,
                Operator(
                    Token(position, "+", TokenType.OPERATOR),
                    SingleValue(Token(position, "5", TokenType.INTEGER)),
                    Operator(
                        Token(position, "+", TokenType.OPERATOR),
                        SingleValue(Token(position, "5", TokenType.INTEGER)),
                        Operator(
                            Token(position, "*", TokenType.OPERATOR),
                            SingleValue(Token(position, "5", TokenType.INTEGER)),
                            SingleValue(Token(position, "3", TokenType.INTEGER)),
                        ),
                    ),
                ),
            )
        val variableMap = interpreter.interpret(compoundAssignation, HashMap())
        assertEquals(1, variableMap.size)
        assertEquals(Result(type, "25"), variableMap["a"])
    }

    @Test
    fun testDeclareAndAssignAVariableSummingAStringAndANumber() {
        val interpreter =
            Interpreter(listOf(AssignationInterpreter(), DeclarationInterpreter(), CompoundAssignationInterpreter()))
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type("string", position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(
                position,
                declarationStatement,
                Operator(
                    Token(position, "+", TokenType.OPERATOR),
                    SingleValue(Token(position, "Hello", TokenType.STRING)),
                    SingleValue(Token(position, "5", TokenType.INTEGER)),
                ),
            )
        val variableMap = interpreter.interpret(compoundAssignation, HashMap())
        assertEquals(1, variableMap.size)
        assertEquals(Result(type, "Hello5"), variableMap["a"])
    }

    @Test
    fun testDeclareAndAssignAVariableSummingTwoStrings() {
        val interpreter =
            Interpreter(listOf(AssignationInterpreter(), DeclarationInterpreter(), CompoundAssignationInterpreter()))
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type("string", position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(
                position,
                declarationStatement,
                Operator(
                    Token(position, "+", TokenType.OPERATOR),
                    SingleValue(Token(position, "Hello", TokenType.STRING)),
                    SingleValue(Token(position, "World", TokenType.STRING)),
                ),
            )
        val variableMap = interpreter.interpret(compoundAssignation, HashMap())
        assertEquals(1, variableMap.size)
        assertEquals(Result(type, "HelloWorld"), variableMap["a"])
    }

    @Test
    fun testPrintLineWithAString() {
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(),
                    PrintLineInterpreter(),
                ),
            )
        val position = Position()
        val printLine = PrintLine(position, (SingleValue(Token(position, "Hello", TokenType.STRING))))
        println()
        println()
        interpreter.interpret(printLine, HashMap())
        println()
        println()
    }

    @Test
    fun testPrintLineWithAnInteger() {
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(),
                    PrintLineInterpreter(),
                ),
            )
        val position = Position()
        val printLine = PrintLine(position, (SingleValue(Token(position, "5", TokenType.INTEGER))))
        println()
        println()
        interpreter.interpret(printLine, HashMap())
        println()
        println()
    }

    @Test
    fun testPrintLineWithAVariable() {
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(),
                    PrintLineInterpreter(),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type("integer", position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(
                position,
                declarationStatement,
                SingleValue(Token(position, "10", TokenType.INTEGER)),
            )
        val variableMap = interpreter.interpret(compoundAssignation, HashMap())
        val printLine = PrintLine(position, (SingleValue(Token(position, "a", TokenType.IDENTIFIER))))
        println()
        println()
        interpreter.interpret(printLine, variableMap)
        println()
        println()
    }

    @Test
    fun testPrintLineWithASum() {
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(),
                    PrintLineInterpreter(),
                ),
            )
        val position = Position()
        val printLine =
            PrintLine(
                position,
                Operator(
                    Token(position, "+", TokenType.OPERATOR),
                    SingleValue(Token(position, "5", TokenType.INTEGER)),
                    SingleValue(Token(position, "3", TokenType.INTEGER)),
                ),
            )
        println()
        println()
        interpreter.interpret(printLine, HashMap())
        println()
        println()
    }

    @Test
    fun testPrintLineWithASubtraction() {
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(),
                    PrintLineInterpreter(),
                ),
            )
        val position = Position()
        val printLine =
            PrintLine(
                position,
                Operator(
                    Token(position, "-", TokenType.OPERATOR),
                    SingleValue(Token(position, "5", TokenType.INTEGER)),
                    SingleValue(Token(position, "3", TokenType.INTEGER)),
                ),
            )
        println()
        println()
        interpreter.interpret(printLine, HashMap())
        println()
        println()
    }

    @Test
    fun testPrintLineWithAMultiplication() {
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(),
                    PrintLineInterpreter(),
                ),
            )
        val position = Position()
        val printLine =
            PrintLine(
                position,
                Operator(
                    Token(position, "*", TokenType.OPERATOR),
                    SingleValue(Token(position, "5", TokenType.INTEGER)),
                    SingleValue(Token(position, "3", TokenType.INTEGER)),
                ),
            )
        println()
        println()
        interpreter.interpret(printLine, HashMap())
        println()
        println()
    }

    @Test
    fun testPrintLineWithADivision() {
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(),
                    PrintLineInterpreter(),
                ),
            )
        val position = Position()
        val printLine =
            PrintLine(
                position,
                Operator(
                    Token(position, "/", TokenType.OPERATOR),
                    SingleValue(Token(position, "10", TokenType.INTEGER)),
                    SingleValue(Token(position, "2", TokenType.INTEGER)),
                ),
            )
        println()
        println()
        interpreter.interpret(printLine, HashMap())
        println()
        println()
    }

    @Test
    fun testPrintLineWithASumWithVariables() {
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(),
                    PrintLineInterpreter(),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variableA = Variable("a", position)
        val type = Type("integer", position)
        val declarationStatementA = Declaration(keyword, variableA, type, position)
        val compoundAssignationVarA =
            CompoundAssignation(
                position,
                declarationStatementA,
                SingleValue(Token(position, "10", TokenType.INTEGER)),
            )
        val variableB = Variable("b", position)
        val declarationStatementB = Declaration(keyword, variableB, type, position)
        val compoundAssignationVarB =
            CompoundAssignation(
                position,
                declarationStatementB,
                SingleValue(Token(position, "20", TokenType.INTEGER)),
            )
        val printLine =
            PrintLine(
                position,
                Operator(
                    Token(position, "+", TokenType.OPERATOR),
                    SingleValue(Token(position, "a", TokenType.IDENTIFIER)),
                    SingleValue(Token(position, "b", TokenType.IDENTIFIER)),
                ),
            )
        var map = HashMap<String, Result>()
        map = interpreter.interpret(compoundAssignationVarA, map)
        map = interpreter.interpret(compoundAssignationVarB, map)
        println()
        println()
        interpreter.interpret(printLine, map)
        println()
        println()
    }

//
    @Test
    fun testPrintLineAVariable() {
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(),
                    PrintLineInterpreter(),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variableA = Variable("a", position)
        val type = Type("integer", position)
        val declarationStatementA = Declaration(keyword, variableA, type, position)
        val compoundAssignationVarA =
            CompoundAssignation(
                position,
                declarationStatementA,
                SingleValue(Token(position, "10", TokenType.INTEGER)),
            )
        val variableB = Variable("b", position)
        val declarationStatementB = Declaration(keyword, variableB, type, position)
        val compoundAssignationVarB =
            CompoundAssignation(
                position,
                declarationStatementB,
                SingleValue(Token(position, "a", TokenType.IDENTIFIER)),
            )
        val printLine =
            PrintLine(
                position,
                SingleValue(Token(position, "b", TokenType.IDENTIFIER)),
            )
        var map = HashMap<String, Result>()
        map = interpreter.interpret(compoundAssignationVarA, map)
        map = interpreter.interpret(compoundAssignationVarB, map)
        println()
        println()
        interpreter.interpret(printLine, map)
        println()
        println()
    }

    @Test
    fun testPrintLineWithADifferenceWithVariablesWithTypeString() {
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(),
                    PrintLineInterpreter(),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variableA = Variable("a", position)
        val type = Type("string", position)
        val declarationStatementA = Declaration(keyword, variableA, type, position)
        val compoundAssignationVarA =
            CompoundAssignation(
                position,
                declarationStatementA,
                SingleValue(Token(position, "hola", TokenType.STRING)),
            )
        val variableB = Variable("b", position)
        val declarationStatementB = Declaration(keyword, variableB, type, position)
        val compoundAssignationVarB =
            CompoundAssignation(
                position,
                declarationStatementB,
                SingleValue(Token(position, "chau", TokenType.STRING)),
            )
        val printLine =
            PrintLine(
                position,
                Operator(
                    Token(position, "+", TokenType.OPERATOR),
                    SingleValue(Token(position, "a", TokenType.IDENTIFIER)),
                    SingleValue(Token(position, "b", TokenType.IDENTIFIER)),
                ),
            )
        var map = HashMap<String, Result>()
        map = interpreter.interpret(compoundAssignationVarA, map)
        map = interpreter.interpret(compoundAssignationVarB, map)
        println()
        println()
        interpreter.interpret(printLine, map)
        println()
        println()
    }
}
