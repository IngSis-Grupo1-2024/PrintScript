package ingsis.lexer

import components.Position
import components.Token
import components.TokenType

class Lexer(override val position: Position) : LexerInterface {
    private val essentialStr =
        mapOf(
            "let" to TokenType.KEYWORD,
            "number" to TokenType.TYPE,
            "string" to TokenType.TYPE,
            "println" to TokenType.FUNCTION,
        )

    private val operators = listOf('+', '-', '/', '*')
    private var tokenList = ArrayList<Token>()
    private var isInsideString = false
    private val currentString = StringBuilder()
    private var isBeforeEqual = true
    private var isString = false
    private var currentPosition = position.copy()

    override fun tokenize(input: String): List<Token> {
        startClassVariables()
//        val dictionary = mutableMapOf<String, Boolean>() This can be used to remove the isString variable and when new types like lists are added.

        for (char in input) {
            currentString.append(char)

            when (char) {
                '"' -> {
                    isInsideString = !isInsideString
                    isString = true
                    if (!isInsideString) {
                        currentPosition = changeEndOffsetAndColumn(currentPosition, 2)
                    }
                }

                in operators -> {
                    when (isInsideString) {
                        true -> {
                            currentString.append(char)
                        }

                        false -> {
                            addRemainValue(currentPosition)
                            tokenList.add(Token(currentPosition, char.toString(), TokenType.OPERATOR))
                            currentPosition = changeStartOffsetAndColumnWithEndValues(currentPosition, 1)
                            currentPosition = changeEndOffsetAndColumn(currentPosition, 1)
                            currentString.clear()
                        }
                    }
                }

                '(' -> {
                    when (isInsideString) {
                        true -> {
                            currentString.append(char)
                        }

                        false -> {
                            addToken(currentPosition, TokenType.PARENTHESIS, "(")
//                            chequear el tema de las positions
                            currentPosition = changeStartOffsetAndColumnWithEndValues(currentPosition, 1)
                            currentPosition = changeEndOffsetAndColumn(currentPosition, 1)
                            currentString.clear()
                        }
                    }
                }

                ')' -> {
                    when (isInsideString) {
                        true -> {
                            currentString.append(char)
                        }

                        false -> {
                            addRemainValue(currentPosition)
//                            chequear el tema de las positions
                            currentPosition = changeStartOffsetAndColumnWithEndValues(currentPosition, 0)

                            addToken(currentPosition, TokenType.PARENTHESIS, ")")
                            currentString.clear()
                        }
                    }
                }

                ';' -> {
                    addSemicolon(char)
                }

                ':' -> {
                    when (isInsideString) {
                        true -> {
                            currentString.append(char)
                        }

                        false -> {
                            if (currentString.isNotEmpty() && currentString.length >= 2) {
                                currentString.deleteAt(currentString.length - 1)
                                addToken(
                                    changeEndOffsetAndColumn(currentPosition, -1),
                                    TokenType.IDENTIFIER,
                                    currentString.toString(),
                                )
                            }
                            if (currentString.toString() == ":") {
                                addToken(
                                    currentPosition,
                                    TokenType.DECLARATION,
                                    ":",
                                )
                            } else {
                                tokenList.add(
                                    Token(
                                        currentPosition.copy(
                                            startOffset = currentPosition.startOffset + currentString.length,
                                            startColumn = currentPosition.startColumn + currentString.length,
                                        ),
                                        ":",
                                        TokenType.DECLARATION,
                                    ),
                                )
                            }
                            currentPosition = changeStartOffsetAndColumnWithEndValues(currentPosition, 1)
                            currentPosition = changeEndOffsetAndColumn(currentPosition, 1)
                            currentString.clear()
                        }
                    }
                }

                '=' -> {
                    when (isInsideString) {
                        true -> {
                            currentString.append(char)
                        }

                        false -> {
                            tokenList.add(Token(currentPosition, "=", TokenType.ASSIGNATION))
                            currentPosition = changeStartOffsetAndColumnWithEndValues(currentPosition, 1)
                            currentPosition = changeEndOffsetAndColumn(currentPosition, 1)
                            isBeforeEqual = false
                            currentString.clear()
                        }
                    }
                }

                ' ' -> {
                    when (isInsideString) {
                        true -> {
                            currentString.append(char)
                        }

                        false -> {
                            if (currentString.toString() != StringBuilder(" ").toString()) {
                                if (isBeforeEqual) {
                                    tokenList.add(
                                        Token(
                                            changeEndOffsetAndColumn(currentPosition, -1),
                                            value = currentString.deleteAt(currentString.length - 1).toString(),
                                            type = TokenType.IDENTIFIER,
                                        ),
                                    )
                                } else {
                                    // This has to be changed because in case it is a list is different, there can be a switch or pattern matching then
                                    var tokenValue = TokenType.INTEGER
                                    if (isString) {
                                        tokenValue = TokenType.STRING
                                        isString = false
                                    }
                                    tokenList.add(
                                        Token(
                                            changeEndOffsetAndColumn(currentPosition, -1),
                                            value = currentString.deleteAt(currentString.length - 1).toString(),
                                            type = tokenValue,
                                        ),
                                    )
                                }
                            }
                            currentPosition = changeStartOffsetAndColumnWithEndValues(currentPosition, 1)
                            currentPosition = changeEndOffsetAndColumn(currentPosition, 1)
                            currentString.clear()
                        }
                    }
                }
                '\n' -> {
                    currentPosition = moveToNewLine(currentPosition)
                    currentString.clear()
                }

                else -> {
                    when (currentString.toString()) {
                        in essentialStr -> {
                            addTokenWithMap(currentPosition, currentString)
                            currentPosition = changeStartOffsetAndColumnWithEndValues(currentPosition, 1)
                            currentString.clear()
                        }
                    }
                    currentPosition = changeEndOffsetAndColumn(currentPosition, 1)
                }
            }
        }
        return tokenList
    }

