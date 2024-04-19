package ingsis.components.statement

import ingsis.components.Position

class ReadInput(private val position: Position, private val declaration: Declaration) : Statement {
    override fun getStatementType(): StatementType = StatementType.READ_INPUT

    fun getDeclaration(): Declaration = declaration

    fun getPosition(): Position = position
}
