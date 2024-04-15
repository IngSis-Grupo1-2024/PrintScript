package ingsis.utils

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File

class ReadScaRulesFile() {
    private var printLnRuleMap = HashMap<String, Boolean>()
    private var identifierFormat = String()

    constructor(printLnRuleMap: HashMap<String, Boolean>, identifierFormat: String) : this() {
        this.printLnRuleMap = printLnRuleMap
        this.identifierFormat = identifierFormat
    }

    fun readSCARulesAndStackMap(jsonPath: String) {
        val objectMapper = jacksonObjectMapper()
        val scaRuleMap: Map<String, Map<String, Any>> = objectMapper.readValue(File(jsonPath).readText())
        for ((ruleName, ruleProps) in scaRuleMap) {
            if (ruleName == "println") {
                handlePrintln(ruleProps)
            } else if (ruleName == "identifier_format") {
                handleIdentifierFormat(ruleProps)
            }
        }
    }

    private fun handleIdentifierFormat(identifierProps: Map<String, Any>) {
        identifierFormat = if (identifierProps.containsKey("format")) {
            identifierProps["format"] as String
        } else ""
    }

    private fun handlePrintln(printLnOptions: Map<String, Any>) {
        val printLnMap = HashMap<String, Boolean>()
        for (value in printLnOptions.keys) {
            printLnMap[value] = printLnOptions.getValue(value) as Boolean
        }
        printLnRuleMap = printLnMap
    }

    fun getPrintLnRuleMap(): HashMap<String, Boolean> {
        return printLnRuleMap
    }

    fun getIdentifierFormat(): String {
        return identifierFormat
    }
}
