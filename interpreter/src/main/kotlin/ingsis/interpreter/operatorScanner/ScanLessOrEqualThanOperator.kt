package ingsis.interpreter.operatorScanner

import ingsis.components.Position
import ingsis.components.Token
import ingsis.components.TokenType
import ingsis.components.statement.SingleValue
import ingsis.components.statement.Value
import ingsis.utils.Result

class ScanLessOrEqualThanOperator : ScanOperatorType {
    override fun canHandle(operator: String): Boolean {
        return operator == "<="
    }

    override fun analyze(
        left: SingleValue,
        right: SingleValue,
        operatorPosition: Position,
        map: Map<String, Result>
    ): Value {
        val firstValue = left.getToken().getValue()
        val secondValue = right.getToken().getValue()

        if (firstValue.toDouble() <= secondValue.toDouble()) {
            return SingleValue(Token(Position(), "true", TokenType.BOOLEAN))
        }
        return SingleValue(Token(Position(), "false", TokenType.BOOLEAN))
    }
}