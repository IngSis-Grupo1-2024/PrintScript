package scaRules

import components.Rule
import components.TokenType
import components.ast.ASTInterface

class IntegerOperationsRule : Rule {
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
                if (ast.getChildren()[0].getToken().getType() == TokenType.INTEGER &&
                    ast.getChildren()[1].getToken().getType() == TokenType.INTEGER
                ) {
                    true
                } else if (ast.getChildren()[0].getToken().getType() == TokenType.INTEGER &&
                    ast.getChildren()[1].getToken().getType() == TokenType.OPERATOR
                ) {
                    recursiveSearch(ast.getChildren()[1], variableType)
                } else if (ast.getChildren()[0].getToken().getType() == TokenType.OPERATOR &&
                    ast.getChildren()[1].getToken().getType() == TokenType.INTEGER
                ) {
                    recursiveSearch(ast.getChildren()[0], variableType)
                } else {
                    recursiveSearch(ast.getChildren()[0], variableType) &&
                        recursiveSearch(ast.getChildren()[1], variableType)
                }
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
