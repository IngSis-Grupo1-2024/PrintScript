package scan

import components.TokenType
import components.statement.*
import ingsis.utils.ReadScaRulesFile
import result.InvalidResult
import result.Result
import result.ValidResult

class ScanPrintLine(private val literalsAllowed: ArrayList<TokenType>) : ScanStatement {
    override fun canHandle(statement: Statement): Boolean {
        return statement.getStatementType() == StatementType.PRINT_LINE
    }

    override fun analyze(statement: Statement, jsonReader: ReadScaRulesFile): Result {
        statement as PrintLine
        when (statement.getValue()) {
            is SingleValue -> {
                val value = statement.getValue() as SingleValue
                return if (value.getToken().getType() == TokenType.IDENTIFIER) {
                    if (jsonReader.getPrintLnRuleMap()["identifier"]!!) {
                        ValidResult()
                    } else
                        InvalidResult(statement.getPosition(), "Println with identifier is not allowed")
                } else if (value.getToken().getType() in literalsAllowed) {
                    ValidResult()
                } else
                    InvalidResult(
                        statement.getPosition(),
                        "Println with ${value.getToken().getType()} is not allowed"
                    )
            }

            is Operator -> {
                val value = statement.getValue() as Operator
                return if (jsonReader.getPrintLnRuleMap()["expression"]!!) {
                    ValidResult()
                } else
                    InvalidResult(statement.getPosition(), "Println with expression is not allowed")
            }
        }
        return InvalidResult(statement.getPosition(), "Println with unknown value type")
    }
}
