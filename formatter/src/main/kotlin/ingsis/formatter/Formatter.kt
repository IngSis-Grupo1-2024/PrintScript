package ingsis.formatter
import com.google.gson.Gson
import components.Position
import components.TokenType
import components.ast.AST
import components.ast.ASTInterface
import java.io.File


data class Rule(
    val on: Boolean,
    val quantity: Int,
    val name: String,
)



class Formatter() {

    fun format(input:ASTInterface):ASTInterface{
        val ruleMap = readJsonsAndStackMap(listOf("src/main/rules/rulesBeforeDeclaration.json", "src/main/rules/rulesAfterDeclaration.json"))
        var inputAux = input

        when(inputAux.getToken().getType()){
            TokenType.DECLARATION -> {
                val diffBeforeDeclaration = getBeforeDeclarationDiff(inputAux, inputAux.getChildren()[1], ruleMap, "beforeDeclaration")
                if( diffBeforeDeclaration != 0 && ruleApplies(ruleMap["beforeDeclaration"]!!) ){
                    inputAux = shift(inputAux, 1, diffBeforeDeclaration,true)
                }
                val diffAfterDeclaration = getAfterDeclarationDiff(inputAux, inputAux.getChildren()[2], ruleMap, "afterDeclaration")
                if ( diffAfterDeclaration != 0 && ruleApplies(ruleMap["afterDeclaration"]!!)){
                    inputAux = shift(inputAux, 2, diffAfterDeclaration,false)
                }
            }
            else -> {
                return inputAux
            }

        }
        return inputAux
    }


    //If the quantity is negative that means it's going to shift right
    private fun shift(ast: ASTInterface, index: Int, quantity: Int, shiftToken:Boolean): ASTInterface {
        var newToken = ast.getToken().copy()
        if (shiftToken){
            newToken = ast.getToken().copy(position = shiftPosition(ast, quantity))
        }

        val children = ast.getChildren().mapIndexed { i, child ->
            if (i >= index) {
                val newPosition = shiftPosition(child, quantity)
                AST(child.getToken().copy(position = newPosition), child.getChildren())
            } else {
                child.copy()
            }
        }

        return AST(newToken, children)
    }


    //If the quantity is negative that means it's going to move the position to the right
    private fun shiftPosition(ast: ASTInterface, quantity: Int): Position {
        return ast.getToken().getPosition().copy(
            startOffset = ast.getToken().getPosition().startOffset - quantity,
            endOffset = ast.getToken().getPosition().endOffset - quantity,
            startColumn = ast.getToken().getPosition().startColumn - quantity,
            endColumn = ast.getToken().getPosition().endColumn - quantity
        )
    }



    private fun readJsonsAndStackMap(jsonPaths: List<String>): Map<String, Rule> {
        val gson = Gson()
        val ruleMap = HashMap<String, Rule>()
        for (jsonPath in jsonPaths) {
            val jsonText = File(jsonPath).readText()
            val rule = gson.fromJson(jsonText, Rule::class.java)
            ruleMap[rule.name] = rule
        }
        return ruleMap
    }


    //If the result is negative that means it needs spaces, so it is going to shift right
    private fun getBeforeDeclarationDiff(token1: ASTInterface, token2: ASTInterface, ruleMap:Map<String, Rule>, ruleName:String):Int {
        val identifierPos = token2.getToken().getPosition().endOffset
        val declarationPos = token1.getToken().getPosition().startOffset
        return (declarationPos-(identifierPos+1)) - ruleMap[ruleName]!!.quantity
    }

    //If the result is negative that means it needs spaces, so it is going to shift right
    private fun getAfterDeclarationDiff(token1: ASTInterface, token2: ASTInterface, ruleMap:Map<String, Rule>, ruleName:String):Int {
        val declarationPos = token1.getToken().getPosition().endOffset
        val typePos = token2.getToken().getPosition().startOffset
        return (typePos-(declarationPos+1)) - ruleMap[ruleName]!!.quantity
    }

    private fun ruleApplies(rule: Rule): Boolean {
        return rule.on
    }



}