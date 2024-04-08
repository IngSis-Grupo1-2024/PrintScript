package components.statement

import components.Position

class CompoundAssignation(private val position: Position, private val declaration: Declaration, private val value: Value) : Statement {
    val statementType = StatementType.COMPOUND_ASSIGNATION

    fun getDeclaration(): Declaration = declaration

    fun getValue(): Value = value

    fun getPosition(): Position = position

    override fun toString(): String {
        return "CompoundAssignation(\n\tdeclaration = $declaration,\n\tvalue = $value)"
    }


}
