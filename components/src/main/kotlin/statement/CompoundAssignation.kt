package components.statement

import components.Position

class CompoundAssignation(private val declaration: Declaration, private val value:Value, private val position: Position) : Statement{
    fun getDeclaration(): Declaration = declaration
    fun getValue(): Value = value

    fun getPosition():Position = position


    override fun getStatementType(): StatementType {
        return StatementType.COMPOUND_ASSIGNATION
    }

    override fun toString(): String = "name: ${declaration.getVariable().getName()} \t  value: ${value.getToken()}"
}