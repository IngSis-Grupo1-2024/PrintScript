package ingsis.parser.scan

import ingsis.components.Position
import ingsis.components.Token
import ingsis.components.TokenType
import ingsis.components.statement.*
import ingsis.parser.error.ParserError

object PSScanAssignation {
    fun createScanAssignation(version: String): ScanAssignation {
        return when (version) {
            "VERSION_1" -> ScanAssignation(scanDeclaration(version), listOf(scanValue(version)))
            "VERSION_2" -> ScanAssignation(scanDeclaration(version), listOf(scanFunction(version), scanValue(version)))
            else -> ScanAssignation(scanDeclaration(version), listOf(scanValue(version)))
        }
    }

    private fun scanFunction(version: String) = PSScanFunction.createScanFunction(version)

    private fun scanValue(version: String) = PrintScriptScanValue.createScanValue(version)

    private fun scanDeclaration(version: String) = PSScanDeclaration.createScanDeclaration(version)
}

class ScanAssignation(private val scanDeclaration: ScanDeclaration, private val valueScanners: List<ValueScanner>) : ScanStatement {
    override fun canHandle(tokens: List<Token>): Boolean {
        if (checkIfThereIsNoDelimiter(tokens)) {
            throw ParserError("error: ';' expected  " + tokens.last().getPosition(), tokens.last())
        }

        val tokWODelimiter = tokens.subList(0, tokens.size - 1)
        return canHandleWODelimiter(tokWODelimiter)
    }

    override fun canHandleWODelimiter(tokens: List<Token>): Boolean {
        if (tokens.size < 3) return false
        val assignIndex = findAssignIndex(tokens)
        if (assignIndex == -1) return false
        if (checkFirstPartOfAssignation(tokens, assignIndex)) {
            if (emptyValue(tokens, assignIndex)) throw ParserError("error: expected value", tokens[assignIndex])
            return valueScannersCanHandle(tokens.subList(assignIndex + 1, tokens.size))
        }
        return false
    }

    private fun valueScannersCanHandle(tokens: List<Token>): Boolean {
        valueScanners.forEach {
            if (it.canHandle(tokens)) return true
        }
        return false
    }

    private fun emptyValue(
        tokens: List<Token>,
        assignIndex: Int,
    ): Boolean {
        val value = tokens.subList(assignIndex + 1, tokens.size)
        return value.isEmpty()
    }

    override fun makeAST(tokens: List<Token>): Statement {
        val assignIndex: Int = findAssignIndex(tokens)
        if (checkIfCompound(tokens, assignIndex)) return compoundAssignation(tokens, assignIndex)
        return simpleAssignation(tokens, assignIndex)
    }

    private fun compoundAssignation(
        tokens: List<Token>,
        assignIndex: Int,
    ): Statement {
        val decl: Declaration = scanDeclaration.makeAST(tokens.subList(0, assignIndex)) as Declaration
        val value: Value = makeValue(tokens.subList(assignIndex + 1, tokens.size - 1))
        if (readInput(value)) return getCompoundAssignationReadInput(tokens[assignIndex].getPosition(), decl, value as Operator)
        if (readEnv(value)) return getCompoundAssignationReadEnv(tokens[assignIndex].getPosition(), decl, value as Operator)
        return CompoundAssignation(tokens[assignIndex].getPosition(), decl, value)
    }

    private fun readEnv(value: Value): Boolean =
        value.getToken().getValue() == "readEnv" && value.getToken().getType() == TokenType.FUNCTION

    private fun getCompoundAssignationReadInput(
        position: Position,
        decl: Declaration,
        value: Operator,
    ): Statement {
        return CompoundAssignationReadInput(position, decl, value.getLeftOperator())
    }

    private fun getCompoundAssignationReadEnv(
        position: Position,
        decl: Declaration,
        value: Operator,
    ): Statement {
        return CompoundAssignationReadEnv(position, decl, value.getLeftOperator().getToken().getValue())
    }


    private fun getAssignationReadEnv(position: Position, variable: Variable, value: Operator): Statement {
        return AssignationReadEnv(position, variable, value.getLeftOperator().getToken().getValue())
    }

    private fun readInput(value: Value): Boolean =
        value.getToken().getValue() == "readInput" && value.getToken().getType() == TokenType.FUNCTION

    private fun makeValue(tokens: List<Token>): Value {
        valueScanners.forEach {
            if (it.canHandle(tokens)) {
                return it.makeValue(tokens)
            }
        }
        return SingleValue(Token(Position(), "", TokenType.STRING))
    }

    private fun simpleAssignation(
        tokens: List<Token>,
        assignIndex: Int,
    ): Statement {
        val position = tokens[assignIndex].getPosition()
        val variable = getVariable(tokens[0])
        val value: Value = makeValue(tokens.subList(assignIndex + 1, tokens.size - 1))
        if (readInput(value)) return getAssignationReadInput(position, variable, value as Operator)
        if (readEnv(value)) return getAssignationReadEnv(tokens[assignIndex].getPosition(), variable, value as Operator)
        return Assignation(
            position,
            variable,
            makeValue(tokens.subList(assignIndex + 1, tokens.size - 1)),
        )
    }

    private fun getAssignationReadInput(
        position: Position,
        variable: Variable,
        operator: Operator,
    ): Statement {
        return AssignationReadInput(position, variable, operator.getLeftOperator())
    }

    private fun checkIfCompound(
        tokens: List<Token>,
        assignIndex: Int,
    ) = tokens.subList(0, assignIndex).size != 1

    private fun getVariable(token: Token): Variable = Variable(token.getValue(), token.getPosition())

    private fun checkFirstPartOfAssignation(
        tokens: List<Token>,
        assignIndex: Int,
    ) = checkDeclaration(tokens, assignIndex) || checkIdentifier(tokens.subList(0, assignIndex))

    private fun checkDeclaration(
        tokens: List<Token>,
        assignIndex: Int,
    ) = scanDeclaration.canHandleWODelimiter(tokens.subList(0, assignIndex))

    private fun findAssignIndex(tokens: List<Token>): Int {
        for (i in tokens.indices)
            if (tokens[i].getType() == TokenType.ASSIGNATION) return i
        return -1
    }

    private fun checkIdentifier(tokens: List<Token>): Boolean {
        return if (tokens.size != 1) {
            false
        } else {
            tokens[0].getType() == TokenType.IDENTIFIER
        }
    }

    private fun checkIfThereIsNoDelimiter(tokens: List<Token>) = tokens.last().getType() != TokenType.DELIMITER
}
