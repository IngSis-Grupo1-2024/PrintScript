package ingsis.interpreter.interpretStatement

import components.statement.*
import ingsis.utils.InterpreterFunctions
import ingsis.utils.Result

class CompoundAssignationInterpreter : StatementInterpreter {
    private val functions = InterpreterFunctions()

    override fun canHandle(statement: Statement): Boolean = statement.getStatementType() == StatementType.COMPOUND_ASSIGNATION

    override fun interpret(
        statement: Statement,
        previousState: HashMap<String, Result>,
    ): HashMap<String, Result> {
        val compoundAssignation = statement as CompoundAssignation
        val variable = compoundAssignation.getDeclaration().getVariable()
        val declarationType = compoundAssignation.getDeclaration().getType()
        val value = compoundAssignation.getValue()

        val result = functions.evaluateExpression(value, previousState)

        previousState[variable.getName()] = Result(declarationType, result)

        return previousState
    }
}
