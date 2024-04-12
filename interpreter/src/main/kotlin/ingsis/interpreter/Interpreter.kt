package ingsis.interpreter

import components.statement.Statement
import ingsis.interpreter.interpretStatement.*
import ingsis.utils.Result
import operator.scanner.ScanDivOperator
import operator.scanner.ScanSubOperator
import scan.value.ScanMulOperator
import scan.value.ScanSumOperator

object PrintScriptInterpreter {
    fun createInterpreter(version: String): Interpreter {
        return when (version) {
            "VERSION_1" -> {
                val scanners = listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator())
                Interpreter(
                    listOf(
                        DeclarationInterpreter(),
                        AssignationInterpreter(scanners),
                        CompoundAssignationInterpreter(scanners),
                        PrintLineInterpreter(scanners),
                    ),
                )
            }

            else -> {
                val scanners = listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator())
                Interpreter(
                    listOf(
                        DeclarationInterpreter(),
                        AssignationInterpreter(scanners),
                        CompoundAssignationInterpreter(scanners),
                        PrintLineInterpreter(scanners),
                    ),
                )
            }
        }
    }
}

class Interpreter(private val interpreters: List<StatementInterpreter>) {
    //    fun interpret(statement: Statement): Map<String, Result> =
//        interpret(statement, emptyMap())

    fun interpret(
        statement: Statement,
        variableMap: HashMap<String, Result>,
    ): HashMap<String, Result> {
        interpreters.forEach {
            if (it.canHandle(statement)) return it.interpret(statement, variableMap)
        }

        throw IllegalArgumentException("No interpreter found for statement: $statement")
    }
}
