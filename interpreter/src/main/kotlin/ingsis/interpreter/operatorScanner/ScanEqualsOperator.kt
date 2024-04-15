package ingsis.interpreter.operatorScanner

import ingsis.components.Position
import ingsis.components.Token
import ingsis.components.TokenType
import ingsis.components.statement.SingleValue
import ingsis.components.statement.Value
import ingsis.utils.Result
import ingsis.utils.checkIfVariableDefined

class ScanEqualsOperator : ScanOperatorType {
    override fun canHandle(operator: String): Boolean {
        return operator == "=="
    }

    override fun analyze(
        left: SingleValue,
        right: SingleValue,
        operatorPosition: Position,
        map: Map<String, Result>
    ): Value {
        val firstValue = checkIfVariableDefined(left, map)
        val secondValue = checkIfVariableDefined(right, map)

        val firstType = firstValue.getType().getValue()
        val secondType = secondValue.getType().getValue()

        if (firstValue == secondValue) {
            return SingleValue(Token(Position(), "true", TokenType.BOOLEAN))
        }
        return SingleValue(Token(Position(), "false", TokenType.BOOLEAN))
    }
}