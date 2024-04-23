package ingsis.interpreter.interpretStatement

import ingsis.components.TokenType
import ingsis.components.statement.CompoundAssignationReadEnv
import ingsis.components.statement.Statement
import ingsis.components.statement.StatementType
import ingsis.components.statement.Type
import ingsis.utils.Result

class CompoundAssignationReadEnvInterpreter : StatementInterpreter {
    override fun canHandle(statement: Statement): Boolean {
        return statement.getStatementType() == StatementType.COMPOUND_ASSIGNATION_READ_ENV
    }

    override fun interpret(
        statement: Statement,
        previousState: HashMap<String, Result>,
    ): HashMap<String, Result> {
        statement as CompoundAssignationReadEnv
        val declaration = statement.getDeclaration()
        val param = statement.getParam()
        val envValue = System.getenv(param)
        val result =
            Result(
                Type(TokenType.STRING, statement.getPosition()),
                declaration.getKeyword().getModifier(),
                envValue,
            )
        previousState[declaration.getVariable().getName()] = result
        return previousState
    }
}
