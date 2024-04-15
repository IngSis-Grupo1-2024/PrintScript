package ingsis.interpreter.interpretStatement

import ingsis.components.statement.*
import ingsis.interpreter.operatorScanner.ScanOperatorType
import ingsis.interpreter.valueAnalyzer.ValueAnalyzer
import ingsis.utils.Result

class CompoundAssignationInterpreter(private val scanners: List<ScanOperatorType>) : StatementInterpreter {
    override fun canHandle(statement: Statement): Boolean = statement.getStatementType() == StatementType.COMPOUND_ASSIGNATION

    override fun interpret(
        statement: Statement,
        previousState: HashMap<String, Result>,
    ): Pair<HashMap<String, Result>, String?> {
        val compoundAssignation = statement as CompoundAssignation
        val variable = compoundAssignation.getDeclaration().getVariable()
        val value = compoundAssignation.getValue()
        val result = ValueAnalyzer(scanners).analyze(value, previousState)
        if (checkIfNewValueTypeMatchesType(compoundAssignation.getDeclaration(), result)) {
            previousState[variable.getName()] = result
        } else {
            throw Exception("Type mismatch")
        }

        return Pair(previousState, null)
    }

    private fun checkIfNewValueTypeMatchesType(
        declaration: Declaration,
        result: Result,
    ): Boolean {
        return declaration.getType().getValue() == result.getType().getValue()
    }
}
