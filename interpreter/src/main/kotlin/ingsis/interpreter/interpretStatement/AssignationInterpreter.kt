package ingsis.interpreter.interpretStatement

import ingsis.components.statement.*
import ingsis.interpreter.operatorScanner.ScanOperatorType
import ingsis.interpreter.valueAnalyzer.ValueAnalyzer
import ingsis.utils.Result
import ingsis.utils.checkIfNewValueTypeMatchesType
import ingsis.utils.checkMutability

class AssignationInterpreter(private val scanners: List<ScanOperatorType>) : StatementInterpreter {
    override fun canHandle(statement: Statement): Boolean = statement.getStatementType() == StatementType.ASSIGNATION

    override fun interpret(
        statement: Statement,
        previousState: HashMap<String, Result>,
    ): HashMap<String, Result> {
        val assignation = statement as Assignation
        val variable = assignation.getVariable()
        val value = assignation.getValue()

        var result = ValueAnalyzer(scanners).analyze(value, previousState)
        result = result.updateModifier(previousState[variable.getName()]?.getModifier())
        if (checkMutability(variable, previousState)) {
            if (checkIfNewValueTypeMatchesType(variable, result, previousState)) {
                previousState[variable.getName()] = result
            } else {
                throw Exception("Type mismatch")
            }
        } else {
            throw Exception(
                "You can't update the value of an immutable variable at line " +
                    variable.getPosition().startLine + " at column " + variable.getPosition().startColumn,
            )
        }

        return previousState
    }
}
