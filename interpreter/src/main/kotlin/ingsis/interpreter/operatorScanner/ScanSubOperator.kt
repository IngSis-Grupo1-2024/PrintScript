package ingsis.interpreter.operatorScanner

import ingsis.components.Position
import ingsis.components.Token
import ingsis.components.TokenType
import ingsis.components.statement.SingleValue
import ingsis.components.statement.Value
import ingsis.utils.Result
import ingsis.utils.checkIfVariableDefined
import ingsis.utils.getResultType

class ScanSubOperator : ScanOperatorType {
    override fun canHandle(operator: String): Boolean {
        return operator == "-"
    }

    val notAllowedTypes = listOf(TokenType.BOOLEAN, TokenType.STRING)

    override fun analyze(
        left: SingleValue,
        right: SingleValue,
        operatorPosition: Position,
        map: Map<String, Result>,
    ): Value {
        val firstValue = checkIfVariableDefined(left, map)
        val secondValue = checkIfVariableDefined(right, map)

        if (notAllowedTypes.contains(firstValue.getType().getValue()) ||
            notAllowedTypes.contains(
                secondValue.getType().getValue(),
            )
        ) {
            throw Exception(
                "Can't do subtraction using no integer types or double types in line " +
                    operatorPosition.startLine + " at position " +
                    operatorPosition.startColumn,
            )
        }

        val resultType = getResultType(firstValue.getType().getValue(), secondValue.getType().getValue())

        if (resultType == TokenType.DOUBLE) {
            val finalValue = firstValue.getValue()!!.toDouble() - secondValue.getValue()!!.toDouble()
            return SingleValue(token = Token(Position(), finalValue.toString(), TokenType.DOUBLE))
        } else {
            val finalValue = firstValue.getValue()!!.toInt() - secondValue.getValue()!!.toInt()
            return SingleValue(token = Token(Position(), finalValue.toString(), TokenType.INTEGER))
        }
    }
}
