package ingsis.interpreter.interpretStatement

import ingsis.components.TokenType
import ingsis.components.statement.*
import ingsis.utils.Result

class ReadInputInterpreter(private val input: Input) : StatementInterpreter {
    override fun canHandle(statement: Statement): Boolean {
        return statement.getStatementType() == StatementType.READ_INPUT
    }

    override fun interpret(
        statement: Statement,
        previousState: HashMap<String, Result>,
    ): HashMap<String, Result> {
        statement as ReadInput
        val declaration = statement.getDeclaration()
        previousState[declaration.getVariable().getName()] =
            getInputResult(
                declaration.getType(),
                input.readInput(),
                declaration,
            )
        return previousState
    }

    private fun getInputResult(
        type: Type,
        input: String,
        declaration: Declaration,
    ): Result {
        if (type.getValue() == TokenType.BOOLEAN) {
            if (input == "true" || input == "false") {
                return Result(type, declaration.getKeyword().getModifier(), input)
            }
        }
        if (type.getValue() == TokenType.INTEGER) {
            if (input.toIntOrNull() != null) {
                return Result(type, declaration.getKeyword().getModifier(), input)
            }
        }
        if (type.getValue() == TokenType.STRING) {
            return Result(type, declaration.getKeyword().getModifier(), input)
        }
        if (type.getValue() == TokenType.DOUBLE) {
            if (input.toDoubleOrNull() != null) {
                return Result(type, declaration.getKeyword().getModifier(), input)
            }
        }
        throw Exception("Invalid input")
    }
}
