package ingsis.sca.scan.function.arguments

import ingsis.components.Position
import ingsis.components.TokenType
import ingsis.components.statement.*
import ingsis.sca.defaultConfig.getDefaultBooleanValue
import ingsis.sca.result.InvalidResult
import ingsis.sca.result.Result
import ingsis.sca.result.ValidResult
import ingsis.utils.ReadScaRulesFile

fun checkFunctionArguments(
    statement: Statement,
    jsonReader: ReadScaRulesFile,
    literalsAllowed: List<TokenType>,
): Result {
    return when (statement) {
        is AssignationReadInput -> handleAssignationReadInput(statement, jsonReader.getFunctionRuleMap(), literalsAllowed)
        is CompoundAssignationReadInput -> handleCompoundAssignationReadInput(statement, jsonReader.getFunctionRuleMap(), literalsAllowed)
        is PrintLine -> handlePrintLine(statement, jsonReader.getPrintLnRuleMap(), literalsAllowed)
        else -> InvalidResult(Position(), "no type allowed")
    }
}

private fun handleAssignationReadInput(
    readInput: AssignationReadInput,
    ruleMap: Map<String, Boolean>,
    literalsAllowed: List<TokenType>,
): Result {
    return when (val value = readInput.getArgument()) {
        is SingleValue -> handleSingleValue(value, ruleMap, literalsAllowed, readInput)
        is Operator -> handleType("expression", ruleMap, value, readInput)
        else -> InvalidResult(readInput.getPosition(), "${readInput.getStatementType()} with unknown value type")
    }
}

private fun handleCompoundAssignationReadInput(
    readInput: CompoundAssignationReadInput,
    ruleMap: Map<String, Boolean>,
    literalsAllowed: List<TokenType>,
): Result {
    return when (val value = readInput.getArgument()) {
        is SingleValue -> handleSingleValue(value, ruleMap, literalsAllowed, readInput)
        is Operator -> handleType("expression", ruleMap, value, readInput)
        else -> InvalidResult(readInput.getPosition(), "${readInput.getStatementType()} with unknown value type")
    }
}

private fun handlePrintLine(
    printLine: PrintLine,
    ruleMap: Map<String, Boolean>,
    literalsAllowed: List<TokenType>,
): Result {
    val value = printLine.getValue()
    return when (value) {
        is SingleValue -> handleSingleValue(value, ruleMap, literalsAllowed, printLine)
        is Operator -> handleType("expression", ruleMap, value, printLine)
        else -> InvalidResult(printLine.getPosition(), "${printLine.getStatementType()} with unknown value type")
    }
}

private fun handleSingleValue(
    value: SingleValue,
    ruleMap: Map<String, Boolean>,
    literalsAllowed: List<TokenType>,
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
    return if (ruleMap.containsKey(string) && ruleMap["on"]!!) {
        ruleMap[string]!!
    } else {
        getDefaultBooleanValue(string)
    }
}
