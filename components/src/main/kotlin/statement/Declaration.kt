package components.statement

import components.Position

class Declaration(
    private val keyword: Keyword,
    private val variable: Variable,
    private val type: Type,
    private val position: Position,
) : Statement {
    private val statementType: StatementType = StatementType.DECLARATION

    fun getVariable(): Variable = variable

    fun getKeyword(): Keyword = keyword

    fun getType(): Type = type

    fun getPosition(): Position = position

    override fun getStatementType(): StatementType = statementType

    override fun toString(): String {
        return "keyword: " + keyword +
            "\nvariable: " + variable +
            "\ntype: " + type
    }
}
