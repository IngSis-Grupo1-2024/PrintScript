package ingsis.interpreter.operatorScanner

import components.Position
import components.statement.SingleValue
import components.statement.Value
import ingsis.utils.Result

interface ScanOperatorType {
    fun canHandle(operator: String): Boolean

    fun analyze(
        left: SingleValue,
        right: SingleValue,
        operatorPosition: Position,
        map: Map<String, Result>,
    ): Value
}
