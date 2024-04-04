package scaRules

import components.Rule
import components.TokenType
import components.ast.ASTInterface

class IntegerAndStringOperationsRule : Rule {
    override fun validate(ast: ASTInterface): Boolean {
        return recursiveSearch(ast, ast.getToken().getType())
    }

    private fun recursiveSearch(
        ast: ASTInterface,
        variableType: TokenType,
    ): Boolean {
        val root = ast.getToken().getType()
        return when (root) {
            TokenType.OPERATOR -> {
                ast.getChildren()[0].getToken().getType() == TokenType.INTEGER && ast
                    .getChildren()[1].getToken().getType() == TokenType.STRING && ast.getToken().getValue() == "+"
            }

            TokenType.INTEGER -> {
                true
            }

            TokenType.ASSIGNATION -> {
                recursiveSearch(ast.getChildren()[1], variableType)
            }

            else -> {
                false
            }
        }
    }
}
