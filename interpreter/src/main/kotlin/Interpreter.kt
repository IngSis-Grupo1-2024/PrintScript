package modules.interpreter

import Variable
import components.Token
import components.TokenType
import components.ast.ASTInterface


class           Interpreter {


    fun interpret(ast: ASTInterface) : Map<String, Variable> {
        val root = ast.getToken().getType()
        val variableMap = HashMap<String, Variable>()
        when (root) {
            TokenType.DECLARATION -> {
                interpret(variableMap, getVariableName(ast), getVariableType(ast))
            }

            TokenType.ASSIGNATION -> {
                if (!checkIfAssignationAndDeclaration(ast.getChildren()[0].getToken().getType())) {
                    val variableName = getVariableName(ast)
                    val variableType = searchForVariableType(variableName, variableMap)
                    val variableValue = recursiveSearch(ast.getChildren()[1],variableType)
                    interpret(variableMap, variableName, searchForVariableType(variableName, variableMap), variableValue)
                } else {
                    val variableName = getVariableName(ast.getChildren()[0])
                    val variableType = getVariableType(ast.getChildren()[0])
                    val variableValue = recursiveSearch(ast.getChildren()[1], variableType)
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
        val variableType = ast.getChildren()[1].getToken().getType()
        return variableType
    }

    private fun getVariableName(ast: ASTInterface): String {
        val variableName = ast.getChildren()[0].getToken().getValue()
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

    private fun interpret(
        variableMap: HashMap<String, Variable>,
        variableName: String,
        variableType: TokenType,
    ) {
        variableMap[variableName] = Variable(variableType)
    }

    private fun searchForVariableType(variableName: String, variableMap: HashMap<String, Variable>): TokenType {
        return variableMap[variableName]!!.getType()

    }


    fun printLine(ast: ASTInterface, variableMap: Map<String, Variable>) {
        //If the first child has more than 1 child we loop
        if (ast.getChildren()[0].getChildren().size > 1) {
                val types = ArrayList<TokenType>()
                val values = ArrayList<String>()
                //We get all the types and values of the ast in order. Left, Centre, Right
                stackTypesAndValues(ast.getChildren()[0], types, values, variableMap)
                if (types.contains(TokenType.STRING)) {
                    //If we have at least one string in the AST, we concatenate the items
                    println(calculateStringResult(values))
                } else {
                    //If inside the print we have a mathematical expression, we loop through the items
                    println(calculateMathResult(values))
                }
        }
        //If the first child does not have getChildren(), we print the value of the first child
        else {
            println(getVariableOrValue(ast.getChildren()[0].getToken(), variableMap))
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
        return when (val root = ast.getToken().getType()) {
            TokenType.STRING  -> ast.getToken().getValue()
            TokenType.INTEGER -> ast.getToken().getValue() //Esto se podría encargar de mirarlo el SCA, en caso de que no vengan operators o string o ints
            TokenType.OPERATOR -> {
                val leftOperand = recursiveSearch(ast.getChildren()[0], tokenType)
                val rightOperand = recursiveSearch(ast.getChildren()[1], tokenType)
                when (ast.getToken().getValue()) {
                    "+" -> if (tokenType == TokenType.STRING) {
                        leftOperand + rightOperand
                    } else (leftOperand.toInt() + rightOperand.toInt()).toString()

                    "-" -> (leftOperand.toInt() - rightOperand.toInt()).toString()
                    "*" -> (leftOperand.toInt() * rightOperand.toInt()).toString()
                    "/" -> (leftOperand.toInt() / rightOperand.toInt()).toString()
                    else -> throw IllegalArgumentException("Unknown operator: ${ast.getToken().getValue()}")
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
        if (ast.getChildren().isEmpty()) {
            addToTypeAndValueList(tokenTypes, ast, variableMap, values)
        } else {
                stackTypesAndValues(ast.getChildren()[0], tokenTypes, values, variableMap)
                addToTypeAndValueList(tokenTypes, ast, variableMap, values)
                stackTypesAndValues(ast.getChildren()[1], tokenTypes, values, variableMap)
        }
    }

    private fun addToTypeAndValueList(
        tokenTypes: ArrayList<TokenType>,
        ast: ASTInterface,
        variableMap: Map<String, Variable>,
        values: ArrayList<String>
    ) {
        tokenTypes.add(getVariableOrValueType(ast.getToken(), variableMap))
        values.add(getVariableOrValue(ast.getToken(), variableMap))
    }


    private fun checkIfIsIdentifier(itemToken: Token): Boolean {
        return itemToken.getType() == TokenType.IDENTIFIER
    }

    private fun getVariableOrValueType(itemToken: Token, variableMap: Map<String, Variable>): TokenType {
        if (checkIfIsIdentifier(itemToken)) {
            return (variableMap[itemToken.getValue()]!!.getType())
        }
        return itemToken.getType()
    }

    private fun getVariableOrValue(itemToken: Token, variableMap: Map<String, Variable>): String {
        if (checkIfIsIdentifier(itemToken)) {
            return variableMap[itemToken.getValue()]!!.getValue()
        }
        return itemToken.getValue()
    }


}