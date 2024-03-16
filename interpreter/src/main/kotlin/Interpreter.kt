package modules.interpreter

import Variable
import VariableType
import components.TokenType
import components.ast.ASTInterface


class Interpreter {

    fun interpret(ast: ASTInterface) {

    }

    fun addVariableToMap(ast: ASTInterface): Map<String, Variable> {
        val root = ast.token!!.type
        val variableMap = HashMap<String, Variable>()
        when (root) {

            TokenType.DECLARATION -> {
                val variableName = ast.children[0].token!!.value
                val variableType = ast.children[1].token!!.value
                variableMap[variableName] = Variable(changeTokenTypeToVariableType(variableType), null)
            }


            TokenType.ASSIGNATION -> {
                if (!checkIfAssignationAndDeclaration(ast.children[0].token!!.type)) {
                    val variableName = ast.children[0].token!!.value
                    val variableValue = ast.children[1].token!!.value
                    variableMap[variableName] = Variable(VariableType.STRING, variableValue)
                } else {
                    val variableName = ast.children[0].children[0].token!!.value
                    val variableType = ast.children[0].children[1].token!!.value
                    val variableValue = recursiveSearch(ast.children[1], changeTokenTypeToVariableType(variableType))
                    variableMap[variableName] = Variable(changeTokenTypeToVariableType(variableType), variableValue)
                }
            }

            TokenType.TYPE -> TODO()
            TokenType.SEMICOLON -> TODO()
            TokenType.OPERATOR -> TODO()
            TokenType.KEYWORD -> TODO()
            TokenType.IDENTIFIER -> TODO()
            TokenType.COMMENT -> TODO()
            TokenType.VALUE -> TODO()
            TokenType.FUNCTION -> TODO()
            TokenType.PARENTHESIS -> TODO()
        }
        return variableMap
    }

    private fun printLine(ast: ASTInterface, variableMap: Map<String, Variable>) {

    }

    private fun checkIfAssignationAndDeclaration(type: TokenType): Boolean {
        //checks if in the declaration has also a declaration of the variable
        //example of declaration and assignation: let a: number = 5
        //just declaration would be: let a: number
        return type == TokenType.DECLARATION
    }

    private fun recursiveSearch(ast: ASTInterface, variableType: VariableType): String {
        return when (val root = ast.token!!.type) {
            TokenType.VALUE -> ast.token!!.value
            TokenType.OPERATOR -> {
                val leftOperand = recursiveSearch(ast.children[0], variableType)
                val rightOperand = recursiveSearch(ast.children[1], variableType)
                when (ast.token!!.value) {
                    "+" -> if (variableType == VariableType.STRING) {
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

    private fun changeTokenTypeToVariableType(type: String): VariableType {
        return when (type) {
            "string" -> VariableType.STRING
            "number" -> VariableType.NUMBER
            else -> throw IllegalArgumentException("Unknown token type: $type")
        }
    }
}