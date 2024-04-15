package ingsis.interpreter.operatorScanner

import ingsis.components.Position
import ingsis.components.Token
import ingsis.components.TokenType
import ingsis.components.statement.SingleValue
import ingsis.components.statement.Value
import ingsis.utils.Result
import ingsis.utils.checkIfVariableDefined

class ScanMulOperator : ScanOperatorType {
    override fun canHandle(operator: String): Boolean {
        return operator == "*"
    }

    override fun analyze(
        left: SingleValue,
        right: SingleValue,
        operatorPosition: Position,
        map: Map<String, Result>,
    ): Value {
        val firstValue = checkIfVariableDefined(left, map)
        val secondValue = checkIfVariableDefined(right, map)

        if (firstValue.getType().getValue() != TokenType.INTEGER ||
            secondValue.getType().getValue() != TokenType.INTEGER
        ) {
            throw Exception(
                "Can't do multiplication using no integer types in line " +
                    operatorPosition.startLine + " at position " + operatorPosition.startColumn,
            )
        }
        val result = firstValue.getValue()!!.toInt() * secondValue.getValue()!!.toInt()
        return SingleValue(token = Token(Position(), result.toString(), TokenType.INTEGER))
    }
}
