package ingsis.utils

import components.TokenType
import components.statement.Operator
import components.statement.SingleValue
import components.statement.Type
import components.statement.Value

fun evaluateExpression(
    value: Value,
    declarationType: Type,
    previousState: Map<String, Result>
): String {
    return when (value) {
        is SingleValue ->
            if (value.getToken().getType() == TokenType.IDENTIFIER){
                val variable = previousState[value.getToken().getValue()]
                if (variable != null) {
                    variable.getValue()!!
                }
                else throw IllegalArgumentException("Variable ${value.getToken().getValue()} not declared")
            }
            else value.getToken().getValue().toString()
        is Operator -> {
            val left = value.getLeftOperator()
            val right = value.getRightOperator()
            val operator = value.getToken().getValue()

            val leftResult = evaluateExpression(left, declarationType, previousState)
            val rightResult = evaluateExpression(right, declarationType, previousState)

            performOperation(leftResult, rightResult, operator, declarationType)
        }

        else -> throw IllegalArgumentException("Invalid expression")
    }
}

fun performOperation(
    leftValue: String,
    rightValue: String,
    operator: String,
    variableType: Type,
): String {
    return when (operator) {
        "+" ->
            if (variableType.getValue() == "string") {
                leftValue + rightValue
            }
            else {
                (leftValue.toInt() + rightValue.toInt()).toString()
            }

        "-" -> (leftValue.toInt() - rightValue.toInt()).toString()
        "*" -> (leftValue.toInt() * rightValue.toInt()).toString()
        "/" -> (leftValue.toInt() / rightValue.toInt()).toString()
        else -> throw IllegalArgumentException("Invalid operator: $operator")
    }
}

fun getVariableType(
    variableName: String,
    previousState: Map<String, Result>,
): Type {
    return previousState[variableName]!!.getType()
}