package modules.parser

import components.*
import components.ast.AST
import components.ast.ASTInterface
import kotlin.math.abs

class Parser : ParserInterface {
    private val typeComparator = ComparatorTokenType()
    private val valueComparator = ComparatorTokenValue()
    private val twoChildToken = listOf(TokenType.ASSIGNATION, TokenType.OPERATOR)
    override fun parse(tokens: List<Token>): ASTInterface {
        var ast: ASTInterface = getLeaf(tokens[0])
        if(tokens.size > 1)
            for (token in tokens.subList(1, tokens.size)) {
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
        val compareTokens = compareValueAndType(token, ast)
        return if (rootIsBigger(compareTokens)) compWChildren(token, ast)
        else if (compareTokens == 1) AST(token, ast)
        else if (abs(compareTokens) == 2) ast
        else ast.addChildren(getLeaf(token))
    }

    private fun compareValueAndType(token: Token, ast: ASTInterface): Int {
        val compValue = valueComparator.compare(token, ast.token)
        val compToken = typeComparator.compare(token.type, ast.token.type)
        return if(compToken != 0) compToken
        else compValue
    }

    private fun compWChildren(token: Token, ast: ASTInterface) : ASTInterface {
        val rootToken = ast.token
        if(rootToken.type in twoChildToken && ast.hasAnyEmptyChild()) return findEmptyLeaf(token, ast)
        if(ast.right != null) {
            val comp = compareValueAndType(token, ast.right!!)
            if (comp != 0) return AST(rootToken, ast.left, add(token, ast.right!!))
        }
        if(ast.left != null) {
            val comp = compareValueAndType(token, ast.left!!)
            if (comp != 0) return  AST(rootToken, add(token, ast.left!!), ast.right)
        }
        return ast.addChildren(getLeaf(token))
    }

    private fun findEmptyLeaf(token: Token, ast: ASTInterface) : ASTInterface {
        return if(ast.left == null) AST(ast.token, getLeaf(token), ast.right)
        else AST(ast.token, ast.left, getLeaf(token))
    }

    private fun rootIsBigger(compareTokens: Int) = compareTokens == -1
}