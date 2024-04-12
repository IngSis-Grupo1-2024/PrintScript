package operator.scanner

import components.Position
import components.Token
import components.TokenType
import components.statement.SingleValue
import components.statement.Value
import ingsis.utils.Result
import ingsis.utils.checkIfVariableDefined
import scan.value.ScanOperatorType

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
        val firstValue = checkIfVariableDefined(left, map)
        val secondValue = checkIfVariableDefined(right, map)

        if (firstValue.getType().getValue() == TokenType.STRING || secondValue.getType().getValue() == TokenType.STRING) {
            throw Error(
                "Can't do division using strings in line " + operatorPosition.startLine + "at position " + operatorPosition.startColumn,
            )
        }
        val finalValue = firstValue.getValue()!!.toInt() / secondValue.getValue()!!.toInt()
        return SingleValue(token = Token(Position(), finalValue.toString(), TokenType.INTEGER))
    }
}
