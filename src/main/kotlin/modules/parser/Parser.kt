package modules.parser

import components.*
import components.ast.AST
import components.ast.ASTInterface

class Parser : ParserInterface {
    private val typeComparator = ComparatorTokenType()
    override fun parse(tokens: List<Token>): ASTInterface {
        var ast: ASTInterface = getLeaf(tokens[0])
        for (token in tokens) {
            ast = add(token, ast)
        }
        return ast
    }

    private fun getLeaf(token: Token): ASTInterface {
        return AST(token)
    }

    // lo que podes hacer es chequear con los hijos
    // sabes que si hay un identifier, un type o un const (o value) esos si o si
    // tienen que ser hoja, por lo que si se encuentran con otro, lo mandas como hermano
    private fun add(token: Token, ast: ASTInterface): ASTInterface {
        val rootToken: Token = ast.token
        val compareTokens = typeComparator.compare(token.type, rootToken.type)
        if (rootIsBigger(compareTokens)) {
            return if (ast.isLeaf()) AST(rootToken, getLeaf(token))
            else compWChildren(token, ast)
//            if (rootToken.type == TokenType.ASSIGNATION) return addInEmptyLeaf(token, ast)
//            if (ast.left != null) return AST(rootToken, add(token, ast.left!!), ast.right)

        } else if (compareTokens == 1) return AST(token, ast)
        // en caso de que compareTokens == 0, tenes que comparar los valores
        return getLeaf(token)
    }

    // acá
    private fun compWChildren(token: Token, ast: ASTInterface) : ASTInterface{
        if(ast.right != null) {
            val compRight = typeComparator.compare(token.type, ast.right!!.token.type)
            return if (compRight == 0) ast.addChildren(getLeaf(token))
            else AST(ast.token, ast.left, add(token, ast.right!!))
        }
        if(ast.left != null) {
            val compLeft = typeComparator.compare(token.type, ast.left!!.token.type)
            return if (compLeft == 0) ast.addChildren(getLeaf(token))
            else AST(ast.token, add(token, ast.left!!), ast.right)
        }
        return AST(ast.token, getLeaf(token))
    }

    private fun rootIsBigger(compareTokens: Int) = compareTokens == -1
}