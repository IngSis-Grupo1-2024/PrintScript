package ingsis.interpreter

import components.statement.Statement
import ingsis.interpreter.interpretStatement.*
import ingsis.utils.Result

object PrintScriptInterpreter {
    fun createInterpreter(version: String): Interpreter {
        return when (version) {
            "VERSION_1" ->
                Interpreter(
                    listOf(DeclarationInterpreter(), AssignationInterpreter(), CompoundAssignationInterpreter(), PrintLineInterpreter()),
                )
            else ->
                Interpreter(
                    listOf(DeclarationInterpreter(), AssignationInterpreter(), CompoundAssignationInterpreter(), PrintLineInterpreter()),
                )
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
