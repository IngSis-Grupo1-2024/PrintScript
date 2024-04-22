package ingsis.components.statement

class Else(private val elseBlock: List<Statement>) : Statement {
    override fun getStatementType(): StatementType = StatementType.ELSE

    fun getElseBlock(): List<Statement> {
        return elseBlock
    }
}
