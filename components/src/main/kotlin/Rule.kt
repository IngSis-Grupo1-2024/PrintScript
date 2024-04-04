package components

import components.ast.ASTInterface

interface Rule {
    fun validate(ast: ASTInterface): Boolean
}