    private fun startClassVariables() {
        tokenList.clear()
        isInsideString = false
        currentString.clear()
        isBeforeEqual = true
        isString = false
        currentPosition = position.copy()
    }

    private fun addSemicolon(char: Char) {
        when (isInsideString) {
            true -> {
                currentString.append(char)
            }

            false -> {
                addRemainValue(currentPosition)

                currentPosition = changeStartOffsetAndColumnWithEndValues(currentPosition, 0)

                addToken(currentPosition, TokenType.SEMICOLON, ";")
                currentString.clear()
            }
        }
    }

    private fun addRemainValue(currentPosition: Position) {
        val tempString = currentString.deleteAt(currentString.length - 1)
        if (tempString.toString() != StringBuilder("").toString()) {
            val pair = getTokenAndUpdateIsString(isString)
            val tokenType = pair.first
            isString = pair.second
            addToken(
                currentPosition.copy(
                    endOffset = currentPosition.startOffset + currentString.length - 1,
                    endColumn = currentPosition.startColumn + currentString.length - 1,
                ),
                tokenType,
                tempString.toString(),
            )
        }
    }

    private fun getTokenAndUpdateIsString(isString: Boolean): Pair<TokenType, Boolean> {
        var isString1 = isString
        var tokenType = TokenType.INTEGER
        if (isString1) {
            tokenType = TokenType.STRING
            isString1 = false
        }
        return Pair(tokenType, isString1)
    }

    private fun moveToNewLine(currentPosition: Position): Position {
        var currentPosition1 = currentPosition
        currentPosition1 =
            Position(
                currentPosition1.startOffset + 1,
                currentPosition1.endOffset + 1,
                currentPosition1.startLine + 1,
                currentPosition1.endLine + 1,
                1,
                1,
            )
        return currentPosition1
    }

    private fun changeEndOffsetAndColumn(
        currentPosition: Position,
        value: Int,
    ) = currentPosition.copy(
        endOffset = currentPosition.endOffset + value,
        endColumn = currentPosition.endColumn + value,
    )

    private fun changeStartOffsetAndColumnWithEndValues(
        currentPosition: Position,
        value: Int,
    ) = currentPosition.copy(
        startOffset = currentPosition.endOffset + value,
        startColumn = currentPosition.endColumn + value,
    )

    private fun addTokenWithMap(
        currentPosition: Position,
        currentString: StringBuilder,
    ) {
        tokenList.add(
            Token(
                currentPosition.copy(endOffset = currentPosition.endOffset),
                currentString.toString(),
                essentialStr.get(currentString.toString())!!,
            ),
        )
    }

    private fun addToken(
        position: Position,
        type: TokenType,
        value: String,
    ): ArrayList<Token> {
        tokenList.add(
            Token(
                position,
                value,
                type,
            ),
        )
        return tokenList
    }
}
