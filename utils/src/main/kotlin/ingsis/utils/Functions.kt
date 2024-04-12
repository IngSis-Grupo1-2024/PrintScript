package ingsis.utils

import components.TokenType
import components.statement.Type
import components.statement.Value

fun checkIfVariableDefined(
    value: Value,
    map: Map<String, Result>,
): Result {
    if (value.getToken().getType() == TokenType.IDENTIFIER) {
        val variable = map[value.getToken().getValue()]
        if (variable != null) {
            return variable
        } else {
            throw IllegalArgumentException("Variable ${value.getToken().getValue()} not declared")
        }
    } else {
        return Result(Type(value.getToken().getType(), value.getToken().getPosition()), value.getToken().getValue())
    }
}
