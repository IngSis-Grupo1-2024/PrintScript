package ingsis.utils

import com.sun.jdi.InvalidTypeException
import components.Position
import components.Token
import components.TokenType
import components.statement.Operator
import components.statement.SingleValue
import components.statement.Type
import components.statement.Value

class InterpreterFunctions {
    fun evaluateExpression(
        value: Value,
        previousState: Map<String, Result>,
    ): String {
        return when (value) {
            is SingleValue ->
                if (value.getToken().getType() == TokenType.IDENTIFIER) {
                    val variable = previousState[value.getToken().getValue()]
                    if (variable != null) {
                        variable.getValue()!!
                    } else {
                        throw IllegalArgumentException("Variable ${value.getToken().getValue()} not declared")
                    }
                } else {
                    value.getToken().getValue()
                }
            is Operator -> {
                val left = value.getLeftOperator()
                val right = value.getRightOperator()
                val operator = value.getToken().getValue()

                val leftResult = evaluateExpressionOperator(left, previousState)
                val rightResult = evaluateExpressionOperator(right, previousState)

                if (leftResult.getType().getValue() == rightResult.getType().getValue()) {
                    performOperation(leftResult.getValue()!!, rightResult.getValue()!!, operator, leftResult.getType())
                } else {
                    performOperationWithDiffTypes(
                        leftResult.getValue()!!,
                        rightResult.getValue()!!,
                        operator,
                        leftResult.getType(),
                        rightResult.getType(),
                    )
                }
            }

            else -> throw IllegalArgumentException("Invalid expression")
        }
    }

    private fun evaluateExpressionOperator(
        value: Value,
        previousState: Map<String, Result>,
    ): Result {
        return when (value) {
            is SingleValue -> {
                if (value.getToken().getType() == TokenType.IDENTIFIER) {
                    previousState[value.getToken().getValue()]
                        ?: throw IllegalArgumentException("Variable ${value.getToken().getValue()} not declared")
                } else {
                    val type = getTypeByASingleValue(value.getToken())
                    val valueStr = value.getToken().getValue()
                    Result(type, valueStr)
                }
            }
            is Operator -> {
                val left = value.getLeftOperator()
                val right = value.getRightOperator()
                val operator = value.getToken().getValue()

                val leftResult = evaluateExpressionOperator(left, previousState)
                val rightResult = evaluateExpressionOperator(right, previousState)

                if (leftResult.getType().getValue() == rightResult.getType().getValue()) {
                    val resultValue = performOperation(leftResult.getValue()!!, rightResult.getValue()!!, operator, leftResult.getType())
                    Result(leftResult.getType(), resultValue)
                } else {
                    val resultValue =
                        performOperationWithDiffTypes(
                            leftResult.getValue()!!,
                            rightResult.getValue()!!,
                            operator,
                            leftResult.getType(),
                            rightResult.getType(),
                        )
                    val type = getHigherType(leftResult.getType(), rightResult.getType())
                    Result(type, resultValue)
                }
            }

            else -> throw IllegalArgumentException("Invalid expression")
        }
    }

    private fun getHigherType(
        type: Type,
        otherType: Type,
    ): Type {
        if (type.getValue() == "string") {
            return type
        } else if (otherType.getValue() == "string") {
            return otherType
        }
        return type
    }

    private fun performOperation(
        leftValue: String,
        rightValue: String,
        operator: String,
        variableType: Type,
    ): String {
        when {
            (variableType.getValue() != "integer" && (operator != "+")) -> {
                throw IllegalArgumentException("Invalid operation: $leftValue $operator $rightValue")
            }

            (
                variableType.getValue() == "integer" &&
                    (leftValue.toIntOrNull() == null || rightValue.toIntOrNull() == null) &&
                    (operator in listOf("*", "/", "+", "-"))
            ) -> {
                throw IllegalArgumentException("Invalid operation: $leftValue $operator $rightValue")
            }
        }

        return when (operator) {
            "+" ->
                if (variableType.getValue() == "string") {
                    leftValue + rightValue
                } else {
                    (leftValue.toInt() + rightValue.toInt()).toString()
                }

            "-" -> (leftValue.toInt() - rightValue.toInt()).toString()
            "*" -> (leftValue.toInt() * rightValue.toInt()).toString()
            "/" -> (leftValue.toInt() / rightValue.toInt()).toString()
            else -> throw IllegalArgumentException("Invalid operator: $operator")
        }
    }

    private fun performOperationWithDiffTypes(
        leftValue: String,
        rightValue: String,
        operator: String,
        leftType: Type,
        rightType: Type,
    ): String {
        when {
            (typesDiffInteger(leftType, rightType) && (operator != "+")) -> {
                throw IllegalArgumentException("Invalid operation: $leftValue $operator $rightValue")
            }

            (
                typesEqualInteger(leftType, rightType) &&
                    (leftValue.toIntOrNull() == null || rightValue.toIntOrNull() == null) &&
                    (operator in listOf("*", "/", "+", "-"))
            ) -> {
                throw IllegalArgumentException("Invalid operation: $leftValue $operator $rightValue")
            }
        }

        return when (operator) {
            "+" ->
                if (typesDiffInteger(leftType, rightType)) {
                    leftValue + rightValue
                } else {
                    (leftValue.toInt() + rightValue.toInt()).toString()
                }

            "-" -> (leftValue.toInt() - rightValue.toInt()).toString()
            "*" -> (leftValue.toInt() * rightValue.toInt()).toString()
            "/" -> (leftValue.toInt() / rightValue.toInt()).toString()
            else -> throw IllegalArgumentException("Invalid operator: $operator")
        }
    }

    private fun typesEqualInteger(
        leftType: Type,
        rightType: Type,
    ): Boolean = leftType.getValue() == "integer" && rightType.getValue() == "integer"

    private fun typesDiffInteger(
        leftType: Type,
        rightType: Type,
    ): Boolean = leftType.getValue() != "integer" || rightType.getValue() != "integer"

    fun getVariableType(
        variableName: String,
        previousState: Map<String, Result>,
    ): Type {
        return previousState[variableName]!!.getType()
    }

    private fun getTypeByASingleValue(token: Token): Type {
        val tokenType = token.getType()
        val tokenValue =
            when (tokenType) {
                TokenType.STRING -> {
                    "string"
                }
                TokenType.INTEGER -> {
                    "integer"
                }
                else -> {
                    throw InvalidTypeException("Invalid type")
                }
            }
        return Type(tokenValue, Position())
    }
}
