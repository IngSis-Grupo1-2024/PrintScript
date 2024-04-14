package ingsis.interpreter.interpretStatement

import components.statement.CompoundAssignation
import components.statement.Statement
import components.statement.StatementType
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
        previousState[variable.getName()] = result

        return previousState
    }
}
