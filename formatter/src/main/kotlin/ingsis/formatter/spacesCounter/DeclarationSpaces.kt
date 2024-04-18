package ingsis.formatter.spacesCounter

import ingsis.formatter.defaultConfig.getDefaultBeforeDeclarationSpaces
import ingsis.formatter.utils.FormatterRule
import ingsis.formatter.utils.generateSpaces

class DeclarationSpaces(
    private val ruleMap: Map<String, FormatterRule>,
) {
    fun getDeclarationSpaces(ruleName: String): String {
        return if ((ruleMap[ruleName]!!).isOn()) {
            generateSpaces(ruleMap[ruleName]!!.quantity)
        } else {
            generateSpaces(getDefaultBeforeDeclarationSpaces())
        }
    }
}
