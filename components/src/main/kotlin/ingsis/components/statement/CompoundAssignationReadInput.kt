package ingsis.components.statement

import ingsis.components.Position

class CompoundAssignationReadInput(private val position: Position, private val declaration: Declaration) : Statement {
    override fun getStatementType(): StatementType = StatementType.COMPOUND_ASSIGNATION_READ_INPUT

    fun getDeclaration(): Declaration = declaration

    fun getPosition(): Position = position
}
