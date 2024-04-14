package ingsis.lexer

import ingsis.components.Position
import ingsis.components.Token
import ingsis.components.TokenType

object PrintScriptLexer {
    fun createLexer(version: String): Lexer {
        return when (version) {
            "VERSION_1" -> version1()
            else -> version1()
        }
    }

    private fun version1() =
        Lexer(
            Position(),
            listOf(
                TokenAssignator(TokenType.KEYWORD, listOf("let")),
                TokenAssignator(TokenType.FUNCTION, listOf("println")),
                TokenAssignator(TokenType.TYPE, listOf("number", "string", "boolean")),
                TokenAssignator(TokenType.OPERATOR, listOf("+", "-", "/", "*")),
                TokenAssignator(TokenType.DELIMITER, listOf(";")),
                TokenAssignator(TokenType.PARENTHESIS, listOf("(", ")")),
                TokenAssignator(TokenType.DECLARATION, listOf(":")),
                TokenAssignator(TokenType.ASSIGNATION, listOf("=")),
            ),
        )
}

class Lexer(
    private val position: Position,
    private val tokenAssignatorList: List<TokenAssignator>,
) {
    fun tokenize(input: String): List<Token> {
        val tokens = ArrayList<Token>()
        var currentPosition = position.copy()
        var currentToken = ""
        var i = 0
        while (i < input.length) {
            val nextChar = input[i]
            if (currentToken.isEmpty() && nextChar == ' ') {
                currentPosition = updatePosition(currentPosition, ' ')
                currentPosition = previousEndToNewStart(currentPosition)
                currentToken = ""
                i++
                continue
            }
            if (currentToken.isNotBlank()) {
                if (canCreateToken(currentToken)) {
                    tokens.add(createTokenIfValid(currentToken, currentPosition))
                    currentToken = ""
                    currentPosition = previousEndToNewStart(currentPosition)
                }
                if (nextChar == ' ' && currentToken.isNotEmpty()) { // current token is a symbol
                    tokens.add(Token(currentPosition, currentToken, TokenType.SYMBOL))
                    currentPosition = updatePosition(currentPosition, ' ')
                    currentPosition = previousEndToNewStart(currentPosition)
                    currentToken = ""
                    i++
                    continue
                }
                if (nextChar == '\n' && currentToken.isNotEmpty()) {
                    tokens.add(Token(currentPosition, currentToken, TokenType.SYMBOL))
                    currentPosition = newLineUpdatePosition(currentPosition)
                    currentPosition = previousEndToNewStart(currentPosition)
                    currentToken = ""
                    i++
                    continue
                }
                if (nextChar == ' ') {
                    currentPosition = updatePosition(currentPosition, nextChar)
                    currentPosition = previousEndToNewStart(currentPosition)
                    i++
                    continue
                }
                if (canCreateToken(nextChar.toString()) && currentToken.isNotEmpty()) { // current token is a symbol
                    tokens.add(Token(currentPosition, currentToken, TokenType.SYMBOL))
                    currentPosition =
                        currentPosition.copy(
                            startOffset = currentPosition.endOffset,
                            endOffset = currentPosition.endOffset + 1,
                            startColumn = currentPosition.endColumn,
                            endColumn = currentPosition.endColumn + 1,
                        )
                    tokens.add(createTokenIfValid(nextChar.toString(), currentPosition))
                    currentPosition = previousEndToNewStart(currentPosition)
                    currentToken = ""
                    i++
                    continue
                }
            }
            if (nextChar != ' ') {
                if (nextChar == '\n') {
                    currentPosition = newLineUpdatePosition(currentPosition)
                    i++
                    continue
                }
                currentToken = currentToken.plus(nextChar)
                currentPosition = updatePosition(currentPosition, nextChar)
            }
            i++
        }
        if (currentToken.isNotBlank()) {
            tokens.add(createTokenIfValid(currentToken, currentPosition))
        }
        return tokens
    }

    private fun updatePosition(
        position: Position,
        char: Char,
    ): Position {
        val newEndOffset = position.endOffset + 1
        val newEndColumn = if (char == '\n') 1 else position.endColumn + 1
        val newEndLine = if (char == '\n') position.endLine + 1 else position.endLine
        val newStartLine = if (char == '\n') newEndLine else position.startLine
        val newStartColumn = if (char == '\n') 1 else position.startColumn
        return position.copy(
            startColumn = newStartColumn,
            startLine = newStartLine,
            endOffset = newEndOffset,
            endColumn = newEndColumn,
            endLine = newEndLine,
        )
    }

    private fun newLineUpdatePosition(position: Position): Position {
        val newEndOffset = position.endOffset
        val newEndColumn = 1
        val newEndLine = position.endLine + 1
        val newStartColumn = 1
        return position.copy(
            startColumn = newStartColumn,
            startLine = newEndLine,
            endOffset = newEndOffset,
            endColumn = newEndColumn,
            endLine = newEndLine
        )
    }

    private fun canCreateToken(string: String): Boolean {
        return tokenAssignatorList.any { it.isInsideValidatedString(string) }
    }

    private fun createTokenIfValid(
        string: String,
        position: Position,
    ): Token {
        var assignator = TokenAssignator(TokenType.TYPE, listOf())
        tokenAssignatorList.forEach {
            if (it.isInsideValidatedString(string)) {
                assignator = it
            }
        }
        return assignator.assignToken(string, position)
    }

    private fun previousEndToNewStart(currentPosition: Position): Position {
        return currentPosition.copy(startOffset = currentPosition.endOffset, startColumn = currentPosition.endColumn)
    }
}
