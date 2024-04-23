package ingsis.components.statement

import ingsis.components.Position

class CompoundAssignationReadInput(
    private val position: Position,
    private val declaration: Declaration,
    private val argument: Value,
) : Statement {
    override fun getStatementType(): StatementType = StatementType.COMPOUND_ASSIGNATION_READ_INPUT

    fun getDeclaration(): Declaration = declaration

    fun getPosition(): Position = position

    fun getArgument(): Value = argument

    override fun toString(): String {
        return "CompoundAssignationReadInput(declaration=$declaration, argument=$argument)"
    }


}
