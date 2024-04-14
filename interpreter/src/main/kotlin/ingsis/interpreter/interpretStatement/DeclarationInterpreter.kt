package ingsis.interpreter.interpretStatement

import components.statement.Declaration
import components.statement.Statement
import components.statement.StatementType
import ingsis.utils.Result

class DeclarationInterpreter : StatementInterpreter {
    override fun canHandle(statement: Statement): Boolean = statement.getStatementType() == StatementType.DECLARATION

    override fun interpret(
        statement: Statement,
        previousState: HashMap<String, Result>,
    ): HashMap<String, Result> {
        val declaration = statement as Declaration
        val variable = declaration.getVariable()
        val type = declaration.getType()

        previousState[variable.getName()] = Result(type, null)

        return previousState
    }
}
