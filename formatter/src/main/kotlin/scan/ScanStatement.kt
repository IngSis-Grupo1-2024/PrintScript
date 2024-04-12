package scan

import components.statement.Statement
import utils.FormatterRule

interface ScanStatement {
    fun canHandle(statement: Statement): Boolean

    fun format(
        statement: Statement,
        ruleMap: Map<String, FormatterRule>,
    ): String
}
