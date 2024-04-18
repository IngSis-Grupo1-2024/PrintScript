package ingsis.components.statement

import ingsis.components.Position

class ReadInput(private val position: Position) : Statement {

    override fun getStatementType(): StatementType {
        return StatementType.FUNCTION
    }

    fun getStatementValue(): String {
        return readlnOrNull() ?: ""
    }

}