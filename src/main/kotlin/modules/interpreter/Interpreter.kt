package modules.interpreter

import components.TokenType
import components.ast.ASTInterface

class Interpreter : InterpreterInterface {
    override fun interpret(
        ast: ASTInterface,
        mapSet: ValueAndTypeMaps,
    ): ValueAndTypeMaps {
        val nodeToken = ast.token
        if (nodeToken.type == TokenType.ASSIGNATION) {
            return if (nodeToken.value == ":") {
                addDeclaration(
                    ast,
                    mapSet,
                )
            } else {
                addAssign(ast, mapSet)
            }
        }
        return mapSet
    }

    private fun addDeclaration(
        ast: ASTInterface,
        mapSet: ValueAndTypeMaps,
    ): ValueAndTypeMaps {
        val left = ast.left!!
        val value: String
        val type: String
        if (left.token.value == "string" && left.token.value == "number") {
            type = left.token.value
            value = ast.right!!.token.value
        } else {
            value = left.token.value
            type = ast.right!!.token.value
        }

        mapSet.typeMap.put(value, type)
        return mapSet
    }

    private fun addAssign(
        ast: ASTInterface,
        mapSet: ValueAndTypeMaps,
    ): ValueAndTypeMaps {
        TODO("Not yet implemented")
    }
}
