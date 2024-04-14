package ingsis.interpreter.interpretStatement

import ingsis.components.statement.CompoundAssignation
import ingsis.components.statement.Statement
import ingsis.components.statement.StatementType
import ingsis.interpreter.operatorScanner.ScanOperatorType
import ingsis.interpreter.valueAnalyzer.ValueAnalyzer
import ingsis.utils.Result

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
        previousState[variable.getName()] = result

        return previousState
    }
}
