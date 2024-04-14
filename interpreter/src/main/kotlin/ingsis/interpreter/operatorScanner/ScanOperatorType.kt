package ingsis.interpreter.operatorScanner

import ingsis.components.Position
import ingsis.components.statement.SingleValue
import ingsis.components.statement.Value
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
