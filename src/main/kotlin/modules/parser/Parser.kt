package modules.parser

import components.*

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
        return AST(token, null, null)
    }

    private fun add(token: Token, ast: ASTInterface): ASTInterface {
        val rootToken: Token = ast.token
        val compareTokens = typeComparator.compare(token.type, rootToken.type)
        if (rootIsBigger(compareTokens)) {
            if (ast.isLeaf()) return AST(rootToken, getLeaf(token), null)
            // lo que podes hacer es chequear con los hijos
            // sabes que si hay un identifier, un type o un const (o value) esos si o si
            // tienen que ser hoja, por lo que si se encuentran con otro, lo mandas como hermano
            if (rootToken.type == TokenType.ASSIGNATION) return addInEmptyLeaf(token, ast)
            if (ast.left != null) return AST(rootToken, add(token, ast.left!!), ast.right)
        } else if (compareTokens == 1) return AST(token, ast, null)
        // en caso de que compareTokens == 0, tenes que comparar los valores
        return getLeaf(token)
    }

    private fun rootIsBigger(compareTokens: Int) = compareTokens == -1

    private fun addInEmptyLeaf(token: Token, ast: ASTInterface): ASTInterface {
        return if (ast.left == null) AST(ast.token, getLeaf(token), ast.right) else AST(
            ast.token,
            ast.left,
            getLeaf(token)
        )
    }
}