package ingsis.components.statement

class ReadInput(private val declaration: Declaration) : Statement {
    override fun getStatementType(): StatementType = StatementType.READ_INPUT

    fun getDeclaration(): Declaration = declaration


}