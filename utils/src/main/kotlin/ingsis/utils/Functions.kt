package ingsis.utils

import ingsis.components.TokenType
import ingsis.components.statement.*

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
        return Result(Type(value.getToken().getType(), value.getToken().getPosition()), Modifier.IMMUTABLE, value.getToken().getValue())
    }
}

fun getResultType(
    firstValueType: TokenType,
    secondValueType: TokenType,
): TokenType {
    if (firstValueType == TokenType.DOUBLE || secondValueType == TokenType.DOUBLE) {
        return TokenType.DOUBLE
    }
    return TokenType.INTEGER
}

fun checkIfNewValueTypeMatchesType(
    variable: Variable,
    result: Result,
    map: Map<String, Result>,
): Boolean {
    if (isInteger(map[variable.getName()]?.getType()?.getValue())) {
        return isDouble(result.getType().getValue()) || isInteger(map[variable.getName()]?.getType()?.getValue())
    }
    return map[variable.getName()]?.getType()?.getValue() == result.getType().getValue()
}

fun isDouble(value: TokenType): Boolean = value == TokenType.DOUBLE

fun isInteger(value: TokenType?): Boolean = value == TokenType.INTEGER

fun checkMutability(
    variable: Variable,
    map: Map<String, Result>,
): Boolean {
    return if (map[variable.getName()]?.getModifier() == null) {
        true
    } else {
        map[variable.getName()]?.getModifier() == Modifier.MUTABLE
    }
}

fun getInputResult(
    type: Type,
    input: String,
    modifier: Modifier,
): Result {
    if (type.getValue() == TokenType.BOOLEAN) {
        if (input == "true" || input == "false") {
            return Result(type, modifier, input)
        }
    }
    if (type.getValue() == TokenType.INTEGER) {
        if (input.toIntOrNull() != null) {
            return Result(type, modifier, input)
        }
    }
    if (type.getValue() == TokenType.STRING) {
        return Result(type, modifier, input)
    }
    if (type.getValue() == TokenType.DOUBLE) {
        if (input.toDoubleOrNull() != null) {
            return Result(type, modifier, input)
        }
    }
    throw Exception("Invalid input")
}
