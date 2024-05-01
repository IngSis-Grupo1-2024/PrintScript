package ingsis.formatter.tabsCounter

import ingsis.formatter.defaultConfig.getDefaultIfTabs
import ingsis.formatter.utils.FormatterRule
import ingsis.formatter.utils.generateTabs

class IfTabs {
    fun getTabs(ruleMap: Map<String, FormatterRule>): String {
        return if (ruleMap.containsKey("if") && ruleMap["if"]!!.isOn()) {
            generateTabs(ruleMap["if"]!!.quantity)
        } else {
            generateTabs(getDefaultIfTabs())
        }
    }
}
