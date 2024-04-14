// package ingsis.utils
//
// import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
// import com.fasterxml.jackson.module.kotlin.readValue
// import java.io.File
//

class TransformRulesInMap {
// data class FormatterRule(
//    val on: Boolean,
//    val quantity: Int,
// )
//
// //I made a map in case that we want to add new types.
// data class PrintLnRule(
//    val types: Map<String, Boolean>,
// )
//
// data class IdentifierFormat(
//    val type:String,
// )
//
// fun readFormatterRulesAndStackMap(jsonPath: String): Map<String, FormatterRule> {
//    val objectMapper = jacksonObjectMapper()
//    val formatterRuleMap: Map<String, Map<String, Any>> = objectMapper.readValue(File(jsonPath).readText())
//    val map = HashMap<String, FormatterRule>()
//
//    for ((ruleName, ruleProps) in formatterRuleMap) {
//        val on = ruleProps["on"] as Boolean
//        val quantity = ruleProps["quantity"] as Int
//        map[ruleName] = FormatterRule(on, quantity)
//    }
//    return map
// }
//
//
// fun readSCARulesAndStackMap(jsonPath: String):Map<String, FormatterRule> {
//    val objectMapper = jacksonObjectMapper()
//    val scaRuleMap: Map<String, Map<String, Any>> = objectMapper.readValue(File(jsonPath).readText())
//
//    for ((ruleName, ruleProps) in scaRuleMap) {
//        if (ruleName == "println"){
//            handlePrintln(ruleProps)
//        }
//        val on = ruleProps["on"] as Boolean
//        val quantity = ruleProps["quantity"] as Int
//        map[ruleName] = FormatterRule(on, quantity)
//    }
//    return map
// }
//
// fun handlePrintln(printLnOptions: Map<String, Any>){
//    val printLnMap = HashMap<String,Boolean>()
//    for (value in printLnOptions.keys){
//        printLnMap[value] = printLnOptions.getValue(value) as Boolean
//    }
//
//
// }
//
//
//
//
//
//
//
}
