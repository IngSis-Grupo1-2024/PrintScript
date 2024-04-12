package new.line.counter

import utils.FormatterRule
import utils.generateNewLines

class PrintNewLines(
    private val ruleMap: Map<String, FormatterRule>,
) {
    fun getPrintlnLines(): String {
        return if (ruleMap["println"]!!.isOn()) {
            generateNewLines(ruleMap["println"]!!.quantity)
        } else {
            generateNewLines(1)
        }
    }
}
