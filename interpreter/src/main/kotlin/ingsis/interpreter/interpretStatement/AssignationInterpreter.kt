package ingsis.interpreter.interpretStatement

import ingsis.components.TokenType
import ingsis.components.statement.*
import ingsis.interpreter.operatorScanner.ScanOperatorType
import ingsis.interpreter.valueAnalyzer.ValueAnalyzer
import ingsis.utils.Result

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
        } else {
            throw Exception("Type mismatch")
        }

        return previousState
    }

    private fun checkIfNewValueTypeMatchesType(
        variable: Variable,
        result: Result,
        map: Map<String, Result>,
    ): Boolean {
        if (isInteger(map[variable.getName()]?.getType()?.getValue())) {
            return isDouble(result.getType().getValue()) || isInteger(map[variable.getName()]?.getType()?.getValue())
        }
        return map[variable.getName()]?.getType()?.getValue() == result.getType().getValue()
    }

    private fun isDouble(value: TokenType): Boolean = value == TokenType.DOUBLE

    private fun isInteger(value: TokenType?): Boolean = value == TokenType.INTEGER
}
