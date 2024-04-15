package ingsis.sca.scan.function.arguments

import ingsis.components.Position
import ingsis.components.TokenType
import ingsis.components.statement.*
import ingsis.components.statement.Function
import ingsis.sca.defaultConfig.getDefaultBooleanValue
import ingsis.sca.result.InvalidResult
import ingsis.sca.result.Result
import ingsis.sca.result.ValidResult
import ingsis.utils.ReadScaRulesFile

fun checkFunctionArguments(
    statement: Statement,
    jsonReader: ReadScaRulesFile,
    literalsAllowed: ArrayList<TokenType>,
): Result {
    return when (statement) {
        is Function -> handleFunction(statement, jsonReader.getFunctionRuleMap(), literalsAllowed)
        is PrintLine -> handlePrintLine(statement, jsonReader.getPrintLnRuleMap(), literalsAllowed)
        else -> InvalidResult(Position(), "no type allowed")
    }
}

private fun handleFunction(
    function: Function,
    ruleMap: Map<String, Boolean>,
    literalsAllowed: ArrayList<TokenType>,
): Result {
    val value = function.getValue()
    return when (value) {
        is SingleValue -> handleSingleValue(value, ruleMap, literalsAllowed, function)
        is Operator -> handleType("expression", ruleMap, value, function)
        else -> InvalidResult(function.getToken().getPosition(), "${function.getValue()} with unknown value type")
    }
}

private fun handlePrintLine(
    printLine: PrintLine,
    ruleMap: Map<String, Boolean>,
    literalsAllowed: ArrayList<TokenType>,
): Result {
    val value = printLine.getValue()
    return when (value) {
        is SingleValue -> handleSingleValue(value, ruleMap, literalsAllowed, printLine)
        is Operator -> handleType("expression", ruleMap, value, printLine)
        else -> InvalidResult(printLine.getPosition(), "${printLine.getValue()} with unknown value type")
    }
}

private fun handleSingleValue(
    value: SingleValue,
    ruleMap: Map<String, Boolean>,
    literalsAllowed: ArrayList<TokenType>,
    statement: Statement,
): Result {
    val tokenType = value.getToken().getType()
    return if (tokenType == TokenType.IDENTIFIER) {
        handleType(tokenType.toString(), ruleMap, value, statement)
    } else if (tokenType in literalsAllowed && isInMap("literal", ruleMap)) {
        ValidResult()
    } else {
        InvalidResult(value.getToken().getPosition(), "${statement.getStatementType()} with $tokenType is not allowed")
    }
}

private fun handleType(
    type: String,
    ruleMap: Map<String, Boolean>,
    value: Value,
    statement: Statement,
): Result {
    return if (isInMap(type.lowercase(), ruleMap)) {
        ValidResult()
    } else {
        InvalidResult(value.getToken().getPosition(), "${statement.getStatementType()} with $type is not allowed")
    }
}

private fun isInMap(
    string: String,
    ruleMap: Map<String, Boolean>,
): Boolean {
    return if (ruleMap.containsKey(string)) {
        ruleMap[string]!!
    } else {
        getDefaultBooleanValue(string)
    }
}
