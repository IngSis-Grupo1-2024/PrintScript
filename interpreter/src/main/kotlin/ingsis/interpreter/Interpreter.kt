package ingsis.interpreter

import ingsis.components.statement.Statement
import ingsis.interpreter.environment.Environment
import ingsis.interpreter.interpretStatement.*
import ingsis.interpreter.operatorScanner.ScanDivOperator
import ingsis.interpreter.operatorScanner.ScanMulOperator
import ingsis.interpreter.operatorScanner.ScanSubOperator
import ingsis.interpreter.operatorScanner.ScanSumOperator
import ingsis.utils.OutputEmitter
import ingsis.utils.Result

object PrintScriptInterpreter {
    fun createInterpreter(
        version: String,
        outputEmitter: OutputEmitter,
    ): Interpreter {
        return when (version) {
            "VERSION_1" -> {
                val scanners = listOf(ScanMulOperator(), ScanSumOperator(), ScanDivOperator(), ScanSubOperator())
                Interpreter(
                    listOf(
                        DeclarationInterpreter(),
                        AssignationInterpreter(scanners),
                        CompoundAssignationInterpreter(scanners),
                        PrintLineInterpreter(scanners, outputEmitter),
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
                        PrintLineInterpreter(scanners, outputEmitter),
                        PrintLineInterpreter(scanners, outputEmitter),
                        IfInterpreter(scanners, version, outputEmitter),
                        ReadEnvInterpreter(Environment(emptyMap()))
                    ),
                )
            }
        }
    }
}

class Interpreter(private val interpreters: List<StatementInterpreter>) {

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
