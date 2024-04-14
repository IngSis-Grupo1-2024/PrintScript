package ingsis.interpreter.interpretStatement

import components.statement.Assignation
import components.statement.Statement
import components.statement.StatementType
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
        previousState[variable.getName()] = result

        return previousState
    }
}
