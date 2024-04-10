package spaces_counter

import utils.FormatterRule
import utils.generateSpaces


class AssignationSpaces(
    private val ruleMap: Map<String, FormatterRule>
) {

    fun getAssignationSpaces(
    ): String {
        return if ((ruleMap["assignation"]!!.isOn())) {
            generateSpaces(ruleMap["assignation"]!!.quantity)
        } else {
            generateSpaces(1)
        }
    }

}