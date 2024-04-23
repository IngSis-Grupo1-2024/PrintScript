package ingsis.interpreter.environment

import ingsis.components.statement.Value
import ingsis.interpreter.interpretStatement.VariableType

class EnvironmentVariable(
    private val type: VariableType,
    private val value: Value,
) {
    fun getType(): VariableType {
        return type
    }

    fun getValue(): Any {
        return value
    }
}
