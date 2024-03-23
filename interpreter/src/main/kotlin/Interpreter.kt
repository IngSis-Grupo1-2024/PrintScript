package modules.interpreter

import Variable
import components.Token
import components.TokenType
import components.ast.ASTInterface


class Interpreter {

    fun addVariableToMap(ast: ASTInterface): Map<String, Variable> {
        val root = ast.getToken().getType()
    fun interpret(ast: ASTInterface) : Map<String, Variable> {
        val root = ast.token!!.type
        val variableMap = HashMap<String, Variable>()
        when (root) {
            TokenType.DECLARATION -> {
                interpret(variableMap, getVariableName(ast), getVariableType(ast), null.toString())
            }

            TokenType.ASSIGNATION -> {
                if (!checkIfAssignationAndDeclaration(ast.children[0].token!!.type)) {
                    val variableName = getVariableName(ast)
                    val variableType = searchForVariableType(variableName, variableMap)
                    val variableValue = recursiveSearch(ast.children[1],variableType)
                    interpret(variableMap, variableName, searchForVariableType(variableName, variableMap), variableValue)
                } else {
                    val variableName = getVariableName(ast.children[0])
                    val variableType = getVariableType(ast.children[0])
                    val variableValue = recursiveSearch(ast.children[1], variableType)
                    interpret(variableMap, variableName, variableType, variableValue)
                }
            }

            TokenType.FUNCTION -> {
                printLine(ast, variableMap)
            }

            else -> {}
        }
        return variableMap
    }

    private fun getVariableType(ast: ASTInterface): TokenType {
        val variableType = ast.children[1].token!!.type
        return variableType
    }

    private fun getVariableName(ast: ASTInterface): String {
        val variableName = ast.children[0].token!!.value
        return variableName
    }

    private fun interpret(
        variableMap: HashMap<String, Variable>,
        variableName: String,
        variableType: TokenType,
        variableValue: String
    ) {
        variableMap[variableName] = Variable(variableType, variableValue)
    }

    private fun searchForVariableType(variableName: String, variableMap: HashMap<String, Variable>): TokenType {
        return variableMap[variableName]!!.getType()

    }


    fun printLine(ast: ASTInterface, variableMap: Map<String, Variable>) {
        //If the first child has more than 1 child we loop
        if (ast.children[0].children.size > 1) {
                val types = ArrayList<TokenType>()
                val values = ArrayList<String>()
                //We get all the types and values of the ast in order. Left, Centre, Right
                stackTypesAndValues(ast.children[0], types, values, variableMap)
                if (types.contains(TokenType.STRING)) {
                    //If we have at least one string in the AST, we concatenate the items
                    println(calculateStringResult(values))
                } else {
                    //If inside the print we have a mathematical expression, we loop through the items
                    println(calculateMathResult(values))
                }
        }
        //If the first child does not have children, we print the value of the first child
        else {
            println(getVariableOrValue(ast.children[0].token!!, variableMap))
        }
    }

    private fun calculateStringResult(values: ArrayList<String>): String {
        var result = ""
        //We grab the values and concatenate using a step 2, to avoid the "+" operator
        for (i in 0 until values.size step 2) {
            result += values[i]
        }
        return result
    }

    private fun calculateMathResult(values: ArrayList<String>): Int {
        //The result is initialized with the first value
        var result = Integer.parseInt(values[0])
        //We grab the values and concatenate using a step 2 in order to apply the operator to the result and the value
        //If we have 3 + 2, we initialize result with 3, and then we add 2 to the result
        for (i in 1 until values.size step 2) {
            when (val operator = values[i]) {
                "+" -> result += Integer.parseInt(values[i + 1])
                "-" -> result -= Integer.parseInt(values[i + 1])
                "*" -> result *= Integer.parseInt(values[i + 1])
                "/" -> result /= Integer.parseInt(values[i + 1])
                else -> throw IllegalArgumentException("Unsupported operator: $operator")
            }
        }
        return result
    }

    private fun checkIfAssignationAndDeclaration(type: TokenType): Boolean {
        //checks if in the declaration has also a declaration of the variable
        //example of declaration and assignation: let a: number = 5
        //just declaration would be: let a: number
        return type == TokenType.DECLARATION
    }

    private fun recursiveSearch(ast: ASTInterface, tokenType: TokenType): String {
        return when (val root = ast.token!!.type) {
            TokenType.STRING  -> ast.token!!.value
            TokenType.INTEGER -> ast.token!!.value //Esto se podría encargar de mirarlo el SCA, en caso de que no vengan operators o string o ints
            TokenType.OPERATOR -> {
                val leftOperand = recursiveSearch(ast.children[0], tokenType)
                val rightOperand = recursiveSearch(ast.children[1], tokenType)
                when (ast.token!!.value) {
                    "+" -> if (tokenType == TokenType.STRING) {
                        leftOperand + rightOperand
                    } else (leftOperand.toInt() + rightOperand.toInt()).toString()

                    "-" -> (leftOperand.toInt() - rightOperand.toInt()).toString()
                    "*" -> (leftOperand.toInt() * rightOperand.toInt()).toString()
                    "/" -> (leftOperand.toInt() / rightOperand.toInt()).toString()
                    else -> throw IllegalArgumentException("Unknown operator: ${ast.token!!.value}")
                }
            }

            else -> throw IllegalArgumentException("Unknown token type: $root")
        }
    }


    //This function loops through the entire print tree in order to search for the different types and values in the ast
    private fun stackTypesAndValues(
        ast: ASTInterface,
        tokenTypes: ArrayList<TokenType>,
        values: ArrayList<String>,
        variableMap: Map<String, Variable>
    ) {
        if (ast.children.isEmpty()) {
            addToTypeAndValueList(tokenTypes, ast, variableMap, values)
        } else {
                stackTypesAndValues(ast.children[0], tokenTypes, values, variableMap)
                addToTypeAndValueList(tokenTypes, ast, variableMap, values)
                stackTypesAndValues(ast.children[1], tokenTypes, values, variableMap)
        }
    }

    private fun addToTypeAndValueList(
        tokenTypes: ArrayList<TokenType>,
        ast: ASTInterface,
        variableMap: Map<String, Variable>,
        values: ArrayList<String>
    ) {
        tokenTypes.add(getVariableOrValueType(ast.token!!, variableMap))
        values.add(getVariableOrValue(ast.token!!, variableMap))
    }


    private fun checkIfIsIdentifier(itemToken: Token): Boolean {
        return itemToken.type == TokenType.IDENTIFIER
    }

    private fun getVariableOrValueType(itemToken: Token, variableMap: Map<String, Variable>): TokenType {
        if (checkIfIsIdentifier(itemToken)) {
            return (variableMap[itemToken.value]!!.type)
        }
        return itemToken.type
    }

    private fun getVariableOrValue(itemToken: Token, variableMap: Map<String, Variable>): String {
        if (checkIfIsIdentifier(itemToken)) {
            return variableMap[itemToken.value]!!.value!!
        }
        return itemToken.value
    }


}