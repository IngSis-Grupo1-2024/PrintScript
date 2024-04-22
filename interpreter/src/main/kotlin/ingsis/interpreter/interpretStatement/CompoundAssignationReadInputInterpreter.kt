package ingsis.interpreter.interpretStatement

import ingsis.components.statement.*
import ingsis.utils.Result
import ingsis.utils.getInputResult

class CompoundAssignationReadInputInterpreter(private val input: Input) : StatementInterpreter {
    override fun canHandle(statement: Statement): Boolean {
        return statement.getStatementType() == StatementType.COMPOUND_ASSIGNATION_READ_INPUT
    }

    override fun interpret(
        statement: Statement,
        previousState: HashMap<String, Result>,
    ): HashMap<String, Result> {
        statement as CompoundAssignationReadInput
        val declaration = statement.getDeclaration()
        previousState[declaration.getVariable().getName()] =
            getInputResult(
                declaration.getType(),
                input.readInput(),
                declaration.getKeyword().getModifier(),
            )
        return previousState
    }
}
