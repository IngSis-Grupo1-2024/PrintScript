package ingsis.parser.scan

import ingsis.components.*
import ingsis.components.statement.EmptyValue
import ingsis.components.statement.Operator
import ingsis.components.statement.SingleValue
import ingsis.components.statement.Value
import ingsis.parser.error.ParserError
import kotlin.math.abs

object PrintScriptScanValue {
    fun createScanValue(version: String): ScanValue {
        return when (version) {
            "VERSION_1" -> ScanValue(valueTypesV1(), comparatorTokenType(version))
            "VERSION_2" -> ScanValue(valueTypesV2(), comparatorTokenType(version))
            else -> ScanValue(valueTypesV1(), comparatorTokenType(version))
        }
    }

    private fun comparatorTokenType(version: String) = PrintScriptComparatorTokenType.createComparatorTokenType(version)

    private fun valueTypesV1() = listOf(TokenType.DOUBLE, TokenType.INTEGER, TokenType.STRING, TokenType.IDENTIFIER)

    private fun valueTypesV2() = valueTypesV1() + listOf(TokenType.BOOLEAN)
}

class ScanValue(private val valueTypes: List<TokenType>, private val typeComparator: ComparatorTokenType) : ValueScanner {
    private val twoChildrenType = listOf(TokenType.OPERATOR)
    private val valueComparator = ComparatorTokenValue()

    override fun canHandle(tokens: List<Token>): Boolean {
        if (tokens.isEmpty()) return true
        val validTypes =
            valueTypes + twoChildrenType + listOf(TokenType.PARENTHESIS)

        tokens
            .filter { it.getType() !in validTypes }
            .forEach { throw ParserError("Invalid value type.", it) }

        return checkQtyOperatorsAndValues(getNumberOfOpp(tokens), getNumberOfValue(tokens), tokens.last())
    }

    override fun makeValue(tokens: List<Token>): Value {
        if (tokens.isEmpty()) return EmptyValue()
        if (tokens.size == 1) return getSingleValue(tokens[0])
        return getOperator(tokens)
    }

    private fun getOperator(tokens: List<Token>): Value = getTreeWithAst(tokens, EmptyValue())

    private fun getSingleValue(token: Token): Value = SingleValue(token)

    private fun checkQtyOperatorsAndValues(
        numberOfOpp: Int,
        numberOfValue: Int,
        lastToken: Token,
    ): Boolean {
        if (numberOfValue == numberOfOpp + 1) return true
        throw ParserError("error: wrong number of values and operators", lastToken)
    }

    private fun getNumberOfValue(tokens: List<Token>) = tokens.filter { it.getType() in valueTypes }.size

    private fun getNumberOfOpp(tokens: List<Token>) = tokens.filter { it.getType() == TokenType.OPERATOR }.size

    private fun getTreeWithAst(
        tokens: List<Token>,
        ast: Value,
    ): Value {
        var tree = ast
        for (i in tokens.indices) {
            if (tree.isEmpty()) {
                tree = SingleValue(tokens[i])
                continue
            }
            if (tokens[i].getType() == TokenType.PARENTHESIS) {
                tree = resolveParenthesis(tree, tokens, i)
                val endIndex = searchEndParenthesisIndex(tokens, i)
                return getTreeWithAst(tokens.subList(endIndex + 1, tokens.size), tree)
            }
            tree = add(tokens[i], tree)
        }
        return tree
    }

    private fun resolveParenthesis(
        originalAST: Value,
        tokens: List<Token>,
        startParenthesis: Int,
    ): Value {
        val endParenthesis = searchEndParenthesisIndex(tokens, startParenthesis)
        val ast = makeValue(tokens.subList(startParenthesis + 1, endParenthesis))
        return addInLastChild(originalAST, ast)
    }

