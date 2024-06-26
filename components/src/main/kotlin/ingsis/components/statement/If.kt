package ingsis.components.statement

class If(
    private val comparison: Value,
    private var elseStatement: Else,
    private val ifBlock: List<Statement>,
) : Statement {
    override fun getStatementType(): StatementType {
        return StatementType.IF
    }

    fun getComparison(): Value {
        return comparison
    }

    fun getElseStatement(): List<Statement> {
        return elseStatement.getElseBlock()
    }

    fun getIfBlock(): List<Statement> {
        return ifBlock
    }

    fun addElse(elseStat: Else) {
        this.elseStatement = elseStat
    }

    override fun toString(): String {
        return "If(\ncomparison=$comparison, \nelseStatement=$elseStatement, \nifBlock=$ifBlock)"
    }
}
