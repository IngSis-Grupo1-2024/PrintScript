package ingsis.utils

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File

class ReadScaRulesFile() {
    private var printLnRuleMap = HashMap<String, Boolean>()
    private var readInputRuleMap = HashMap<String, Boolean>()
    private var identifierFormat = String()

    fun readSCARulesAndStackMap(jsonPath: String) {
        val objectMapper = jacksonObjectMapper()
        val scaRuleMap: Map<String, Map<String, Any>> = objectMapper.readValue(File(jsonPath).readText())
        for ((ruleName, ruleProps) in scaRuleMap) {
            if (ruleName == "println") {
                handlePrintln(ruleProps)
            } else if (ruleName == "identifier_format") {
                handleIdentifierFormat(ruleProps)
            } else if (ruleName == "readinput") {
                handleReadInput(ruleProps)
            }
        }
    }

    private fun handleIdentifierFormat(identifierProps: Map<String, Any>) {
        identifierFormat =
            if (identifierProps.containsKey("format")) {
                identifierProps["format"] as String
            } else {
                ""
            }
    }

    private fun handlePrintln(printLnOptions: Map<String, Any>) {
        val printLnMap = HashMap<String, Boolean>()
        for (value in printLnOptions.keys) {
            printLnMap[value] = printLnOptions.getValue(value) as Boolean
        }
        printLnRuleMap = printLnMap
    }

    private fun handleReadInput(readInputOptions: Map<String, Any>) {
        val readInputMap = HashMap<String, Boolean>()
        for (value in readInputOptions.keys) {
            readInputMap[value] = readInputOptions.getValue(value) as Boolean
        }
        readInputRuleMap = readInputMap
    }

    fun getFunctionRuleMap(): HashMap<String, Boolean> {
        return readInputRuleMap
    }

    fun getPrintLnRuleMap(): HashMap<String, Boolean> {
        return printLnRuleMap
    }

    fun getIdentifierFormat(): String {
        return identifierFormat
    }
}
