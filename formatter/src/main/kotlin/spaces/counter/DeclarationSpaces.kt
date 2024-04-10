package spaces.counter

import utils.FormatterRule
import utils.generateSpaces

class DeclarationSpaces(
    private val ruleMap: Map<String, FormatterRule>,
) {
    fun getBeforeDeclarationSpaces(): String {
        return if ((ruleMap["beforeDeclaration"]!!).isOn()) {
            generateSpaces(ruleMap["beforeDeclaration"]!!.quantity)
        } else {
            generateSpaces(1)
        }
    }

    fun getAfterDeclarationSpaces(): String {
        return if (ruleMap["afterDeclaration"]!!.isOn()) {
            generateSpaces(ruleMap["afterDeclaration"]!!.quantity)
        } else {
            generateSpaces(1)
        }
    }
}
