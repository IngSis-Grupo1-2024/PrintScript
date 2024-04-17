package ingsis.utils

import ingsis.components.TokenType
import ingsis.components.statement.Type
import ingsis.components.statement.Value

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

fun getResultType(firstValueType: TokenType, secondValueType: TokenType): TokenType {
    if (firstValueType == TokenType.DOUBLE || secondValueType == TokenType.DOUBLE) {
        return TokenType.DOUBLE
    }
    return TokenType.INTEGER
}