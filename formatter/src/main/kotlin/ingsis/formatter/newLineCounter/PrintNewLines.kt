package ingsis.formatter.newLineCounter

import ingsis.formatter.defaultConfig.getDefaultPrintlnNewLines
import ingsis.formatter.utils.FormatterRule
import ingsis.formatter.utils.generateNewLines

class PrintNewLines(
    private val ruleMap: Map<String, FormatterRule>,
) {
    fun getPrintlnLines(): String {
        return if (ruleMap.containsKey("println") && ruleMap["println"]!!.isOn()) {
            generateNewLines(ruleMap["println"]!!.quantity)
        } else {
            generateNewLines(getDefaultPrintlnNewLines())
        }
    }
}
