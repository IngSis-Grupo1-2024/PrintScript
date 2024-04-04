package scaRules

import components.TokenType
import components.ast.ASTInterface

class TypeValueRule : Rule {
    private var assignedType = ""
    private var assignedValue = ""

    override fun validate(ast: ASTInterface): Boolean {
        return recursiveTypeValidation(ast)
    }

//    private fun validateType(ast: ASTInterface): Boolean {
//        val astChildren = ast.getChildren();
//        var assignedType = ""
//        if (ast.getToken().getType() == TokenType.ASSIGNATION) {
//            for (child in astChildren) {
//                if (child.getToken().getType() == TokenType.TYPE) {
//                    assignedType = child.getToken().getValue()
//                }
//                if (child.getToken().getType() == TokenType.INTEGER || child.getToken().getType() == TokenType.STRING) {
//                    if (child.getToken().getType() == TokenType.INTEGER && assignedType == "number") {
//                        return true
//                    }
//                    if (child.getToken().getType() == TokenType.STRING && assignedType == "string") {
//                        return true
//                    }
//                }
//            }
//            return false
//        }
//        return false
//    }

    private fun recursiveTypeValidation(ast: ASTInterface): Boolean {
        val rootType = ast.getToken().getType()
        when (rootType) {
            TokenType.ASSIGNATION -> {
                recursiveTypeValidation(ast.getChildren()[0])
                recursiveTypeValidation(ast.getChildren()[1])
            }

            TokenType.TYPE -> {
                assignedType = ast.getToken().getValue()
            }

            TokenType.INTEGER -> {
                assignedValue = "number"
            }

            TokenType.STRING -> {
                assignedValue = "string"
            }

            TokenType.OPERATOR -> {
                assignedValue = getOperationType(ast)
            }

            else -> {
                for (child in ast.getChildren()) {
                    recursiveTypeValidation(child)
                }
            }
        }
        return assignedType == assignedValue
    }

    private fun getOperationType(ast: ASTInterface): String {
        if (IntegerAndStringOperationsRule().validate(ast)) {
            return "string"
        }
        return "number"
    }

//    private fun recursiveTypeValidation(ast: ASTInterface): Boolean {
//        var leftAst = ast.getChildren()[0]
//        val rightAst = ast.getChildren()[1]
//        var assignedType = ""
//        var assignedValue = ""
//        if (leftAst.getChildren().isNotEmpty()) {
//            for (child in leftAst.getChildren()) {
//                if (child.getToken().getType() == TokenType.TYPE) {
//                    assignedType = child.getToken().getValue()
//                }
//            }
//        }
//        if (rightAst.getChildren().isNotEmpty()) {
//            for (child in rightAst.getChildren()) {
//                if (child.getToken().getType() == TokenType.INTEGER) {
//                    assignedValue = "number"
//                }
//                if (child.getToken().getType() == TokenType.STRING) {
//                    assignedValue = "string"
//                }
//            }
//        }
//        else {
//            if (rightAst.getToken().getType() == TokenType.INTEGER) {
//                assignedValue = "number"
//            }
//            if (rightAst.getToken().getType() == TokenType.STRING) {
//                assignedValue = "string"
//            }
//        }
//        return assignedType == assignedValue
//    }
}
