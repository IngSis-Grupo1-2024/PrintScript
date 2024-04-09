package ingsis.utils

import components.statement.Operator
import components.statement.SingleValue
import components.statement.Type
import components.statement.Value

fun evaluateExpression(value: Value, declarationType: Type): String {
    return when (value) {
        is SingleValue -> value.getToken().getValue().toString()
        is Operator -> {
            val left = value.getLeftOperator()
            val right = value.getRightOperator()
            val operator = value.getToken().getValue()

            val leftResult = evaluateExpression(left, declarationType)
            val rightResult = evaluateExpression(right, declarationType)

            performOperation(leftResult, rightResult, operator, declarationType)
        }

        else -> throw IllegalArgumentException("Invalid expression")
    }
}

fun performOperation(leftValue: String, rightValue: String, operator: String, variableType: Type): String {
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
