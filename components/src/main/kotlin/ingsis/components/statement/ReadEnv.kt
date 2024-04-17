package ingsis.components.statement

class ReadEnv (private val variableName: String, val value: Value): Statement {
    override fun getStatementType(): StatementType {
        return StatementType.READ_ENV
    }

    fun getVariableName(): String {
        return variableName
    }

    fun getValue() : Value {
        return value
    }
}