package ingsis.interpreter.interpretStatement

import ingsis.components.statement.ReadInput
import ingsis.components.statement.Statement
import ingsis.components.statement.StatementType
import ingsis.utils.Result

class ReadInputInterpreter(private val input: Input) : StatementInterpreter {

    override fun canHandle(statement: Statement): Boolean {
        return statement.getStatementType() == StatementType.READ_INPUT
    }

    override fun interpret(
        statement: Statement,
        previousState: HashMap<String, Result>
    ): HashMap<String, Result> {
        statement as ReadInput
        val declaration = statement.getDeclaration()
        previousState[declaration.getVariable().getName()] = Result(
            declaration.getType(),
            declaration.getKeyword().getModifier(),
            input.readInput()
        )
        return previousState
    }

}