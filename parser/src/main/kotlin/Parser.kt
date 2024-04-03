import components.*
import components.ast.AST
import components.ast.ASTInterface
import error.ParserError
import kotlin.math.abs

class Parser : ParserInterface {
    private val typeComparator = ComparatorTokenType()
    private val valueComparator = ComparatorTokenValue()
    private val DOESNTMATTERTYPES = listOf(TokenType.PARENTHESIS, TokenType.KEYWORD)
    private val VALUETYPES = listOf(TokenType.INTEGER, TokenType.STRING, TokenType.IDENTIFIER)
    private val FUNCIONTYPES = listOf(TokenType.FUNCTION, TokenType.PARENTHESIS)
    private val twoChildrenType = listOf(TokenType.OPERATOR, TokenType.ASSIGNATION, TokenType.DECLARATION)

    override fun parse(tokens: List<Token>): ASTInterface {
        if (tokens.last().getType() != TokenType.SEMICOLON)
            throw ParserError("error: ';' expected  " + tokens.last().getPosition(), tokens.last())
        val tokWSemicolon = tokens.subList(0, tokens.size - 1)
        if (checkDeclaration(tokWSemicolon)) return transformDeclaration(tokWSemicolon)
        else if (isAssignation(tokWSemicolon)) return transformAssignation(tokWSemicolon)
        else if (isCallingMethod(tokWSemicolon)) return transformFunction(tokWSemicolon)
        throw ParserError("PrintScript couldn't parse that code " + tokens[0].getPosition(), tokens[0])
    }

    private fun transformDeclaration(tokens: List<Token>): ASTInterface {
        val decl = tokens[2]
        val identifier = tokens[1]
        val keyword = tokens[0]
        val value = tokens[3]
        return AST(decl, listOf(getLeaf(keyword), getLeaf(identifier), getLeaf(value)))
    }

    private fun transformAssignation(tokens: List<Token>): ASTInterface {
        val assignIndex: Int = findAssignIndex(tokens)
        val ident: ASTInterface = transformIdent(tokens.subList(0, assignIndex))
        val assign = tokens[assignIndex]
        val value = transformValue(tokens.subList(assignIndex + 1, tokens.size))
        return AST(assign, listOf(ident, value))
    }

    private fun transformFunction(tokens: List<Token>): ASTInterface {
        val function = tokens[0]
        val value = transformValue(tokens.subList(2, tokens.size-1))
        return AST(function, listOf(value))
    }

    private fun transformIdent(tokens: List<Token>): ASTInterface {
        if (tokens.size == 1) return getLeaf(tokens[0])
        return transformDeclaration(tokens)
    }

    private fun findAssignIndex(tokens: List<Token>): Int {
        for (i in tokens.indices)
            if (tokens[i].getType() == TokenType.ASSIGNATION) return i
        return -1
    }

    private fun transformValue(tokens: List<Token>): ASTInterface {
        if (tokens.size == 1) return getLeaf(tokens[0])
        return getTree(tokens)
    }

    private fun getTree(tokens: List<Token>): ASTInterface {
        return getTreeWithAst(tokens, getEmptyAST())
    }

    private fun getTreeWithAst(tokens: List<Token>, ast: ASTInterface): ASTInterface {
        var tree = ast
        for (i in tokens.indices) {
            if (tree.isEmpty()){
                tree = tree.addChildren(getLeaf(tokens[i]))
                continue
            }
            if(tokens[i].getType() == TokenType.PARENTHESIS) {
                tree = resolveParenthesis(tree, tokens, i)
                val endIndex = searchEndParenthesisIndex(tokens, i)
                return getTreeWithAst(tokens.subList(endIndex+1, tokens.size), tree)
            }
            tree = add(tokens[i], tree)
        }
        return tree
    }

    private fun resolveParenthesis(originalAST: ASTInterface, tokens: List<Token>, startParenthesis: Int): ASTInterface {
        val endParenthesis = searchEndParenthesisIndex(tokens, startParenthesis)
        val ast = getTree(tokens.subList(startParenthesis+1, endParenthesis))
        return addInLastChild(originalAST, ast)
    }

    private fun addInLastChild(originalAST: ASTInterface, ast: ASTInterface): ASTInterface {
        if(originalAST.getChildrenAmount() < 2 && originalAST.getToken().getType() in twoChildrenType)
            return originalAST.addChildren(ast)
        return addInLastChild(originalAST.getChildren().last(), ast)
    }

    private fun searchEndParenthesisIndex(tokens: List<Token>, i: Int): Int {
        var qtyStartParenthesis = 0
        for(j in i+1..<tokens.size){
            if(tokens[j].getType() == TokenType.PARENTHESIS && tokens[j].getValue() == ")"){
                if(qtyStartParenthesis == 0) return j
                else qtyStartParenthesis--
            }
            else if(tokens[j].getType() == TokenType.PARENTHESIS && tokens[j].getValue() == "(")
                qtyStartParenthesis++
        }
        throw ParserError("error: expected ')'", tokens[tokens.size-1])
    }

    private fun getEmptyAST(): ASTInterface {
        return AST()
    }

    private fun getLeaf(token: Token): ASTInterface = AST(token)


