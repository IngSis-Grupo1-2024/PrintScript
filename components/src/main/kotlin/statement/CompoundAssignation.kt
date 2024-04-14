package components.statement

import components.Position

class CompoundAssignation(private val position: Position, private val declaration: Declaration, private val value: Value) : Statement {
    private val statementType = StatementType.COMPOUND_ASSIGNATION

    fun getDeclaration(): Declaration = declaration

    fun getValue(): Value = value

    fun getPosition(): Position = position

    override fun getStatementType(): StatementType = statementType

    override fun toString(): String = "name: ${declaration.getVariable().getName()} \t  value: ${value.getToken()}"
}
