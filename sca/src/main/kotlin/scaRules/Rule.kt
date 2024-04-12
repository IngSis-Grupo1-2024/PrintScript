package scaRules

import components.statement.Statement

interface Rule {
    fun validate(statement: Statement): Boolean
}
