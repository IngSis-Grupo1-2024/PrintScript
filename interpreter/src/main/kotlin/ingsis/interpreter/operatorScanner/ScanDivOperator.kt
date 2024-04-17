package ingsis.interpreter.operatorScanner

import com.sun.jdi.InvalidTypeException
import ingsis.components.Position
import ingsis.components.Token
import ingsis.components.TokenType
import ingsis.components.statement.SingleValue
import ingsis.components.statement.Value
import ingsis.utils.Result
import ingsis.utils.checkIfVariableDefined

class ScanDivOperator : ScanOperatorType {
    override fun canHandle(operator: String): Boolean {
        return operator == "/"
    }

    override fun analyze(
        left: SingleValue,
        right: SingleValue,
        operatorPosition: Position,
        map: Map<String, Result>,
    ): Value {

        var finalValue: Any = 0
        val firstValue = checkIfVariableDefined(left, map)
        val secondValue = checkIfVariableDefined(right, map)

        if (firstValue.getType().getValue() != TokenType.INTEGER ||
            secondValue.getType().getValue() != TokenType.INTEGER
        ) {
            throw Exception(
                "Can't do division using no integer types in line " +
                        operatorPosition.startLine + " at position " +
                        operatorPosition.startColumn,
            )
        }
        if (firstValue.getType().getValue() == TokenType.INTEGER && secondValue.getType().getValue() == TokenType.INTEGER) {
            finalValue = firstValue.getValue()!!.toInt() / secondValue.getValue()!!.toInt()
        }
        if (firstValue.getType().getValue() == TokenType.DOUBLE && secondValue.getType().getValue() == TokenType.DOUBLE) {
            finalValue = firstValue.getValue()!!.toDouble() / secondValue.getValue()!!.toDouble()
        }
        if (firstValue.getType().getValue() == TokenType.DOUBLE && secondValue.getType().getValue() == TokenType.INTEGER) {
            finalValue = firstValue.getValue()!!.toDouble() / secondValue.getValue()!!.toInt()
        }
        if (firstValue.getType().getValue() == TokenType.INTEGER && secondValue.getType().getValue() == TokenType.DOUBLE) {
            finalValue = firstValue.getValue()!!.toInt() / secondValue.getValue()!!.toDouble()
        }
        return SingleValue(token = Token(Position(), finalValue.toString(), TokenType.INTEGER))
    }
}
