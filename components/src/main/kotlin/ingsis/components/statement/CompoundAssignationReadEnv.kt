package ingsis.components.statement

import ingsis.components.Position

class CompoundAssignationReadEnv(
    private val position: Position,
    private val declaration: Declaration,
    private val param: String,
) : Statement {
    override fun getStatementType(): StatementType {
        return StatementType.COMPOUND_ASSIGNATION_READ_ENV
    }

    fun getDeclaration(): Declaration = declaration

    fun getPosition(): Position = position

    fun getParam(): String = param

    override fun toString(): String {
        return "CompoundAssignationReadEnv(declaration=$declaration, param='$param')"
    }
}
