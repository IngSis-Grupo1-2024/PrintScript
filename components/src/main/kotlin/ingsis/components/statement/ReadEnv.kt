package ingsis.components.statement

class ReadEnv (private val variableName: String, private val callingStatement: Statement): Statement {
    override fun getStatementType(): StatementType {
        return StatementType.READ_ENV
    }

    fun getVariableName(): String {
        return variableName
    }

    fun getCallingStatement() : Statement {
        return callingStatement
    }

}