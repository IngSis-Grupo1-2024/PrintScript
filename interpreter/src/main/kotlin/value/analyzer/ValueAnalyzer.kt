package value.analyzer

import components.TokenType
import components.statement.Operator
import components.statement.SingleValue
import components.statement.Type
import components.statement.Value
import ingsis.utils.Result
import scan.value.ScanOperatorType

class ValueAnalyzer(private val scanners: List<ScanOperatorType>) {
    fun analyze(
        value: Value,
        map: Map<String, Result>,
    ): Result {
        when (value) {
            is SingleValue -> {
                if (value.getToken().getType() == TokenType.IDENTIFIER) {
                    val variableResult = map[value.getToken().getValue()]
                    return Result(
                        Type(variableResult!!.getType().getValue(), variableResult.getType().getPosition()),
                        variableResult.getValue(),
                    )
                }
                return Result(
                    Type(value.getToken().getType(), value.getToken().getPosition()),
                    value.getToken().getValue(),
                )
            }
            is Operator -> {
                val calculatedValue = recursiveOperationAnalyzer(value, map)
                return Result(
                    Type(
                        calculatedValue.getToken().getType(),
                        calculatedValue.getToken().getPosition(),
                    ),
                    calculatedValue.getToken().getValue(),
                )
            }
        }
        throw Error("Not able to assign expression")
    }

    private fun recursiveOperationAnalyzer(
        operator: Operator,
        map: Map<String, Result>,
    ): Value {
        if (operator.getLeftOperator().isLeaf() && operator.getRightOperator().isLeaf()) {
            for (scanner in scanners) {
                if (scanner.canHandle(operator.getToken().getValue())) {
                    return scanner.analyze(
                        operator.getLeftOperator() as SingleValue,
                        operator.getRightOperator() as SingleValue,
                        operator.getToken().getPosition(),
                        map,
                    )
                }
            }
            return operator
        } else {
            val left =
                if (!operator.getLeftOperator().isLeaf()) {
                    recursiveOperationAnalyzer(operator.getLeftOperator() as Operator, map)
                } else {
                    operator.getLeftOperator()
                }

            val right =
                if (!operator.getRightOperator().isLeaf()) {
                    recursiveOperationAnalyzer(operator.getRightOperator() as Operator, map)
                } else {
                    operator.getRightOperator()
                }

            return recursiveOperationAnalyzer(Operator(operator.getToken(), left, right), map)
        }
        return operator
    }
}
