package ingsis.interpreter.operatorScanner

import components.Position
import components.Token
import components.TokenType
import components.statement.SingleValue
import components.statement.Value
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

        if (firstValue.getType().getValue() == TokenType.STRING || secondValue.getType().getValue() == TokenType.STRING) {
            val finalValue = firstValue.getValue() + secondValue.getValue()
            return SingleValue(token = Token(Position(), finalValue, TokenType.STRING))
        }
        else if (firstValue.getType().getValue() != TokenType.INTEGER ||
            secondValue.getType().getValue() != TokenType.INTEGER) {
            throw Error(
                "Can't do addition using no integer types or string types in line " + operatorPosition.startLine + "at position " + operatorPosition.startColumn,
            )
        }
        val finalValue = firstValue.getValue()!!.toInt() + secondValue.getValue()!!.toInt()
        return SingleValue(token = Token(Position(), finalValue.toString(), TokenType.INTEGER))
    }
}
