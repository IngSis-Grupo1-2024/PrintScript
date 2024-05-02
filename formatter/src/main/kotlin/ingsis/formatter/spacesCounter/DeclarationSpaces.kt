package ingsis.formatter.spacesCounter

import ingsis.formatter.defaultConfig.getDefaultDeclarationSpaces
import ingsis.formatter.utils.FormatterRule
import ingsis.formatter.utils.generateSpaces

class DeclarationSpaces(
    private val ruleMap: Map<String, FormatterRule>,
) {
    fun getDeclarationSpaces(ruleName: String): String {
        return if (ruleMap.containsKey(ruleName) && ruleMap[ruleName]!!.isOn()) {
            generateSpaces(ruleMap[ruleName]!!.quantity)
        } else {
            generateSpaces(getDefaultDeclarationSpaces())
        }
    }
}
