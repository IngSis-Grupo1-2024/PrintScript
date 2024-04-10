package utils

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File

fun generateSpaces(numSpaces: Int): String {
    return " ".repeat(numSpaces)
}

fun ruleApplies(formatterRule: FormatterRule): Boolean {
    return formatterRule.on
}

fun generateNewLines(numNewLines: Int): String {
    return "\n".repeat(numNewLines)
}

data class FormatterRule(
    val on: Boolean,
    val quantity: Int,
) {
    fun isOn(): Boolean {
        return this.on
    }
}

fun readJsonAndStackMap(jsonPath: String): Map<String, FormatterRule> {
    val objectMapper = jacksonObjectMapper()
    val formatterRuleMap: Map<String, Map<String, Any>> = objectMapper.readValue(File(jsonPath).readText())
    val map = HashMap<String, FormatterRule>()

    for ((ruleName, ruleProps) in formatterRuleMap) {
        val on = ruleProps["on"] as Boolean
        val quantity = ruleProps["quantity"] as Int
        map[ruleName] = FormatterRule(on, quantity)
    }
    return map
}