    private fun searchEndParenthesisIndex(
        tokens: List<Token>,
        i: Int,
    ): Int {
        var qtyStartParenthesis = 0
        for (j in i + 1..<tokens.size) {
            if (tokens[j].getType() == TokenType.PARENTHESIS && tokens[j].getValue() == ")") {
                if (qtyStartParenthesis == 0) {
                    return j
                } else {
                    qtyStartParenthesis--
                }
            } else if (tokens[j].getType() == TokenType.PARENTHESIS && tokens[j].getValue() == "(") {
                qtyStartParenthesis++
            }
        }
        throw ParserError("error: expected ')'", tokens[tokens.size - 1])
    }

    private fun addInLastChild(
        originalAST: Value,
        ast: Value,
    ): Value {
        if (originalAST.isEmpty() || originalAST.isLeaf()) return originalAST.addChildren(ast)
        if (newChildrenIsAvailable(originalAST)) {
            return originalAST.addChildren(ast)
        }
        if (originalAST is Operator) {
            return originalAST.replace(originalAST.getLastOperator(), addInLastChild(originalAST.getLastOperator(), ast))
        }
        return originalAST.addChildren(ast)
    }

    private fun add(
        token: Token,
        ast: Value,
    ): Value {
        val compareTokens = compareValueAndType(token, ast.getToken())
        if (newChildrenIsAvailable(ast) && rootIsBigger(compareTokens)) {
            return ast.addChildren(SingleValue(token))
        }
        return if (rootIsBigger(compareTokens)) {
            compWChildren(token, ast)
        } else if (compareTokens == 1) {
            Operator(token, ast)
        } else if (abs(compareTokens) == 2) {
            ast
        } else if (!ast.isLeaf()) {
            removeLastChild(ast as Operator, token)
        } else {
            ast.addChildren(SingleValue(token))
        }
    }

    private fun compareValueAndType(
        token: Token,
        root: Token,
    ): Int {
        val compToken = typeComparator.compare(token.getType(), root.getType())
        return if (compToken != 0) {
            compToken
        } else {
            valueComparator.compare(token, root)
        }
    }

    private fun removeLastChild(
        ast: Operator,
        token: Token,
    ): Operator {
        try {
            val lastChild = ast.getLastOperator()
            return ast.replace(lastChild, add(token, lastChild))
        } catch (e: NullPointerException) {
            return ast.addChildren(SingleValue(token)) as Operator
        }
    }

    private fun rootIsBigger(compareTokens: Int) = compareTokens == -1

    private fun compWChildren(
        token: Token,
        ast: Value,
    ): Value {
        if (ast.getChildrenAmount() == 1) {
            val compLeft = getCompLeft(token, ast as Operator)
            when (compLeft) {
                // child's root bigger than token
                -1 -> return removeLastChild(ast, token)
                // token bigger than child's root
                1 -> return removeLastChild(ast, token)
            }
        }
        if (ast.getChildrenAmount() == 2) {
            val compLeft = getCompLeft(token, ast as Operator)
            when (compLeft) {
                // child's root bigger than token
                -1 -> return removeLastChild(ast, token)
                // token bigger than child's root
                1 -> return removeLastChild(ast, token)
            }
            val compRight = getCompRight(token, ast)
            when (compRight) {
                // child's root bigger than token
                -1 -> return removeLastChild(ast, token)
                // token bigger than child's root
                1 -> return removeLastChild(ast, token)
            }
        }
        return if (newChildrenIsAvailable(ast)) {
            ast.addChildren(SingleValue(token))
        } else if (ast.isLeaf()) {
            removeLastChild(ast as Operator, token)
        } else {
            ast.addChildren(SingleValue(token))
        }
    }

    private fun newChildrenIsAvailable(originalAST: Value) =
        originalAST.getChildrenAmount() < 2 && originalAST.getToken().getType() in twoChildrenType

    private fun getCompLeft(
        token: Token,
        ast: Operator,
    ): Int = compareValueAndType(token, ast.getLeftOperator().getToken())

    private fun getCompRight(
        token: Token,
        ast: Operator,
    ): Int = compareValueAndType(token, ast.getRightOperator().getToken())
}
