package ingsis.interpreter.interpretStatement

import ingsis.components.statement.*
import ingsis.interpreter.operatorScanner.ScanOperatorType
import ingsis.interpreter.valueAnalyzer.ValueAnalyzer
import ingsis.utils.Result

class AssignationInterpreter(private val scanners: List<ScanOperatorType>) : StatementInterpreter {
    override fun canHandle(statement: Statement): Boolean = statement.getStatementType() == StatementType.ASSIGNATION

    override fun interpret(
        statement: Statement,
        previousState: HashMap<String, Result>,
    ): Pair<HashMap<String, Result>, String?> {
        val assignation = statement as Assignation
        val variable = assignation.getVariable()
        val value = assignation.getValue()

        val result = ValueAnalyzer(scanners).analyze(value, previousState)
        if (checkIfNewValueTypeMatchesType(variable, result, previousState)) {
            previousState[variable.getName()] = result
        } else {
            throw Exception("Type mismatch")
        }

        return Pair(previousState, null)
    }

    private fun checkIfNewValueTypeMatchesType(
        variable: Variable,
        result: Result,
        map: Map<String, Result>,
    ): Boolean {
        return map[variable.getName()]?.getType()?.getValue() == result.getType().getValue()
    }
}