    // lo que podes hacer es chequear con los hijos
    // sabes que si hay un identifier, un type o un const (o value) esos si o si
    // tienen que ser hoja, por lo que si se encuentran con otro, lo mandas como hermano
    private fun add(token: Token, ast: ASTInterface): ASTInterface {
        if (token.getType() in DOESNTMATTERTYPES) return ast
        val compareTokens = compareValueAndType(token, ast.getToken())
        if (ast.getChildrenAmount() < 2 && ast.getToken().getType() in twoChildrenType && rootIsBigger(compareTokens))
            return ast.addChildren(getLeaf(token))
        return if (rootIsBigger(compareTokens)) compWChildren(token, ast)
        else if (compareTokens == 1) AST(token, ast)
        else if (abs(compareTokens) == 2) ast
        else removeLastChild(ast, token)
    }

    private fun compareValueAndType(token: Token, root: Token): Int {
        val compToken = typeComparator.compare(token.getType(), root.getType())
        return if (compToken != 0) compToken
        else valueComparator.compare(token, root)
    }

    private fun compWChildren(token: Token, ast: ASTInterface): ASTInterface {
        for (child in ast.getChildren()) {
            val comp = compareValueAndType(token, child.getToken())
            when (comp) {
                // child's root bigger than token
                -1 -> return removeLastChild(ast, token)
                // token bigger than child's root
                1 -> return removeLastChild(ast, token)
            }
        }
        return if(ast.getChildrenAmount() < 2 && ast.getToken().getType() in twoChildrenType) ast.addChildren(getLeaf(token))
        else removeLastChild(ast, token)
    }

    private fun removeLastChild(ast: ASTInterface, token: Token): ASTInterface {
        val lastChild = ast.getChildren().last()
        return ast.replace(lastChild, add(token, lastChild))
    }

    private fun rootIsBigger(compareTokens: Int) = compareTokens == -1

    // this would check if tokens apply to the declaration structure:
    // let x: string;
    private fun checkDeclaration(tokens: List<Token>): Boolean {
        val declarationTypes = listOf(TokenType.KEYWORD, TokenType.IDENTIFIER, TokenType.DECLARATION, TokenType.TYPE)

        val tokenTypes = tokens.map { it.getType() }
        val declarationTypesPresent = declarationTypes.intersect(tokenTypes.toSet())
        if(declarationTypes == tokenTypes) return true
        else if (declarationTypesPresent.size > 2 && checkCollections(declarationTypesPresent, tokenTypes))
            throw ParserError(
                "error: to declare a variable, it's expected to do it by 'let <name of the variable>: <type of the variable>'",
                tokens[0]
            )

        return false
    }

    private fun checkCollections(declarationTypesPresent: Set<TokenType>, tokenTypes: List<TokenType>): Boolean {
        if(tokenTypes.size != declarationTypesPresent.size ) return false
        for((i, type) in declarationTypesPresent.withIndex()){
            if(i>=tokenTypes.size) return false
            if(tokenTypes[i] != type) return false
        }
        return true
    }

    private fun isAssignation(tokens: List<Token>): Boolean {
        // there are 2 options for assignation:
        // declaration + assignation OR just declaration
        if(tokens.size < 3) return false
        val assignIndex = findAssignIndex(tokens)
        if (assignIndex == -1) return false
        if (checkDeclaration(tokens.subList(0, assignIndex)) || checkIdentifier(tokens.subList(0, assignIndex)))
            return checkValue(tokens, assignIndex+1, tokens.size)
        return false
    }

    private fun checkIdentifier(tokens: List<Token>): Boolean {
        return if (tokens.size != 1) false
        else tokens[0].getType() == TokenType.IDENTIFIER
    }

    private fun isCallingMethod(tokens: List<Token>): Boolean {
        // this would be the calling function structure that comes from lexer:
        // print("hello")
        // sum(a + b)
        val tokenTypes = tokens.map { it.getType() }
        if (tokenTypes.subList(0, FUNCIONTYPES.size) == FUNCIONTYPES) {
            if (tokenTypes.last() != TokenType.PARENTHESIS) throw ParserError(
                tokens.last().getPosition().startLine.toString() + ":error: ')' expected " + tokens.last().getPosition(),
                tokens.last()
            )
            return checkValue(tokens, FUNCIONTYPES.size, tokens.size-1)
        }
        return false
    }

    private fun checkValue(tokens: List<Token>, startIndex: Int, endIndex: Int): Boolean {
        try{
            val tokenList = tokens.subList(startIndex, endIndex)
            val invalidValueTypes =
                listOf(TokenType.TYPE, TokenType.DECLARATION, TokenType.ASSIGNATION, TokenType.SEMICOLON, TokenType.KEYWORD)

            tokenList
                .filter { it.getType() in invalidValueTypes }
                .forEach { throw ParserError("Invalid value type ${it.getPosition()}", it)}
            return checkQtyOperatorsAndValues(tokenList)
        }
        catch (e: NoSuchElementException){
            throw ParserError("error: expected value", tokens.last())
        }
    }

    private fun checkQtyOperatorsAndValues(tokens: List<Token>): Boolean {
        val numberOfOpp: Int = tokens.filter { it.getType() == TokenType.OPERATOR }.size
        val numberOfValue: Int = tokens.filter { it.getType() in VALUETYPES }.size

        if (numberOfValue == numberOfOpp + 1) return true
        if (numberOfOpp == 0 && numberOfValue == 0) throw ParserError("error: expected value", tokens.last())
        throw ParserError("error: wrong number of values and operators", tokens.last())
    }

}