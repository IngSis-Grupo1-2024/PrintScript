package ingsis.interpreter.operatorScanner

import com.sun.jdi.InvalidTypeException
import ingsis.components.Position
import ingsis.components.Token
import ingsis.components.TokenType
import ingsis.components.statement.SingleValue
import ingsis.components.statement.Value
import ingsis.utils.Result
import ingsis.utils.checkIfVariableDefined
import ingsis.utils.getResultType

class ScanDivOperator : ScanOperatorType {
    override fun canHandle(operator: String): Boolean {
        return operator == "/"
    }

    val notAllowedTypes = listOf(TokenType.STRING)

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
                "Can't do division using no integer types in line " +
                    operatorPosition.startLine + " at position " +
                    operatorPosition.startColumn,
            )
        }

        val resultType = getResultType(firstValue.getType().getValue(), secondValue.getType().getValue())
        if (resultType == TokenType.DOUBLE) {
            return SingleValue(
                Token(
                    Position(),
                    (
                        firstValue.getValue().toString().toDouble() /
                            secondValue.getValue().toString()
                                .toDouble()
                    ).toString(),
                    TokenType.DOUBLE,
                ),
            )
        } else {
            return SingleValue(
                Token(
                    Position(),
                    (firstValue.getValue().toString().toInt() / secondValue.getValue().toString().toInt()).toString(),
                    TokenType.INTEGER,
                ),
            )
        }
    }
}
