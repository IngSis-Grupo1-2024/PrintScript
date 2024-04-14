package ingsis.interpreter.interpretStatement

import components.statement.*
import ingsis.utils.Result
import ingsis.interpreter.operatorScanner.ScanOperatorType
import ingsis.interpreter.valueAnalyzer.ValueAnalyzer

class CompoundAssignationInterpreter(private val scanners: List<ScanOperatorType>) : StatementInterpreter {
    override fun canHandle(statement: Statement): Boolean = statement.getStatementType() == StatementType.COMPOUND_ASSIGNATION

    override fun interpret(
        statement: Statement,
        previousState: HashMap<String, Result>,
    ): HashMap<String, Result> {
        val compoundAssignation = statement as CompoundAssignation
        val variable = compoundAssignation.getDeclaration().getVariable()
        val value = compoundAssignation.getValue()
        val result = ValueAnalyzer(scanners).analyze(value, previousState)
        if (checkIfNewValueTypeMatchesType(compoundAssignation.getDeclaration(), result)) {
            previousState[variable.getName()] = result
        }
        else {
            throw Exception("Type mismatch")
        }

        return previousState
    }

    private fun checkIfNewValueTypeMatchesType(
        declaration: Declaration,
        result: Result,
    ): Boolean {
        return declaration.getType().getValue() == result.getType().getValue()
    }
}
