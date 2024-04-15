package ingsis.formatter.spacesCounter

import ingsis.formatter.defaultConfig.getDefaultAssignationSpaces
import ingsis.formatter.utils.FormatterRule
import ingsis.formatter.utils.generateSpaces

class AssignationSpaces(
    private val ruleMap: Map<String, FormatterRule>,
) {
    fun getAssignationSpaces(): String {
        return if ((ruleMap["assignation"]!!.isOn())) {
            generateSpaces(ruleMap["assignation"]!!.quantity)
        } else {
            generateSpaces(getDefaultAssignationSpaces())
        }
    }
}
