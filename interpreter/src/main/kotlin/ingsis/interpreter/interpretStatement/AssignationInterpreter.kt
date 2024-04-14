package ingsis.interpreter.interpretStatement

import components.statement.*
import ingsis.utils.Result
import ingsis.interpreter.operatorScanner.ScanOperatorType
import ingsis.interpreter.valueAnalyzer.ValueAnalyzer

class AssignationInterpreter(private val scanners: List<ScanOperatorType>) : StatementInterpreter {
    override fun canHandle(statement: Statement): Boolean = statement.getStatementType() == StatementType.ASSIGNATION

    override fun interpret(
        statement: Statement,
        previousState: HashMap<String, Result>,
    ): HashMap<String, Result> {
        val assignation = statement as Assignation
        val variable = assignation.getVariable()
        val value = assignation.getValue()

        val result = ValueAnalyzer(scanners).analyze(value, previousState)
        if (checkIfNewValueTypeMatchesType(variable, result, previousState)) {
            previousState[variable.getName()] = result
        }
        else {
            throw Exception("Type mismatch")
        }

        return previousState
    }

    private fun checkIfNewValueTypeMatchesType(
        variable: Variable,
        result: Result,
        map: Map<String, Result>,
    ): Boolean {
        return map[variable.getName()]?.getType()?.getValue() == result.getType().getValue()
    }
}
