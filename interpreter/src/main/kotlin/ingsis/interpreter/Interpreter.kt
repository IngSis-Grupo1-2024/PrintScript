package ingsis.interpreter

import ingsis.components.statement.Statement
import ingsis.interpreter.interpretStatement.*
import ingsis.interpreter.operatorScanner.ScanDivOperator
import ingsis.interpreter.operatorScanner.ScanMulOperator
import ingsis.interpreter.operatorScanner.ScanSubOperator
import ingsis.interpreter.operatorScanner.ScanSumOperator
import ingsis.utils.Result

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
            if (it.canHandle(statement)) {
                return it.interpret(statement, variableMap)
            }
        }

        throw IllegalArgumentException("No interpreter found for statement: $statement")
    }

}
