// package scaRules
//
// import components.TokenType
// import components.ast.ASTInterface
//
class StringOperationsRule {
//    override fun validate(ast: ASTInterface): Boolean {
//        return recursiveSearch(ast, ast.getToken().getType())
//    }
//
//    private fun recursiveSearch(
//        ast: ASTInterface,
//        variableType: TokenType,
//    ): Boolean {
//        val rootType = ast.getToken().getType()
//        return when (rootType) {
//            TokenType.OPERATOR -> {
//                ast.getChildren()[0].getToken().getType() == TokenType.STRING &&
//                    ast.getChildren()[1].getToken().getType() == TokenType.STRING &&
//                    ast.getToken().getValue() == "+"
//            }
//
//            TokenType.ASSIGNATION -> {
//                recursiveSearch(ast.getChildren()[1], variableType)
//            }
//
//            else -> {
//                false
//            }
//        }
//    }
}
