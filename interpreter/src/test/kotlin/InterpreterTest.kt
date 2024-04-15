
import com.sun.jdi.InvalidTypeException
import ingsis.components.Position
import ingsis.components.Token
import ingsis.components.TokenType
import ingsis.components.statement.*
import ingsis.interpreter.Interpreter
import ingsis.interpreter.interpretStatement.AssignationInterpreter
import ingsis.interpreter.interpretStatement.CompoundAssignationInterpreter
import ingsis.interpreter.interpretStatement.DeclarationInterpreter
import ingsis.interpreter.interpretStatement.PrintLineInterpreter
import ingsis.interpreter.operatorScanner.ScanDivOperator
import ingsis.interpreter.operatorScanner.ScanMulOperator
import ingsis.interpreter.operatorScanner.ScanSubOperator
import ingsis.interpreter.operatorScanner.ScanSumOperator
import ingsis.utils.Result
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class InterpreterTest {
    @Test
    fun testDeclareNumericVariable() {
        val scanners = listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator())
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.INTEGER, position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val pair = interpreter.interpret(declarationStatement, HashMap())
        val variableMap = pair.first
        assertEquals(1, variableMap.size)
        assertEquals(Result(type, null), variableMap["a"])
    }

    @Test
    fun testDeclareAndThenAssignValueToAVariable() {
        val scanners = listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator())
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.INTEGER, position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val pair = interpreter.interpret(declarationStatement, HashMap())
        val variableMap1 = pair.first
        val assignationStatement = Assignation(position, variable, SingleValue(Token(position, "5", TokenType.INTEGER)))
        val pair1 = interpreter.interpret(assignationStatement, variableMap1)
        val variableMap2 = pair1.first
        assertEquals(1, variableMap2.size)
        assertEquals(Result(type, "5"), variableMap2["a"])
    }

    @Test
    fun testDeclareStringVariable() {
        val scanners = listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator())
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.STRING, position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val pair = interpreter.interpret(declarationStatement, HashMap())
        val variableMap = pair.first
        assertEquals(1, variableMap.size)
        assertEquals(Result(type, null), variableMap["a"])
    }

    @Test
    fun testDeclareAndAssignNumericVariable() {
        val scanners = listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator())
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.INTEGER, position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(position, declarationStatement, SingleValue(Token(position, "5", TokenType.INTEGER)))
        val pair = interpreter.interpret(compoundAssignation, HashMap())
        val variableMap = pair.first
        assertEquals(1, variableMap.size)
        assertEquals(Result(type, "5"), variableMap["a"])
    }

    @Test
    fun testDeclareAndAssignANumericVariablesWithSum() {
        val scanners = listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator())
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.INTEGER, position)
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
        val pair = interpreter.interpret(compoundAssignation, HashMap())
        val variableMap = pair.first
        assertEquals(1, variableMap.size)
        assertEquals(Result(type, "8"), variableMap["a"])
    }

    @Test
    fun testDeclareAndAssignANumericVariablesWithSubtraction() {
        val scanners = listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator())
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.INTEGER, position)
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
        val pair = interpreter.interpret(compoundAssignation, HashMap())
        val variableMap = pair.first
        assertEquals(1, variableMap.size)
        assertEquals(Result(type, "2"), variableMap["a"])
    }

    @Test
    fun testDeclareAndAssignANumericVariablesWithSubtractionUsingStringAndInt() {
        val scanners = listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator())
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.INTEGER, position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(
                position,
                declarationStatement,
                Operator(
                    Token(position, "-", TokenType.OPERATOR),
                    SingleValue(Token(position, "5", TokenType.INTEGER)),
                    SingleValue(Token(position, "3", TokenType.STRING)),
                ),
            )
        val exception = assertThrows<Error> { interpreter.interpret(compoundAssignation, HashMap()) }
        assertEquals(
            "Can't do subtraction using no integer types in line " + position.startLine + " at position " + position.startColumn,
            exception.message,
        )
    }

    @Test
    fun testDeclareAndAssignANumericVariablesWithMultiplication() {
        val scanners = listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator())
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.INTEGER, position)
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
        val pair = interpreter.interpret(compoundAssignation, HashMap())
        val variableMap = pair.first
        assertEquals(1, variableMap.size)
        assertEquals(Result(type, "15"), variableMap["a"])
    }

    @Test
    fun testDeclareAndAssignANumericVariablesWithMultiplicationBetweenStrings() {
        val scanners = listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator())
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.INTEGER, position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(
                position,
                declarationStatement,
                Operator(
                    Token(position, "*", TokenType.OPERATOR),
                    SingleValue(Token(position, "5", TokenType.STRING)),
                    SingleValue(Token(position, "3", TokenType.STRING)),
                ),
            )

        val exception = assertThrows<Error> { interpreter.interpret(compoundAssignation, HashMap()) }
        assertEquals(
            "Can't do multiplication using no integer types in line " + position.startLine + " at position " + position.startColumn,
            exception.message,
        )
    }

    @Test
    fun testDeclareAndAssignANumericVariablesWithDivision() {
        val scanners = listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator())
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.INTEGER, position)
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
        val pair = interpreter.interpret(compoundAssignation, HashMap())
        val variableMap = pair.first
        assertEquals(1, variableMap.size)
        assertEquals(Result(type, "5"), variableMap["a"])
    }

    @Test
    fun testDeclareAndAssignANumericVariablesWithDivisionUsingStringAndInteger() {
        val scanners = listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator())
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.INTEGER, position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(
                position,
                declarationStatement,
                Operator(
                    Token(position, "/", TokenType.OPERATOR),
                    SingleValue(Token(position, "10", TokenType.STRING)),
                    SingleValue(Token(position, "2", TokenType.INTEGER)),
                ),
            )

        val exception = assertThrows<InvalidTypeException> { interpreter.interpret(compoundAssignation, HashMap()) }
        assertEquals(
            "Can't do division using no integer types in line " + position.startLine + " at position " + position.startColumn,
            exception.message,
        )
    }

    @Test
    fun testDeclareAndAssignANumericVariablesWithMultipleSum() {
        val scanners = listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator())
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.INTEGER, position)
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
        val pair = interpreter.interpret(compoundAssignation, HashMap())
        val variableMap = pair.first
        assertEquals(1, variableMap.size)
        assertEquals(Result(type, "13"), variableMap["a"])
    }

    @Test
    fun testDeclareAndAssignANumericVariablesWithMultipleSumAndMultiplication() {
        val scanners = listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator())
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.INTEGER, position)
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
        val pair = interpreter.interpret(compoundAssignation, HashMap())
        val variableMap = pair.first
        assertEquals(1, variableMap.size)
        assertEquals(Result(type, "20"), variableMap["a"])
    }

    @Test
    fun testDeclareAndAssignANumericVariablesWithSumAndSubtraction() {
        val scanners = listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator())
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.INTEGER, position)
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
        val pair = interpreter.interpret(compoundAssignation, HashMap())
        val variableMap = pair.first
        assertEquals(1, variableMap.size)
        assertEquals(Result(type, "7"), variableMap["a"])
    }

    @Test
    fun testDeclareAndAssignANumericVariablesWithSumAndDivision() {
        val scanners = listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator())
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.INTEGER, position)
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
        val pair = interpreter.interpret(compoundAssignation, HashMap())
        val variableMap = pair.first
        assertEquals(1, variableMap.size)
        assertEquals(Result(type, "15"), variableMap["a"])
    }

    @Test
    fun testDeclareAndAssignANumericVariablesWithTwoSumsAndMultiplication() {
        val scanners = listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator())
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.INTEGER, position)
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
        val pair = interpreter.interpret(compoundAssignation, HashMap())
        val variableMap = pair.first
        assertEquals(1, variableMap.size)
        assertEquals(Result(type, "25"), variableMap["a"])
    }

    @Test
    fun testDeclareAndAssignAVariableSummingAStringAndANumber() {
        val scanners = listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator())
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.STRING, position)
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
        val pair = interpreter.interpret(compoundAssignation, HashMap())
        val variableMap = pair.first
        assertEquals(1, variableMap.size)
        assertEquals(Result(type, "Hello5"), variableMap["a"])
    }

    @Test
    fun testDeclareAndAssignAVariableSummingTwoStrings() {
        val scanners = listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator())
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.STRING, position)
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
        val pair = interpreter.interpret(compoundAssignation, HashMap())
        val variableMap = pair.first
        assertEquals(1, variableMap.size)
        assertEquals(Result(type, "HelloWorld"), variableMap["a"])
    }

    @Test
    fun testPrintLineWithAString() {
        val scanners = listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator())
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                    PrintLineInterpreter(scanners),
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
        val scanners = listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator())
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                    PrintLineInterpreter(scanners),
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
        val scanners = listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator())
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                    PrintLineInterpreter(scanners),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.INTEGER, position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(
                position,
                declarationStatement,
                SingleValue(Token(position, "10", TokenType.INTEGER)),
            )
        val variableMap = interpreter.interpret(compoundAssignation, HashMap()).first
        val printLine = PrintLine(position, (SingleValue(Token(position, "a", TokenType.IDENTIFIER))))
        println()
        println()
        val pair = interpreter.interpret(printLine, variableMap)
        println(pair.second)
        println()
        println()
    }

    @Test
    fun testPrintLineWithASum() {
        val scanners = listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator())
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                    PrintLineInterpreter(scanners),
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
        val scanners = listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator())
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                    PrintLineInterpreter(scanners),
                ),
            )
        val position = Position()
        val printLine =
            PrintLine(
                position,
                Operator(
                    Token(position, "-", TokenType.OPERATOR),
                    SingleValue(Token(position, "3", TokenType.INTEGER)),
                    SingleValue(Token(position, "5", TokenType.INTEGER)),
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
        val scanners = listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator())
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                    PrintLineInterpreter(scanners),
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
        val scanners = listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator())
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                    PrintLineInterpreter(scanners),
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
        val scanners = listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator())
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                    PrintLineInterpreter(scanners),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variableA = Variable("a", position)
        val type = Type(TokenType.INTEGER, position)
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
        map = interpreter.interpret(compoundAssignationVarA, map).first
        map = interpreter.interpret(compoundAssignationVarB, map).first
        println()
        println()
        interpreter.interpret(printLine, map)
        println()
        println()
    }

    @Test
    fun testPrintLineAVariable() {
        val scanners = listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator())
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                    PrintLineInterpreter(scanners),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variableA = Variable("a", position)
        val type = Type(TokenType.INTEGER, position)
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
        map = interpreter.interpret(compoundAssignationVarA, map).first
        map = interpreter.interpret(compoundAssignationVarB, map).first
        println()
        println()
        interpreter.interpret(printLine, map)
        println()
        println()
    }

    @Test
    fun testPrintLineWithADifferenceWithVariablesWithTypeString() {
        val scanners = listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator())
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                    PrintLineInterpreter(scanners),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variableA = Variable("a", position)
        val type = Type(TokenType.STRING, position)
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
        map = interpreter.interpret(compoundAssignationVarA, map).first
        map = interpreter.interpret(compoundAssignationVarB, map).first
        println()
        println()
        interpreter.interpret(printLine, map)
        println()
        println()
    }

    @Test
    fun testErrorWhenAssigningAStringToANumericVariable() {
        val scanners = listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator())
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.INTEGER, position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(
                position,
                declarationStatement,
                SingleValue(Token(position, "8", TokenType.INTEGER)),
            )
        interpreter.interpret(compoundAssignation, HashMap())
        val assignation = Assignation(position, variable, SingleValue(Token(position, "Hello", TokenType.STRING)))
        val exception = assertThrows<Exception> { interpreter.interpret(assignation, HashMap()) }
        assertEquals(
            "Type mismatch",
            exception.message,
        )
    }

    @Test
    fun testErrorWhenAssigningANumberToAStringVariable() {
        val scanners = listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator())
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.STRING, position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(
                position,
                declarationStatement,
                SingleValue(Token(position, "hello world", TokenType.STRING)),
            )
        interpreter.interpret(compoundAssignation, HashMap())
        val assignation = Assignation(position, variable, SingleValue(Token(position, "8", TokenType.INTEGER)))
        val exception = assertThrows<Exception> { interpreter.interpret(assignation, HashMap()) }
        assertEquals(
            "Type mismatch",
            exception.message,
        )
    }

    @Test
    fun testCompoundAssignationNumericTypeDoesNotMatchStringVariableType() {
        val scanners = listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator())
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.INTEGER, position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(
                position,
                declarationStatement,
                SingleValue(Token(position, "hello world", TokenType.STRING)),
            )
        val exception = assertThrows<Exception> { interpreter.interpret(compoundAssignation, HashMap()) }
        assertEquals(
            "Type mismatch",
            exception.message,
        )
    }

    @Test
    fun testCompoundAssignationStringTypeDoesNotMatchNumericVariableType() {
        val scanners = listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator())
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.STRING, position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(
                position,
                declarationStatement,
                SingleValue(Token(position, "8", TokenType.INTEGER)),
            )
        val exception = assertThrows<Exception> { interpreter.interpret(compoundAssignation, HashMap()) }
        assertEquals(
            "Type mismatch",
            exception.message,
        )
    }
}
