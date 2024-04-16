package ingsis.interpreter.operatorScanner

import ingsis.components.Position
import ingsis.components.Token
import ingsis.components.TokenType
import ingsis.components.statement.SingleValue
import ingsis.components.statement.Value
import ingsis.utils.Result
import ingsis.utils.checkIfVariableDefined

class ScanSumOperator : ScanOperatorType {
    override fun canHandle(operator: String): Boolean {
        return operator == "+"
    }

    override fun analyze(
        left: SingleValue,
        right: SingleValue,
        operatorPosition: Position,
        map: Map<String, Result>,
    ): Value {
        val firstValue = checkIfVariableDefined(left, map)
        val secondValue = checkIfVariableDefined(right, map)

        val firstType = firstValue.getType().getValue()
        val secondType = secondValue.getType().getValue()

        if (firstType == TokenType.BOOLEAN || secondType == TokenType.BOOLEAN) {
            throw Exception(
                "Sum operation is just allowed between integers and strings in line " +
                        operatorPosition.startLine + " at position " +
                        operatorPosition.startColumn
            )
        }

        if (firstType == TokenType.STRING || secondType == TokenType.STRING) {
            val finalValue = firstValue.getValue().toString() + secondValue.getValue().toString()
            return SingleValue(Token(Position(), finalValue, TokenType.STRING))
        }

        if (firstType != TokenType.INTEGER || secondType != TokenType.INTEGER) {
            throw Exception(
                "Sum operation is just allowed between integers and strings in line " +
                        operatorPosition.startLine + " at position " +
                        operatorPosition.startColumn
            )
        }
        val finalValue = firstValue.getValue()!!.toInt() + secondValue.getValue()!!.toInt()
        return SingleValue(Token(Position(), finalValue.toString(), TokenType.INTEGER))
    }
}
