package ingsis.formatter.scan

import ingsis.components.TokenType
import ingsis.components.statement.Declaration
import ingsis.components.statement.Statement
import ingsis.components.statement.StatementType
import ingsis.components.statement.Type
import ingsis.formatter.spacesCounter.DeclarationSpaces
import ingsis.formatter.utils.FormatterRule

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
        val spacesBeforeDeclaration = declarationSpaces.getDeclarationSpaces("beforeDeclaration")
        val spacesAfterDeclaration = declarationSpaces.getDeclarationSpaces("afterDeclaration")
        return buildDeclarationString(
            keyword.getValue(),
            variable.getName(),
            getType(type),
            spacesBeforeDeclaration,
            spacesAfterDeclaration,
        )
    }

    private fun getType(type: Type): String =
        if (type.getValue() == TokenType.INTEGER) {
            "number"
        } else {
            type.getValue().toString().lowercase()
        }

    private fun buildDeclarationString(
        keywordValue: String,
        name: String,
        type: String,
        spacesBeforeDeclaration: String,
        spacesAfterDeclaration: String,
    ): String {
        return "$keywordValue $name" +
            "$spacesBeforeDeclaration:$spacesAfterDeclaration" +
            "${type.lowercase()};\n"
    }
}
