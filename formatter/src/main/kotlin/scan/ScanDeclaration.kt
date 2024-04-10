package scan

import components.statement.Declaration
import components.statement.Statement
import components.statement.StatementType
import spaces.counter.DeclarationSpaces
import utils.FormatterRule

class ScanDeclaration : ScanStatement {
    override fun canHandle(statement: Statement): Boolean {
        return statement.getStatementType() == StatementType.DECLARATION
    }

    override fun format(
        statement: Statement,
        ruleMap: Map<String, FormatterRule>,
    ): String {
        val declaration = statement as Declaration
        val keyword = declaration.getKeyword()
        val variable = declaration.getVariable()
        val type = declaration.getType()
        val declarationSpaces = DeclarationSpaces(ruleMap)
        val spacesBeforeDeclaration = declarationSpaces.getBeforeDeclarationSpaces()
        val spacesAfterDeclaration = declarationSpaces.getAfterDeclarationSpaces()
        return buildDeclarationString(
            keyword.getValue(),
            variable.getName(),
            type.getValue(),
            spacesBeforeDeclaration,
            spacesAfterDeclaration,
        )
    }

    private fun buildDeclarationString(
        keywordValue: String,
        name: String,
        type: String,
        spacesBeforeDeclaration: String,
        spacesAfterDeclaration: String,
    ): String {
        return keywordValue + " " + name + spacesBeforeDeclaration + ":" +
            spacesAfterDeclaration +
            type + ";" + "\n"
    }
}
