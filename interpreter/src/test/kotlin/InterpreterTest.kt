import ingsis.components.Position
import ingsis.components.Token
import ingsis.components.TokenType
import ingsis.components.statement.*
import ingsis.interpreter.Interpreter
import ingsis.interpreter.interpretStatement.*
import ingsis.interpreter.operatorScanner.*
import ingsis.utils.Result
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import util.PrintOutputEmitterTests
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream

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
        val variableMap = interpreter.interpret(declarationStatement, HashMap())
        assertEquals(1, variableMap.size)
        assertEquals(Result(type, Modifier.MUTABLE, null), variableMap["a"])
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
        val variableMap1 = interpreter.interpret(declarationStatement, HashMap())
        val assignationStatement = Assignation(position, variable, SingleValue(Token(position, "5", TokenType.INTEGER)))
        val variableMap2 = interpreter.interpret(assignationStatement, variableMap1)
        assertEquals(1, variableMap2.size)
        assertEquals(Result(type, Modifier.MUTABLE, "5"), variableMap2["a"])
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
        val variableMap = interpreter.interpret(declarationStatement, HashMap())
        assertEquals(1, variableMap.size)
        assertEquals(Result(type, Modifier.MUTABLE, null), variableMap["a"])
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
        val variableMap = interpreter.interpret(compoundAssignation, HashMap())
        assertEquals(1, variableMap.size)
        assertEquals(Result(type, Modifier.MUTABLE, "5"), variableMap["a"])
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
        val variableMap = interpreter.interpret(compoundAssignation, HashMap())
        assertEquals(1, variableMap.size)
        assertEquals(Result(type, Modifier.MUTABLE, "8"), variableMap["a"])
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
        val variableMap = interpreter.interpret(compoundAssignation, HashMap())
        assertEquals(1, variableMap.size)
        assertEquals(Result(type, Modifier.MUTABLE, "2"), variableMap["a"])
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
        val exception = assertThrows<Exception> { interpreter.interpret(compoundAssignation, HashMap()) }
        assertEquals(
            "Can't do subtraction using no integer types or double types in line " + position.startLine +
                " at position " + position.startColumn,
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
        val variableMap = interpreter.interpret(compoundAssignation, HashMap())
        assertEquals(1, variableMap.size)
        assertEquals(Result(type, Modifier.MUTABLE, "15"), variableMap["a"])
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

        val exception = assertThrows<Exception> { interpreter.interpret(compoundAssignation, HashMap()) }
        assertEquals(
            "Can't do multiplication using no integer types or double types in line " + position.startLine +
                " at position " + position.startColumn,
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
        val variableMap = interpreter.interpret(compoundAssignation, HashMap())
        assertEquals(1, variableMap.size)
        assertEquals(Result(type, Modifier.MUTABLE, "5"), variableMap["a"])
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

        val exception = assertThrows<Exception> { interpreter.interpret(compoundAssignation, HashMap()) }
        assertEquals(
            "Can't do division using no integer types or double types in line " + position.startLine +
                " at position " + position.startColumn,
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
        val variableMap = interpreter.interpret(compoundAssignation, HashMap())
        assertEquals(1, variableMap.size)
        assertEquals(Result(type, Modifier.MUTABLE, "13"), variableMap["a"])
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
        val variableMap = interpreter.interpret(compoundAssignation, HashMap())
        assertEquals(1, variableMap.size)
        assertEquals(Result(type, Modifier.MUTABLE, "20"), variableMap["a"])
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
        val variableMap = interpreter.interpret(compoundAssignation, HashMap())
        assertEquals(1, variableMap.size)
        assertEquals(Result(type, Modifier.MUTABLE, "7"), variableMap["a"])
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
        val variableMap = interpreter.interpret(compoundAssignation, HashMap())
        assertEquals(1, variableMap.size)
        assertEquals(Result(type, Modifier.MUTABLE, "15"), variableMap["a"])
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
        val variableMap = interpreter.interpret(compoundAssignation, HashMap())
        assertEquals(1, variableMap.size)
        assertEquals(Result(type, Modifier.MUTABLE, "25"), variableMap["a"])
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
        val variableMap = interpreter.interpret(compoundAssignation, HashMap())
        assertEquals(1, variableMap.size)
        assertEquals(Result(type, Modifier.MUTABLE, "Hello5"), variableMap["a"])
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
        val variableMap = interpreter.interpret(compoundAssignation, HashMap())
        assertEquals(1, variableMap.size)
        assertEquals(Result(type, Modifier.MUTABLE, "HelloWorld"), variableMap["a"])
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
                    PrintLineInterpreter(scanners, PrintOutputEmitterTests()),
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
                    PrintLineInterpreter(scanners, PrintOutputEmitterTests()),
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
                    PrintLineInterpreter(scanners, PrintOutputEmitterTests()),
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
        val scanners = listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator())
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                    PrintLineInterpreter(scanners, PrintOutputEmitterTests()),
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
                    PrintLineInterpreter(scanners, PrintOutputEmitterTests()),
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
                    PrintLineInterpreter(scanners, PrintOutputEmitterTests()),
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
                    PrintLineInterpreter(scanners, PrintOutputEmitterTests()),
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
                    PrintLineInterpreter(scanners, PrintOutputEmitterTests()),
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
        map = interpreter.interpret(compoundAssignationVarA, map)
        map = interpreter.interpret(compoundAssignationVarB, map)
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
                    PrintLineInterpreter(scanners, PrintOutputEmitterTests()),
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
        val scanners = listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator())
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                    PrintLineInterpreter(scanners, PrintOutputEmitterTests()),
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
        map = interpreter.interpret(compoundAssignationVarA, map)
        map = interpreter.interpret(compoundAssignationVarB, map)
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

    @Test
    fun testEqualsEquals() {
        val scanners =
            listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator(), ScanEqualsOperator())
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
        val type = Type(TokenType.BOOLEAN, position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(
                position,
                declarationStatement,
                Operator(
                    Token(position, "==", TokenType.OPERATOR),
                    SingleValue(Token(position, "5", TokenType.INTEGER)),
                    SingleValue(Token(position, "5", TokenType.INTEGER)),
                ),
            )
        val pair = interpreter.interpret(compoundAssignation, HashMap())
        assertEquals(1, pair.size)
        assertEquals(Result(type, Modifier.MUTABLE, "true"), pair["a"])
    }

    @Test
    fun testNotEquals() {
        val scanners =
            listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator(), ScanNotEqualsOperator())
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
        val type = Type(TokenType.BOOLEAN, position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(
                position,
                declarationStatement,
                Operator(
                    Token(position, "!=", TokenType.OPERATOR),
                    SingleValue(Token(position, "6", TokenType.INTEGER)),
                    SingleValue(Token(position, "5", TokenType.INTEGER)),
                ),
            )
        val pair = interpreter.interpret(compoundAssignation, HashMap())
        assertEquals(1, pair.size)
        assertEquals(Result(type, Modifier.MUTABLE, "true"), pair["a"])
    }

    @Test
    fun testGreaterThan() {
        val scanners =
            listOf(
                ScanMulOperator(),
                ScanSumOperator(),
                ScanDivOperator(),
                ScanSubOperator(),
                ScanGreaterThanOperator(),
            )
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
        val type = Type(TokenType.BOOLEAN, position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(
                position,
                declarationStatement,
                Operator(
                    Token(position, ">", TokenType.OPERATOR),
                    SingleValue(Token(position, "6", TokenType.INTEGER)),
                    SingleValue(Token(position, "5", TokenType.INTEGER)),
                ),
            )
        val pair = interpreter.interpret(compoundAssignation, HashMap())
        assertEquals(1, pair.size)
        assertEquals(Result(type, Modifier.MUTABLE, "true"), pair["a"])
    }

    @Test
    fun testGreaterOrEqualsThanWithANumberGreater() {
        val scanners =
            listOf(
                ScanMulOperator(),
                ScanSumOperator(),
                ScanDivOperator(),
                ScanSubOperator(),
                ScanGreaterOrEqualThanOperator(),
            )
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
        val type = Type(TokenType.BOOLEAN, position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(
                position,
                declarationStatement,
                Operator(
                    Token(position, ">=", TokenType.OPERATOR),
                    SingleValue(Token(position, "6", TokenType.INTEGER)),
                    SingleValue(Token(position, "5", TokenType.INTEGER)),
                ),
            )
        val pair = interpreter.interpret(compoundAssignation, HashMap())
        assertEquals(1, pair.size)
        assertEquals(Result(type, Modifier.MUTABLE, "true"), pair["a"])
    }

    @Test
    fun testGreaterOrEqualsThanWithEqualNumbers() {
        val scanners =
            listOf(
                ScanMulOperator(),
                ScanSumOperator(),
                ScanDivOperator(),
                ScanSubOperator(),
                ScanGreaterOrEqualThanOperator(),
            )
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
        val type = Type(TokenType.BOOLEAN, position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(
                position,
                declarationStatement,
                Operator(
                    Token(position, ">=", TokenType.OPERATOR),
                    SingleValue(Token(position, "6", TokenType.INTEGER)),
                    SingleValue(Token(position, "5", TokenType.INTEGER)),
                ),
            )
        val pair = interpreter.interpret(compoundAssignation, HashMap())
        assertEquals(1, pair.size)
        assertEquals(Result(type, Modifier.MUTABLE, "true"), pair["a"])
    }

    @Test
    fun testLessOrEqualsThanWithANumberLesser() {
        val scanners =
            listOf(
                ScanMulOperator(),
                ScanSumOperator(),
                ScanDivOperator(),
                ScanSubOperator(),
                ScanLessOrEqualThanOperator(),
            )
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
        val type = Type(TokenType.BOOLEAN, position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(
                position,
                declarationStatement,
                Operator(
                    Token(position, "<=", TokenType.OPERATOR),
                    SingleValue(Token(position, "4", TokenType.INTEGER)),
                    SingleValue(Token(position, "5", TokenType.INTEGER)),
                ),
            )
        val pair = interpreter.interpret(compoundAssignation, HashMap())
        assertEquals(1, pair.size)
        assertEquals(Result(type, Modifier.MUTABLE, "true"), pair["a"])
    }

    @Test
    fun testLessOrEqualsThanWithEqualsNumbers() {
        val scanners =
            listOf(
                ScanMulOperator(),
                ScanSumOperator(),
                ScanDivOperator(),
                ScanSubOperator(),
                ScanLessOrEqualThanOperator(),
            )
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
        val type = Type(TokenType.BOOLEAN, position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(
                position,
                declarationStatement,
                Operator(
                    Token(position, "<=", TokenType.OPERATOR),
                    SingleValue(Token(position, "5", TokenType.INTEGER)),
                    SingleValue(Token(position, "5", TokenType.INTEGER)),
                ),
            )
        val pair = interpreter.interpret(compoundAssignation, HashMap())
        assertEquals(1, pair.size)
        assertEquals(Result(type, Modifier.MUTABLE, "true"), pair["a"])
    }

    @Test
    fun testLessThan() {
        val scanners =
            listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator(), ScanLessThanOperator())
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
        val type = Type(TokenType.BOOLEAN, position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(
                position,
                declarationStatement,
                Operator(
                    Token(position, "<", TokenType.OPERATOR),
                    SingleValue(Token(position, "4", TokenType.INTEGER)),
                    SingleValue(Token(position, "5", TokenType.INTEGER)),
                ),
            )
        val pair = interpreter.interpret(compoundAssignation, HashMap())
        assertEquals(1, pair.size)
        assertEquals(Result(type, Modifier.MUTABLE, "true"), pair["a"])
    }

    @Test
    fun testEqualsWithStrings() {
        val scanners =
            listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator(), ScanEqualsOperator())
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
        val type = Type(TokenType.BOOLEAN, position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(
                position,
                declarationStatement,
                Operator(
                    Token(position, "==", TokenType.OPERATOR),
                    SingleValue(Token(position, "hello", TokenType.STRING)),
                    SingleValue(Token(position, "hello", TokenType.STRING)),
                ),
            )
        val pair = interpreter.interpret(compoundAssignation, HashMap())
        assertEquals(1, pair.size)
        assertEquals(Result(type, Modifier.MUTABLE, "true"), pair["a"])
    }

    @Test
    fun testTryToUpdateValueToAnImmutableVariable() {
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
        val keyword = Keyword(Modifier.IMMUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.INTEGER, position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(
                position,
                declarationStatement,
                SingleValue(Token(position, "5", TokenType.INTEGER)),
            )
        val pair = interpreter.interpret(compoundAssignation, HashMap())
        val updateVariable = Assignation(position, variable, SingleValue(Token(position, "10", TokenType.INTEGER)))
        val exception = assertThrows<Exception> { interpreter.interpret(updateVariable, pair) }
        assertEquals(
            "You can't update the value of an immutable variable at line " +
                position.startLine + " at column " + position.startColumn,
            exception.message,
        )
    }

    @Test
    fun testTryToUpdateValueToAMutableVariable() {
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
                SingleValue(Token(position, "5", TokenType.INTEGER)),
            )
        val pair = interpreter.interpret(compoundAssignation, HashMap())
        val updateVariable = Assignation(position, variable, SingleValue(Token(position, "10", TokenType.INTEGER)))
        val pair2 = interpreter.interpret(updateVariable, pair)
        assertEquals(1, pair2.size)
        assertEquals(Result(type, Modifier.MUTABLE, "10"), pair2["a"])
    }

    @Test
    fun testSubtractTwoBooleans() {
        val scanners =
            listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator(), ScanEqualsOperator())
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.IMMUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.BOOLEAN, position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(
                position,
                declarationStatement,
                Operator(
                    Token(position, "-", TokenType.OPERATOR),
                    SingleValue(Token(position, "true", TokenType.BOOLEAN)),
                    SingleValue(Token(position, "true", TokenType.BOOLEAN)),
                ),
            )
        val exception = assertThrows<Exception> { interpreter.interpret(compoundAssignation, HashMap()) }
        assertEquals(
            "Can't do subtraction using no integer types or double types in line " +
                position.startLine + " at position " +
                position.startColumn,
            exception.message,
        )
    }

    @Test
    fun testSumTwoBooleans() {
        val scanners =
            listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator(), ScanEqualsOperator())
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.IMMUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.BOOLEAN, position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(
                position,
                declarationStatement,
                Operator(
                    Token(position, "+", TokenType.OPERATOR),
                    SingleValue(Token(position, "true", TokenType.BOOLEAN)),
                    SingleValue(Token(position, "true", TokenType.BOOLEAN)),
                ),
            )
        val exception = assertThrows<Exception> { interpreter.interpret(compoundAssignation, HashMap()) }
        assertEquals(
            "Sum operation is just allowed between integers and strings in line " +
                position.startLine + " at position " +
                position.startColumn,
            exception.message,
        )
    }

    @Test
    fun testMultiplyTwoBooleans() {
        val scanners =
            listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator(), ScanEqualsOperator())
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.IMMUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.BOOLEAN, position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(
                position,
                declarationStatement,
                Operator(
                    Token(position, "*", TokenType.OPERATOR),
                    SingleValue(Token(position, "true", TokenType.BOOLEAN)),
                    SingleValue(Token(position, "true", TokenType.BOOLEAN)),
                ),
            )
        val exception = assertThrows<Exception> { interpreter.interpret(compoundAssignation, HashMap()) }
        assertEquals(
            "Can't do multiplication using no integer types or double types in line " +
                position.startLine + " at position " +
                position.startColumn,
            exception.message,
        )
    }

    @Test
    fun testDivideTwoBooleans() {
        val scanners =
            listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator(), ScanEqualsOperator())
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.IMMUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.BOOLEAN, position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(
                position,
                declarationStatement,
                Operator(
                    Token(position, "/", TokenType.OPERATOR),
                    SingleValue(Token(position, "true", TokenType.BOOLEAN)),
                    SingleValue(Token(position, "true", TokenType.BOOLEAN)),
                ),
            )
        val exception = assertThrows<Exception> { interpreter.interpret(compoundAssignation, HashMap()) }
        assertEquals(
            "Can't do division using no integer types or double types in line " +
                position.startLine + " at position " +
                position.startColumn,
            exception.message,
        )
    }

    @Test
    fun testSumStingAndBoolean() {
        val scanners =
            listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator(), ScanEqualsOperator())
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                ),
            )
        val position = Position()

        val keyword = Keyword(Modifier.IMMUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.STRING, position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(
                position,
                declarationStatement,
                Operator(
                    Token(position, "+", TokenType.OPERATOR),
                    SingleValue(Token(position, "hello", TokenType.STRING)),
                    SingleValue(Token(position, "true", TokenType.BOOLEAN)),
                ),
            )
        val exception = assertThrows<Exception> { interpreter.interpret(compoundAssignation, HashMap()) }
        assertEquals(
            "Sum operation is just allowed between integers and strings in line " +
                position.startLine + " at position " +
                position.startColumn,
            exception.message,
        )
    }

    @Test
    fun testSumIntegerAndBoolean() {
        val scanners =
            listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator(), ScanEqualsOperator())
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                ),
            )
        val position = Position()

        val keyword = Keyword(Modifier.IMMUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.STRING, position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(
                position,
                declarationStatement,
                Operator(
                    Token(position, "+", TokenType.OPERATOR),
                    SingleValue(Token(position, "3", TokenType.INTEGER)),
                    SingleValue(Token(position, "true", TokenType.BOOLEAN)),
                ),
            )
        val exception = assertThrows<Exception> { interpreter.interpret(compoundAssignation, HashMap()) }
        assertEquals(
            "Sum operation is just allowed between integers and strings in line " +
                position.startLine + " at position " +
                position.startColumn,
            exception.message,
        )
    }

    @Test
    fun testSubtractBooleanAndInteger() {
        val scanners =
            listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator(), ScanEqualsOperator())
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                ),
            )
        val position = Position()

        val keyword = Keyword(Modifier.IMMUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.STRING, position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(
                position,
                declarationStatement,
                Operator(
                    Token(position, "-", TokenType.OPERATOR),
                    SingleValue(Token(position, "true", TokenType.BOOLEAN)),
                    SingleValue(Token(position, "3", TokenType.INTEGER)),
                ),
            )
        val exception = assertThrows<Exception> { interpreter.interpret(compoundAssignation, HashMap()) }
        assertEquals(
            "Can't do subtraction using no integer types or double types in line " +
                position.startLine + " at position " +
                position.startColumn,
            exception.message,
        )
    }

    @Test
    fun testMultiplyBooleanAndInteger() {
        val scanners =
            listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator(), ScanEqualsOperator())
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                ),
            )
        val position = Position()

        val keyword = Keyword(Modifier.IMMUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.STRING, position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(
                position,
                declarationStatement,
                Operator(
                    Token(position, "*", TokenType.OPERATOR),
                    SingleValue(Token(position, "true", TokenType.BOOLEAN)),
                    SingleValue(Token(position, "3", TokenType.INTEGER)),
                ),
            )
        val exception = assertThrows<Exception> { interpreter.interpret(compoundAssignation, HashMap()) }
        assertEquals(
            "Can't do multiplication using no integer types or double types in line " +
                position.startLine + " at position " +
                position.startColumn,
            exception.message,
        )
    }

    @Test
    fun testDivideBooleanAndInteger() {
        val scanners =
            listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator(), ScanEqualsOperator())
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                ),
            )
        val position = Position()

        val keyword = Keyword(Modifier.IMMUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.STRING, position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(
                position,
                declarationStatement,
                Operator(
                    Token(position, "/", TokenType.OPERATOR),
                    SingleValue(Token(position, "true", TokenType.BOOLEAN)),
                    SingleValue(Token(position, "3", TokenType.INTEGER)),
                ),
            )
        val exception =
            assertThrows<Exception> {
                interpreter.interpret(compoundAssignation, HashMap())
            }
        assertEquals(
            "Can't do division using no integer types or double types in line " +
                position.startLine + " at position " +
                position.startColumn,
            exception.message,
        )
    }

    @Test
    fun testIfStatementWhenComparisonBetweenTwoIntegersIsTrue() {
        val scanners =
            listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator(), ScanEqualsOperator())
        val outputEmitter = PrintOutputEmitterTests()
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                    IfInterpreter(scanners, "VERSION_2", outputEmitter),
                ),
            )

        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val ifTrueVariable = Variable("a", position)
        val type = Type(TokenType.INTEGER, position)
        val ifTrueDeclarationStatement = Declaration(keyword, ifTrueVariable, type, position)
        val ifTrueCompoundAssignation =
            CompoundAssignation(
                position,
                ifTrueDeclarationStatement,
                SingleValue(Token(position, "5", TokenType.INTEGER)),
            )

        val ifFalseVariable = Variable("b", position)
        val ifFalseDeclarationStatement = Declaration(keyword, ifFalseVariable, type, position)
        val ifFalseCompoundAssignation =
            CompoundAssignation(
                position,
                ifFalseDeclarationStatement,
                SingleValue(Token(position, "7", TokenType.INTEGER)),
            )
        val elseStatement = Else(listOf(ifFalseCompoundAssignation))

        val ifStatement =
            If(
                Operator(
                    Token(position, "==", TokenType.OPERATOR),
                    SingleValue(Token(position, "5", TokenType.INTEGER)),
                    SingleValue(Token(position, "5", TokenType.INTEGER)),
                ),
                elseStatement,
                listOf(ifTrueCompoundAssignation),
            )
        val pair = interpreter.interpret(ifStatement, HashMap())
        assertEquals(1, pair.size)
        assertEquals(Result(type, Modifier.MUTABLE, "5"), pair["a"])
        assertFalse(pair.containsKey("b"))
    }

    @Test
    fun testIfStatementWhenComparisonBetweenTwoIntegersIsFalse() {
        val scanners =
            listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator(), ScanEqualsOperator())
        val outputEmitter = PrintOutputEmitterTests()
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                    IfInterpreter(scanners, "VERSION_2", outputEmitter),
                ),
            )

        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val ifTrueVariable = Variable("a", position)
        val type = Type(TokenType.INTEGER, position)
        val ifTrueDeclarationStatement = Declaration(keyword, ifTrueVariable, type, position)
        val ifTrueCompoundAssignation =
            CompoundAssignation(
                position,
                ifTrueDeclarationStatement,
                SingleValue(Token(position, "5", TokenType.INTEGER)),
            )

        val ifFalseVariable = Variable("b", position)
        val ifFalseDeclarationStatement = Declaration(keyword, ifFalseVariable, type, position)
        val ifFalseCompoundAssignation =
            CompoundAssignation(
                position,
                ifFalseDeclarationStatement,
                SingleValue(Token(position, "7", TokenType.INTEGER)),
            )
        val elseStatement = Else(listOf(ifFalseCompoundAssignation))

        val ifStatement =
            If(
                Operator(
                    Token(position, "==", TokenType.OPERATOR),
                    SingleValue(Token(position, "5", TokenType.INTEGER)),
                    SingleValue(Token(position, "6", TokenType.INTEGER)),
                ),
                elseStatement,
                listOf(ifTrueCompoundAssignation),
            )
        val pair = interpreter.interpret(ifStatement, HashMap())
        assertEquals(1, pair.size)
        assertEquals(Result(type, Modifier.MUTABLE, "7"), pair["b"])
        assertFalse(pair.containsKey("a"))
    }

    @Test
    fun testIfStatementWhenComparisonBetweenTwoStringsIsTrue() {
        val scanners =
            listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator(), ScanEqualsOperator())
        val outputEmitter = PrintOutputEmitterTests()
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                    IfInterpreter(scanners, "VERSION_2", outputEmitter),
                ),
            )

        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val ifTrueVariable = Variable("a", position)
        val type = Type(TokenType.STRING, position)
        val ifTrueDeclarationStatement = Declaration(keyword, ifTrueVariable, type, position)
        val ifTrueCompoundAssignation =
            CompoundAssignation(
                position,
                ifTrueDeclarationStatement,
                SingleValue(Token(position, "hello", TokenType.STRING)),
            )

        val ifFalseVariable = Variable("b", position)
        val ifFalseDeclarationStatement = Declaration(keyword, ifFalseVariable, type, position)
        val ifFalseCompoundAssignation =
            CompoundAssignation(
                position,
                ifFalseDeclarationStatement,
                SingleValue(Token(position, "world", TokenType.STRING)),
            )
        val elseStatement = Else(listOf(ifFalseCompoundAssignation))

        val ifStatement =
            If(
                Operator(
                    Token(position, "==", TokenType.OPERATOR),
                    SingleValue(Token(position, "hello", TokenType.STRING)),
                    SingleValue(Token(position, "hello", TokenType.STRING)),
                ),
                elseStatement,
                listOf(ifTrueCompoundAssignation),
            )
        val pair = interpreter.interpret(ifStatement, HashMap())
        assertEquals(1, pair.size)
        assertEquals(Result(type, Modifier.MUTABLE, "hello"), pair["a"])
        assertFalse(pair.containsKey("b"))
    }

    @Test
    fun testIfStatementWhenComparisonBetweenTwoStringsIsFalse() {
        val scanners =
            listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator(), ScanEqualsOperator())
        val outputEmitter = PrintOutputEmitterTests()
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                    IfInterpreter(scanners, "VERSION_2", outputEmitter),
                ),
            )

        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val ifTrueVariable = Variable("a", position)
        val type = Type(TokenType.STRING, position)
        val ifTrueDeclarationStatement = Declaration(keyword, ifTrueVariable, type, position)
        val ifTrueCompoundAssignation =
            CompoundAssignation(
                position,
                ifTrueDeclarationStatement,
                SingleValue(Token(position, "hello", TokenType.STRING)),
            )

        val ifFalseVariable = Variable("b", position)
        val ifFalseDeclarationStatement = Declaration(keyword, ifFalseVariable, type, position)
        val ifFalseCompoundAssignation =
            CompoundAssignation(
                position,
                ifFalseDeclarationStatement,
                SingleValue(Token(position, "world", TokenType.STRING)),
            )
        val elseStatement = Else(listOf(ifFalseCompoundAssignation))

        val ifStatement =
            If(
                Operator(
                    Token(position, "==", TokenType.OPERATOR),
                    SingleValue(Token(position, "hello", TokenType.STRING)),
                    SingleValue(Token(position, "world", TokenType.STRING)),
                ),
                elseStatement,
                listOf(ifTrueCompoundAssignation),
            )
        val pair = interpreter.interpret(ifStatement, HashMap())
        assertEquals(1, pair.size)
        assertEquals(Result(type, Modifier.MUTABLE, "world"), pair["b"])
        assertFalse(pair.containsKey("a"))
    }

    @Test
    fun testIfStatementWhenIfReceivesAVariableAlreadyDefined() {
        val scanners =
            listOf(
                ScanMulOperator(),
                ScanSumOperator(),
                ScanDivOperator(),
                ScanSubOperator(),
                ScanEqualsOperator(),
            )
        val outputEmitter = PrintOutputEmitterTests()
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                    IfInterpreter(scanners, "VERSION_2", outputEmitter),
                ),
            )

        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variableA = Variable("a", position)
        val type = Type(TokenType.INTEGER, position)
        val declarationStatementA = Declaration(keyword, variableA, type, position)
        val assignationA =
            CompoundAssignation(
                position,
                declarationStatementA,
                SingleValue(Token(position, "5", TokenType.INTEGER)),
            )

        val addAToMap = interpreter.interpret(assignationA, HashMap())

        val variableB = Variable("b", position)
        val declarationStatementB = Declaration(keyword, variableB, type, position)
        val assignationB =
            CompoundAssignation(
                position,
                declarationStatementB,
                SingleValue(Token(position, "7", TokenType.INTEGER)),
            )

        val updateVariableA = Assignation(position, variableA, SingleValue(Token(position, "10", TokenType.INTEGER)))

        val elseStatement = Else(listOf(assignationB))
        val ifStatement =
            If(
                Operator(
                    Token(position, "==", TokenType.OPERATOR),
                    SingleValue(Token(position, "a", TokenType.IDENTIFIER)),
                    SingleValue(Token(position, "5", TokenType.INTEGER)),
                ),
                elseStatement,
                listOf(updateVariableA),
            )

        val variableMap = interpreter.interpret(ifStatement, addAToMap)
        assertEquals(1, variableMap.size)
        assertEquals(Result(type, Modifier.MUTABLE, "10"), variableMap["a"])
        assertFalse(variableMap.containsKey("b"))
    }

    @Test
    fun testIfStatementWhenIfReceivesATrueBoolean() {
        val scanners =
            listOf(
                ScanMulOperator(),
                ScanSumOperator(),
                ScanDivOperator(),
                ScanSubOperator(),
                ScanEqualsOperator(),
            )
        val outputEmitter = PrintOutputEmitterTests()
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                    IfInterpreter(scanners, "VERSION_2", outputEmitter),
                ),
            )

        val position = Position()
        val variableA = Variable("a", position)
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val type = Type(TokenType.BOOLEAN, position)
        val declarationStatement = Declaration(keyword, Variable("a", position), type, position)
        val assignation =
            CompoundAssignation(
                position,
                declarationStatement,
                SingleValue(Token(position, "true", TokenType.BOOLEAN)),
            )

        val variableMap = interpreter.interpret(assignation, HashMap())

        val variableB = Variable("b", position)
        val declarationStatementB = Declaration(keyword, variableB, type, position)
        val assignationB =
            CompoundAssignation(
                position,
                declarationStatementB,
                SingleValue(Token(position, "7", TokenType.INTEGER)),
            )

        val updateVariableA = Assignation(position, variableA, SingleValue(Token(position, "false", TokenType.BOOLEAN)))

        val ifStatement =
            If(
                SingleValue(Token(position, "a", TokenType.IDENTIFIER)),
                Else(listOf(assignationB)),
                listOf(updateVariableA),
            )

        val variableMap2 = interpreter.interpret(ifStatement, variableMap)
        assertEquals(1, variableMap2.size)
        assertEquals(Result(type, Modifier.MUTABLE, "false"), variableMap2["a"])
    }

    @Test
    fun testIfStatementWhenIfReceivesAFalseBoolean() {
        val scanners =
            listOf(
                ScanMulOperator(),
                ScanSumOperator(),
                ScanDivOperator(),
                ScanSubOperator(),
                ScanEqualsOperator(),
            )
        val outputEmitter = PrintOutputEmitterTests()
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                    IfInterpreter(scanners, "VERSION_2", outputEmitter),
                ),
            )

        val position = Position()
        val variableA = Variable("a", position)
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val type = Type(TokenType.BOOLEAN, position)
        val declarationStatement = Declaration(keyword, Variable("a", position), type, position)
        val assignation =
            CompoundAssignation(
                position,
                declarationStatement,
                SingleValue(Token(position, "false", TokenType.BOOLEAN)),
            )

        val variableMap = interpreter.interpret(assignation, HashMap())

        val variableB = Variable("b", position)
        val typeB = Type(TokenType.INTEGER, position)
        val declarationStatementB = Declaration(keyword, variableB, typeB, position)
        val assignationB =
            CompoundAssignation(
                position,
                declarationStatementB,
                SingleValue(Token(position, "7", TokenType.INTEGER)),
            )

        val updateVariableA = Assignation(position, variableA, SingleValue(Token(position, "false", TokenType.BOOLEAN)))

        val ifStatement =
            If(
                SingleValue(Token(position, "a", TokenType.IDENTIFIER)),
                Else(listOf(assignationB)),
                listOf(updateVariableA),
            )

        val variableMap2 = interpreter.interpret(ifStatement, variableMap)
        assertEquals(2, variableMap2.size)
        assertEquals(Result(type, Modifier.MUTABLE, "false"), variableMap2["a"])
        assertEquals(Result(typeB, Modifier.MUTABLE, "7"), variableMap2["b"])
    }

    @Test
    fun testDeclareAndAssignADouble() {
        val scanners =
            listOf(
                ScanMulOperator(),
                ScanSumOperator(),
                ScanDivOperator(),
                ScanSubOperator(),
                ScanEqualsOperator(),
            )
        val outputEmitter = PrintOutputEmitterTests()
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                    IfInterpreter(scanners, "VERSION_2", outputEmitter),
                ),
            )

        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val type = Type(TokenType.DOUBLE, position)
        val declarationStatement = Declaration(keyword, Variable("a", position), type, position)
        val assignation =
            CompoundAssignation(
                position,
                declarationStatement,
                SingleValue(Token(position, "5.5", TokenType.DOUBLE)),
            )

        val variableMap = interpreter.interpret(assignation, HashMap())

        assertEquals(1, variableMap.size)
        assertEquals(Result(type, Modifier.MUTABLE, "5.5"), variableMap["a"])
    }

    @Test
    fun testSumTwoDoubles() {
        val scanners =
            listOf(
                ScanMulOperator(),
                ScanSumOperator(),
                ScanDivOperator(),
                ScanSubOperator(),
                ScanEqualsOperator(),
            )
        val outputEmitter = PrintOutputEmitterTests()
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                    IfInterpreter(scanners, "VERSION_2", outputEmitter),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.DOUBLE, position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(
                position,
                declarationStatement,
                Operator(
                    Token(position, "+", TokenType.OPERATOR),
                    SingleValue(Token(position, "5.5", TokenType.DOUBLE)),
                    SingleValue(Token(position, "3.3", TokenType.DOUBLE)),
                ),
            )
        val pair = interpreter.interpret(compoundAssignation, HashMap())
        assertEquals(1, pair.size)
        assertEquals(Result(type, Modifier.MUTABLE, "8.8"), pair["a"])
    }

    @Test
    fun testSubtractTwoDoubles() {
        val scanners =
            listOf(
                ScanMulOperator(),
                ScanSumOperator(),
                ScanDivOperator(),
                ScanSubOperator(),
                ScanEqualsOperator(),
            )
        val outputEmitter = PrintOutputEmitterTests()
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                    IfInterpreter(scanners, "VERSION_2", outputEmitter),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.DOUBLE, position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(
                position,
                declarationStatement,
                Operator(
                    Token(position, "-", TokenType.OPERATOR),
                    SingleValue(Token(position, "5.5", TokenType.DOUBLE)),
                    SingleValue(Token(position, "3.3", TokenType.DOUBLE)),
                ),
            )
        val pair = interpreter.interpret(compoundAssignation, HashMap())
        assertEquals(1, pair.size)
        assertEquals(Result(type, Modifier.MUTABLE, "2.2"), pair["a"])
    }

    @Test
    fun testDivideTwoDoubles() {
        val scanners =
            listOf(
                ScanMulOperator(),
                ScanSumOperator(),
                ScanDivOperator(),
                ScanSubOperator(),
                ScanEqualsOperator(),
            )
        val outputEmitter = PrintOutputEmitterTests()
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                    IfInterpreter(scanners, "VERSION_2", outputEmitter),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.DOUBLE, position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(
                position,
                declarationStatement,
                Operator(
                    Token(position, "/", TokenType.OPERATOR),
                    SingleValue(Token(position, "5.5", TokenType.DOUBLE)),
                    SingleValue(Token(position, "2.2", TokenType.DOUBLE)),
                ),
            )
        val pair = interpreter.interpret(compoundAssignation, HashMap())
        assertEquals(1, pair.size)
        assertEquals(Result(type, Modifier.MUTABLE, "2.5"), pair["a"])
    }

    @Test
    fun testMultiplyTwoDoubles() {
        val scanners =
            listOf(
                ScanMulOperator(),
                ScanSumOperator(),
                ScanDivOperator(),
                ScanSubOperator(),
                ScanEqualsOperator(),
            )
        val outputEmitter = PrintOutputEmitterTests()
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                    IfInterpreter(scanners, "VERSION_2", outputEmitter),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.DOUBLE, position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(
                position,
                declarationStatement,
                Operator(
                    Token(position, "*", TokenType.OPERATOR),
                    SingleValue(Token(position, "5.5", TokenType.DOUBLE)),
                    SingleValue(Token(position, "2.2", TokenType.DOUBLE)),
                ),
            )
        val pair = interpreter.interpret(compoundAssignation, HashMap())
        assertEquals(1, pair.size)
        assertEquals(Result(type, Modifier.MUTABLE, "12.1"), pair["a"])
    }

    @Test
    fun testSumBetweenDoubleAndInteger() {
        val scanners =
            listOf(
                ScanMulOperator(),
                ScanSumOperator(),
                ScanDivOperator(),
                ScanSubOperator(),
                ScanEqualsOperator(),
            )
        val outputEmitter = PrintOutputEmitterTests()
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                    IfInterpreter(scanners, "VERSION_2", outputEmitter),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.DOUBLE, position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(
                position,
                declarationStatement,
                Operator(
                    Token(position, "+", TokenType.OPERATOR),
                    SingleValue(Token(position, "5.5", TokenType.DOUBLE)),
                    SingleValue(Token(position, "2", TokenType.INTEGER)),
                ),
            )
        val pair = interpreter.interpret(compoundAssignation, HashMap())
        assertEquals(1, pair.size)
        assertEquals(Result(type, Modifier.MUTABLE, "7.5"), pair["a"])
    }

    @Test
    fun testSubtractBetweenDoubleAndInteger() {
        val scanners =
            listOf(
                ScanMulOperator(),
                ScanSumOperator(),
                ScanDivOperator(),
                ScanSubOperator(),
                ScanEqualsOperator(),
            )
        val outputEmitter = PrintOutputEmitterTests()
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                    IfInterpreter(scanners, "VERSION_2", outputEmitter),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.DOUBLE, position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(
                position,
                declarationStatement,
                Operator(
                    Token(position, "-", TokenType.OPERATOR),
                    SingleValue(Token(position, "5.5", TokenType.DOUBLE)),
                    SingleValue(Token(position, "2", TokenType.INTEGER)),
                ),
            )
        val pair = interpreter.interpret(compoundAssignation, HashMap())
        assertEquals(1, pair.size)
        assertEquals(Result(type, Modifier.MUTABLE, "3.5"), pair["a"])
    }

    @Test
    fun testMultiplyBetweenDoubleAndInteger() {
        val scanners =
            listOf(
                ScanMulOperator(),
                ScanSumOperator(),
                ScanDivOperator(),
                ScanSubOperator(),
                ScanEqualsOperator(),
            )
        val outputEmitter = PrintOutputEmitterTests()
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                    IfInterpreter(scanners, "VERSION_2", outputEmitter),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.DOUBLE, position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(
                position,
                declarationStatement,
                Operator(
                    Token(position, "*", TokenType.OPERATOR),
                    SingleValue(Token(position, "5.5", TokenType.DOUBLE)),
                    SingleValue(Token(position, "2", TokenType.INTEGER)),
                ),
            )
        val pair = interpreter.interpret(compoundAssignation, HashMap())
        assertEquals(1, pair.size)
        assertEquals(Result(type, Modifier.MUTABLE, "11.0"), pair["a"])
    }

    @Test
    fun testDivideBetweenDoubleAndInteger() {
        val scanners =
            listOf(
                ScanMulOperator(),
                ScanSumOperator(),
                ScanDivOperator(),
                ScanSubOperator(),
                ScanEqualsOperator(),
            )
        val outputEmitter = PrintOutputEmitterTests()
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                    IfInterpreter(scanners, "VERSION_2", outputEmitter),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.DOUBLE, position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(
                position,
                declarationStatement,
                Operator(
                    Token(position, "/", TokenType.OPERATOR),
                    SingleValue(Token(position, "5.5", TokenType.DOUBLE)),
                    SingleValue(Token(position, "2", TokenType.INTEGER)),
                ),
            )
        val pair = interpreter.interpret(compoundAssignation, HashMap())
        assertEquals(1, pair.size)
        assertEquals(Result(type, Modifier.MUTABLE, "2.75"), pair["a"])
    }

    @Test
    fun testSumDoubleAndString() {
        val scanners =
            listOf(
                ScanMulOperator(),
                ScanSumOperator(),
                ScanDivOperator(),
                ScanSubOperator(),
                ScanEqualsOperator(),
            )
        val outputEmitter = PrintOutputEmitterTests()
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                    IfInterpreter(scanners, "VERSION_2", outputEmitter),
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
                    SingleValue(Token(position, "5.5", TokenType.DOUBLE)),
                    SingleValue(Token(position, "hello", TokenType.STRING)),
                ),
            )
        val result = interpreter.interpret(compoundAssignation, HashMap())
        assertEquals(1, result.size)
        assertEquals(Result(type, Modifier.MUTABLE, "5.5hello"), result["a"])
    }

    @Test
    fun testSumBetweenBooleanAndDouble() {
        val scanners =
            listOf(
                ScanMulOperator(),
                ScanSumOperator(),
                ScanDivOperator(),
                ScanSubOperator(),
                ScanEqualsOperator(),
            )
        val outputEmitter = PrintOutputEmitterTests()
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                    IfInterpreter(scanners, "VERSION_2", outputEmitter),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.DOUBLE, position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(
                position,
                declarationStatement,
                Operator(
                    Token(position, "+", TokenType.OPERATOR),
                    SingleValue(Token(position, "5.5", TokenType.DOUBLE)),
                    SingleValue(Token(position, "true", TokenType.BOOLEAN)),
                ),
            )
        val exception = assertThrows<Exception> { interpreter.interpret(compoundAssignation, HashMap()) }
        assertEquals(
            "Sum operation is just allowed between integers and strings in line " +
                position.startLine + " at position " +
                position.startColumn,
            exception.message,
        )
    }

    @Test
    fun testSubtractBetweenBooleanAndDouble() {
        val scanners =
            listOf(
                ScanMulOperator(),
                ScanSumOperator(),
                ScanDivOperator(),
                ScanSubOperator(),
                ScanEqualsOperator(),
            )
        val outputEmitter = PrintOutputEmitterTests()
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                    IfInterpreter(scanners, "VERSION_2", outputEmitter),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.DOUBLE, position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(
                position,
                declarationStatement,
                Operator(
                    Token(position, "-", TokenType.OPERATOR),
                    SingleValue(Token(position, "5.5", TokenType.DOUBLE)),
                    SingleValue(Token(position, "true", TokenType.BOOLEAN)),
                ),
            )
        val exception = assertThrows<Exception> { interpreter.interpret(compoundAssignation, HashMap()) }
        assertEquals(
            "Can't do subtraction using no integer types or double types in line " +
                position.startLine + " at position " +
                position.startColumn,
            exception.message,
        )
    }

    @Test
    fun testMultiplyBetweenBooleanAndDouble() {
        val scanners =
            listOf(
                ScanMulOperator(),
                ScanSumOperator(),
                ScanDivOperator(),
                ScanSubOperator(),
                ScanEqualsOperator(),
            )
        val outputEmitter = PrintOutputEmitterTests()
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                    IfInterpreter(scanners, "VERSION_2", outputEmitter),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.DOUBLE, position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(
                position,
                declarationStatement,
                Operator(
                    Token(position, "*", TokenType.OPERATOR),
                    SingleValue(Token(position, "5.5", TokenType.DOUBLE)),
                    SingleValue(Token(position, "true", TokenType.BOOLEAN)),
                ),
            )
        val exception = assertThrows<Exception> { interpreter.interpret(compoundAssignation, HashMap()) }
        assertEquals(
            "Can't do multiplication using no integer types or double types in line " +
                position.startLine + " at position " +
                position.startColumn,
            exception.message,
        )
    }

    @Test
    fun testDivideBetweenBooleanAndDouble() {
        val scanners =
            listOf(
                ScanMulOperator(),
                ScanSumOperator(),
                ScanDivOperator(),
                ScanSubOperator(),
                ScanEqualsOperator(),
            )
        val outputEmitter = PrintOutputEmitterTests()
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                    IfInterpreter(scanners, "VERSION_2", outputEmitter),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.DOUBLE, position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val compoundAssignation =
            CompoundAssignation(
                position,
                declarationStatement,
                Operator(
                    Token(position, "/", TokenType.OPERATOR),
                    SingleValue(Token(position, "5.5", TokenType.DOUBLE)),
                    SingleValue(Token(position, "true", TokenType.BOOLEAN)),
                ),
            )
        val exception = assertThrows<Exception> { interpreter.interpret(compoundAssignation, HashMap()) }
        assertEquals(
            "Can't do division using no integer types or double types in line " +
                position.startLine + " at position " +
                position.startColumn,
            exception.message,
        )
    }

    @Test
    fun testReadInputWithInteger() {
        val input = ByteArrayInputStream("123".toByteArray())
        System.setIn(input)

        val output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))

        val scanners =
            listOf(
                ScanMulOperator(),
                ScanSumOperator(),
                ScanDivOperator(),
                ScanSubOperator(),
                ScanEqualsOperator(),
            )
        val outputEmitter = PrintOutputEmitterTests()
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                    IfInterpreter(scanners, "VERSION_2", outputEmitter),
                    CompoundAssignationReadInputInterpreter(InputReader(), scanners),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.INTEGER, position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val argument = SingleValue(Token(position, "Write your input", TokenType.STRING))
        val readInput =
            CompoundAssignationReadInput(
                position,
                declarationStatement,
                argument,
            )
        val pair = interpreter.interpret(readInput, HashMap())

        System.setIn(System.`in`)
        System.setOut(System.out)

        assertEquals(1, pair.size)
        assertEquals(Result(type, Modifier.MUTABLE, "123"), pair["a"])
    }

    @Test
    fun testReadInputWithDouble() {
        val input = ByteArrayInputStream("123.456".toByteArray())
        System.setIn(input)

        val output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))

        val scanners =
            listOf(
                ScanMulOperator(),
                ScanSumOperator(),
                ScanDivOperator(),
                ScanSubOperator(),
                ScanEqualsOperator(),
            )
        val outputEmitter = PrintOutputEmitterTests()
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                    IfInterpreter(scanners, "VERSION_2", outputEmitter),
                    CompoundAssignationReadInputInterpreter(InputReader(), scanners),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.DOUBLE, position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val argument = SingleValue(Token(position, "Write your input", TokenType.STRING))
        val readInput =
            CompoundAssignationReadInput(
                position,
                declarationStatement,
                argument,
            )
        val pair = interpreter.interpret(readInput, HashMap())

        System.setIn(System.`in`)
        System.setOut(System.out)

        assertEquals(1, pair.size)
        assertEquals(Result(type, Modifier.MUTABLE, "123.456"), pair["a"])
    }

    @Test
    fun testReadInputWithString() {
        val input = ByteArrayInputStream("hello".toByteArray())
        System.setIn(input)

        val output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))

        val scanners =
            listOf(
                ScanMulOperator(),
                ScanSumOperator(),
                ScanDivOperator(),
                ScanSubOperator(),
                ScanEqualsOperator(),
            )
        val outputEmitter = PrintOutputEmitterTests()
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                    IfInterpreter(scanners, "VERSION_2", outputEmitter),
                    CompoundAssignationReadInputInterpreter(InputReader(), scanners),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.STRING, position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val argument = SingleValue(Token(position, "Write your input", TokenType.STRING))
        val readInput =
            CompoundAssignationReadInput(
                position,
                declarationStatement,
                argument,
            )
        val pair = interpreter.interpret(readInput, HashMap())

        System.setIn(System.`in`)
        System.setOut(System.out)

        assertEquals(1, pair.size)
        assertEquals(Result(type, Modifier.MUTABLE, "hello"), pair["a"])
    }

    @Test
    fun testReadInputWhenDeclaringAnIntegerAndPassingAString() {
        val input = ByteArrayInputStream("hello".toByteArray())
        System.setIn(input)

        val output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))

        val scanners =
            listOf(
                ScanMulOperator(),
                ScanSumOperator(),
                ScanDivOperator(),
                ScanSubOperator(),
                ScanEqualsOperator(),
            )
        val outputEmitter = PrintOutputEmitterTests()
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                    IfInterpreter(scanners, "VERSION_2", outputEmitter),
                    CompoundAssignationReadInputInterpreter(InputReader(), scanners),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.INTEGER, position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val argument = SingleValue(Token(position, "Write your input", TokenType.STRING))
        val readInput =
            CompoundAssignationReadInput(
                position,
                declarationStatement,
                argument,
            )
        val exception = assertThrows<Exception> { interpreter.interpret(readInput, HashMap()) }

        System.setIn(System.`in`)
        System.setOut(System.out)

        assertEquals(
            "Invalid input",
            exception.message,
        )
    }

    @Test
    fun testReadInputWhenDeclaringADoubleAndPassingAString() {
        val input = ByteArrayInputStream("hello".toByteArray())
        System.setIn(input)

        val output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))

        val scanners =
            listOf(
                ScanMulOperator(),
                ScanSumOperator(),
                ScanDivOperator(),
                ScanSubOperator(),
                ScanEqualsOperator(),
            )
        val outputEmitter = PrintOutputEmitterTests()
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                    IfInterpreter(scanners, "VERSION_2", outputEmitter),
                    CompoundAssignationReadInputInterpreter(InputReader(), scanners),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.DOUBLE, position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val argument = SingleValue(Token(position, "Write your input", TokenType.STRING))
        val readInput =
            CompoundAssignationReadInput(
                position,
                declarationStatement,
                argument,
            )
        val exception = assertThrows<Exception> { interpreter.interpret(readInput, HashMap()) }

        System.setIn(System.`in`)
        System.setOut(System.out)

        assertEquals(
            "Invalid input",
            exception.message,
        )
    }

    @Test
    fun testReadInputWhenDeclaringABooleanAndPassingAString() {
        val input = ByteArrayInputStream("hello".toByteArray())
        System.setIn(input)

        val output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))

        val scanners =
            listOf(
                ScanMulOperator(),
                ScanSumOperator(),
                ScanDivOperator(),
                ScanSubOperator(),
                ScanEqualsOperator(),
            )
        val outputEmitter = PrintOutputEmitterTests()
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                    IfInterpreter(scanners, "VERSION_2", outputEmitter),
                    CompoundAssignationReadInputInterpreter(InputReader(), scanners),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.BOOLEAN, position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val argument = SingleValue(Token(position, "Write your input", TokenType.STRING))
        val readInput =
            CompoundAssignationReadInput(
                position,
                declarationStatement,
                argument,
            )
        val exception = assertThrows<Exception> { interpreter.interpret(readInput, HashMap()) }

        System.setIn(System.`in`)
        System.setOut(System.out)

        assertEquals(
            "Invalid input",
            exception.message,
        )
    }

    @Test
    fun testReadInputWithABoolean() {
        val input = ByteArrayInputStream("true".toByteArray())
        System.setIn(input)

        val output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))

        val scanners =
            listOf(
                ScanMulOperator(),
                ScanSumOperator(),
                ScanDivOperator(),
                ScanSubOperator(),
                ScanEqualsOperator(),
            )
        val outputEmitter = PrintOutputEmitterTests()
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                    IfInterpreter(scanners, "VERSION_2", outputEmitter),
                    CompoundAssignationReadInputInterpreter(InputReader(), scanners),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.BOOLEAN, position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val argument = SingleValue(Token(position, "Write your input", TokenType.STRING))
        val readInput =
            CompoundAssignationReadInput(
                position,
                declarationStatement,
                argument,
            )
        val pair = interpreter.interpret(readInput, HashMap())

        System.setIn(System.`in`)
        System.setOut(System.out)

        assertEquals(1, pair.size)
        assertEquals(Result(type, Modifier.MUTABLE, "true"), pair["a"])
    }

    @Test
    fun testReadInputWithAStringWithValueTrue() {
        val input = ByteArrayInputStream("true".toByteArray())
        System.setIn(input)

        val output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))

        val scanners =
            listOf(
                ScanMulOperator(),
                ScanSumOperator(),
                ScanDivOperator(),
                ScanSubOperator(),
                ScanEqualsOperator(),
            )
        val outputEmitter = PrintOutputEmitterTests()
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                    IfInterpreter(scanners, "VERSION_2", outputEmitter),
                    CompoundAssignationReadInputInterpreter(InputReader(), scanners),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.STRING, position)
        val declarationStatement = Declaration(keyword, variable, type, position)
        val argument = SingleValue(Token(position, "Write your input", TokenType.STRING))
        val readInput =
            CompoundAssignationReadInput(
                position,
                declarationStatement,
                argument,
            )
        val pair = interpreter.interpret(readInput, HashMap())

        System.setIn(System.`in`)
        System.setOut(System.out)

        assertEquals(1, pair.size)
        assertEquals(Result(type, Modifier.MUTABLE, "true"), pair["a"])
    }

    @Test
    fun testAssignationReadInputWithInteger() {
        val input = ByteArrayInputStream("123".toByteArray())
        System.setIn(input)

        val output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))

        val scanners =
            listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator(), ScanEqualsOperator())
        val outputEmitter = PrintOutputEmitterTests()
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                    IfInterpreter(scanners, "VERSION_2", outputEmitter),
                    CompoundAssignationReadInputInterpreter(InputReader(), scanners),
                    AssignationReadInputInterpreter(InputReader(), scanners),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.INTEGER, position)
        val declarationStatement = Declaration(keyword, variable, type, position)

        val declarationMap = interpreter.interpret(declarationStatement, HashMap())

        val argument = SingleValue(Token(position, "Write your input", TokenType.STRING))

        val assignationReadInput = AssignationReadInput(position, variable, argument)
        val variableMap = interpreter.interpret(assignationReadInput, declarationMap)

        System.setIn(System.`in`)
        System.setOut(System.out)

        assertEquals(1, variableMap.size)
        assertEquals(Result(type, Modifier.MUTABLE, "123"), variableMap["a"])
    }

    @Test
    fun testAssignationReadInputWithDouble() {
        val input = ByteArrayInputStream("123.456".toByteArray())
        System.setIn(input)

        val output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))

        val scanners =
            listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator(), ScanEqualsOperator())
        val outputEmitter = PrintOutputEmitterTests()
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                    IfInterpreter(scanners, "VERSION_2", outputEmitter),
                    CompoundAssignationReadInputInterpreter(InputReader(), scanners),
                    AssignationReadInputInterpreter(InputReader(), scanners),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.DOUBLE, position)
        val declarationStatement = Declaration(keyword, variable, type, position)

        val declarationMap = interpreter.interpret(declarationStatement, HashMap())

        val argument = SingleValue(Token(position, "Write your input", TokenType.STRING))

        val assignationReadInput = AssignationReadInput(position, variable, argument)
        val variableMap = interpreter.interpret(assignationReadInput, declarationMap)

        System.setIn(System.`in`)
        System.setOut(System.out)

        assertEquals(1, variableMap.size)
        assertEquals(Result(type, Modifier.MUTABLE, "123.456"), variableMap["a"])
    }

    @Test
    fun testAssignationReadInputWithString() {
        val input = ByteArrayInputStream("hello".toByteArray())
        System.setIn(input)

        val output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))

        val scanners =
            listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator(), ScanEqualsOperator())
        val outputEmitter = PrintOutputEmitterTests()
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                    IfInterpreter(scanners, "VERSION_2", outputEmitter),
                    CompoundAssignationReadInputInterpreter(InputReader(), scanners),
                    AssignationReadInputInterpreter(InputReader(), scanners),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.STRING, position)
        val declarationStatement = Declaration(keyword, variable, type, position)

        val declarationMap = interpreter.interpret(declarationStatement, HashMap())

        val argument = SingleValue(Token(position, "Write your input", TokenType.STRING))

        val assignationReadInput = AssignationReadInput(position, variable, argument)
        val variableMap = interpreter.interpret(assignationReadInput, declarationMap)

        System.setIn(System.`in`)
        System.setOut(System.out)

        assertEquals(1, variableMap.size)
        assertEquals(Result(type, Modifier.MUTABLE, "hello"), variableMap["a"])
    }

    @Test
    fun testAssignationReadInputWithBoolean() {
        val input = ByteArrayInputStream("true".toByteArray())
        System.setIn(input)

        val output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))

        val scanners =
            listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator(), ScanEqualsOperator())
        val outputEmitter = PrintOutputEmitterTests()
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                    IfInterpreter(scanners, "VERSION_2", outputEmitter),
                    CompoundAssignationReadInputInterpreter(InputReader(), scanners),
                    AssignationReadInputInterpreter(InputReader(), scanners),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.BOOLEAN, position)
        val declarationStatement = Declaration(keyword, variable, type, position)

        val declarationMap = interpreter.interpret(declarationStatement, HashMap())

        val argument = SingleValue(Token(position, "Write your input", TokenType.STRING))

        val assignationReadInput = AssignationReadInput(position, variable, argument)
        val variableMap = interpreter.interpret(assignationReadInput, declarationMap)

        System.setIn(System.`in`)
        System.setOut(System.out)

        assertEquals(1, variableMap.size)
        assertEquals(Result(type, Modifier.MUTABLE, "true"), variableMap["a"])
    }

    @Test
    fun testTryingToAssignAStringToAnIntegerVariable() {
        val input = ByteArrayInputStream("hello".toByteArray())
        System.setIn(input)

        val output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))

        val scanners =
            listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator(), ScanEqualsOperator())
        val outputEmitter = PrintOutputEmitterTests()
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                    IfInterpreter(scanners, "VERSION_2", outputEmitter),
                    AssignationReadInputInterpreter(InputReader(), scanners),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.INTEGER, position)
        val declarationStatement = Declaration(keyword, variable, type, position)

        val declarationMap = interpreter.interpret(declarationStatement, HashMap())

        val argument = SingleValue(Token(position, "Write your input", TokenType.STRING))

        val assignationReadInput = AssignationReadInput(position, variable, argument)
        val exception =
            assertThrows<Exception> {
                interpreter.interpret(assignationReadInput, declarationMap)
            }

        System.setIn(System.`in`)
        System.setOut(System.out)

        assertEquals(
            "Invalid input",
            exception.message,
        )
    }

    @Test
    fun testTryingToAssignAStringToADoubleVariable() {
        val input = ByteArrayInputStream("hello".toByteArray())
        System.setIn(input)

        val output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))

        val scanners =
            listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator(), ScanEqualsOperator())
        val outputEmitter = PrintOutputEmitterTests()
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                    IfInterpreter(scanners, "VERSION_2", outputEmitter),
                    AssignationReadInputInterpreter(InputReader(), scanners),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.DOUBLE, position)
        val declarationStatement = Declaration(keyword, variable, type, position)

        val declarationMap = interpreter.interpret(declarationStatement, HashMap())

        val argument = SingleValue(Token(position, "Write your input", TokenType.STRING))

        val assignationReadInput = AssignationReadInput(position, variable, argument)
        val exception =
            assertThrows<Exception> {
                interpreter.interpret(assignationReadInput, declarationMap)
            }

        System.setIn(System.`in`)
        System.setOut(System.out)

        assertEquals(
            "Invalid input",
            exception.message,
        )
    }

    @Test
    fun testTryingToAssignAStringToABooleanVariable() {
        val input = ByteArrayInputStream("hello".toByteArray())
        System.setIn(input)

        val output = ByteArrayOutputStream()
        System.setOut(PrintStream(output))

        val scanners =
            listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator(), ScanEqualsOperator())
        val outputEmitter = PrintOutputEmitterTests()
        val interpreter =
            Interpreter(
                listOf(
                    AssignationInterpreter(scanners),
                    DeclarationInterpreter(),
                    CompoundAssignationInterpreter(scanners),
                    IfInterpreter(scanners, "VERSION_2", outputEmitter),
                    AssignationReadInputInterpreter(InputReader(), scanners),
                ),
            )
        val position = Position()
        val keyword = Keyword(Modifier.MUTABLE, "let", position)
        val variable = Variable("a", position)
        val type = Type(TokenType.BOOLEAN, position)
        val declarationStatement = Declaration(keyword, variable, type, position)

        val declarationMap = interpreter.interpret(declarationStatement, HashMap())

        val argument = SingleValue(Token(position, "Write your input", TokenType.STRING))

        val assignationReadInput = AssignationReadInput(position, variable, argument)
        val exception =
            assertThrows<Exception> {
                interpreter.interpret(assignationReadInput, declarationMap)
            }

        System.setIn(System.`in`)
        System.setOut(System.out)

        assertEquals(
            "Invalid input",
            exception.message,
        )
    }
}
