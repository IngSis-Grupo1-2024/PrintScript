package components.statement

class CompoundAssignation(private val declaration: Declaration, private val value:Value) : Statement{
    val statementType = StatementType.COMPOUND_ASSIGNATION
    fun getDeclaration(): Declaration = declaration
    fun getValue(): Value = value
    override fun toString(): String = "name: ${declaration.getVariable().getName()} \t  value: ${value.getToken()}"
}