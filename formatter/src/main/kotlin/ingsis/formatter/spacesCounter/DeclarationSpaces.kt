package ingsis.formatter.spacesCounter

import ingsis.formatter.utils.FormatterRule
import ingsis.formatter.utils.generateSpaces
import ingsis.formatter.defaultConfig.getDefaultBeforeDeclarationSpaces

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
