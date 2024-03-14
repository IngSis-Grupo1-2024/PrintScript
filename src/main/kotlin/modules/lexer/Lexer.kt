package modules.lexer

import components.Position
import components.Token
import components.TokenType

class Lexer(override val position: Position) : LexerInterface {

    private val essentialStr = mapOf(
        "let" to TokenType.KEYWORD,
        "number" to TokenType.TYPE,
        "string" to TokenType.TYPE,
        "println" to TokenType.KEYWORD
    )

    private val operators = listOf('+', '-', '/', '*', '(', ')')

    override fun tokenize(input: String): List<Token> {
        var tokenList = ArrayList<Token>()
        var isInsideString = false
        val currentString = StringBuilder()
        var isBeforeEqual = true

        var currentPosition = position.copy()

        for (char in input) {

            currentString.append(char)

            when (char) {
                '"' -> {
                    isInsideString = !isInsideString
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
                            tokenList.add(Token(currentPosition, currentString.toString(), TokenType.OPERATOR))
                            currentPosition = previousEndToNewStart(currentPosition, 1)
                            currentPosition = changeEndOffsetAndColumn(currentPosition, 1)
                            currentString.clear()
                        }
                    }
                }

                ';' -> {
                    when (isInsideString) {
                        true -> {
                            currentString.append(char)
                        }

                        false -> {
                            val tempString = currentString.deleteAt(currentString.length - 1)
                            if (tempString.toString() != StringBuilder("").toString()) {
                                tokenList.add(
                                    Token(
                                        currentPosition.copy(
                                            endOffset = currentPosition.startOffset + currentString.length - 1,
                                            endColumn = currentPosition.startColumn + currentString.length - 1
                                        ),
                                        tempString.toString(),
                                        TokenType.VALUE
                                    )
                                )
                            }

                            currentPosition = previousEndToNewStart(currentPosition, 0)

                            tokenList.add(
                                Token(
                                    currentPosition,
                                    ";",
                                    TokenType.SEMICOLON
                                )
                            )

                            currentPosition = moveToNewLine(currentPosition)
                        }
                    }
                }

                ':' -> {
                    when (isInsideString) {
                        true -> {
                            currentString.append(char)
                        }

                        false -> {
                            if (currentString.isNotEmpty() && currentString.length >= 2) {
                                currentString.deleteAt(currentString.length - 1)
                                tokenList.add(
                                    Token(
                                        changeEndOffsetAndColumn(currentPosition, -1),
                                        currentString.toString(),
                                        TokenType.IDENTIFIER
                                    )
                                )
                            }
                            if (currentString.toString() == ":") {
                                tokenList.add(
                                    Token(
                                        currentPosition,
                                        ":",
                                        TokenType.ASSIGNATION
                                    )
                                )
                            } else {
                                tokenList.add(
                                    Token(
                                        currentPosition.copy(
                                            startOffset = currentPosition.startOffset + currentString.length,
                                            startColumn = currentPosition.startColumn + currentString.length
                                        ),
                                        ":",
                                        TokenType.ASSIGNATION
                                    )
                                )
                            }
                            currentPosition = previousEndToNewStart(currentPosition, 1)
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
                            currentPosition = previousEndToNewStart(currentPosition, 1)
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
                                            type = TokenType.IDENTIFIER
                                        )
                                    )
                                } else {
                                    tokenList.add(
                                        Token(
                                            changeEndOffsetAndColumn(currentPosition, -1),
                                            value = currentString.deleteAt(currentString.length - 1).toString(),
                                            type = TokenType.VALUE
                                        )
                                    )
                                }
                            }
                            currentPosition = previousEndToNewStart(currentPosition, 1)
                            currentPosition = changeEndOffsetAndColumn(currentPosition, 1)
                            currentString.clear()
                        }
                    }
                }

                else -> {
                    when (currentString.toString()) {
                        in essentialStr -> {
                            tokenList = addToken(tokenList, currentPosition, currentString)
                            currentPosition = previousEndToNewStart(currentPosition, 1)
                            currentString.clear()
                        }
                    }
                    currentPosition = changeEndOffsetAndColumn(currentPosition, 1)
                }
            }
        }
        return tokenList
    }

    private fun moveToNewLine(currentPosition: Position): Position {
        var currentPosition1 = currentPosition
        currentPosition1 = Position(
            currentPosition1.startOffset,
            currentPosition1.endOffset,
            currentPosition1.startLine + 1,
            currentPosition1.endLine + 1,
            1,
            1
        )
        return currentPosition1
    }

    private fun changeEndOffsetAndColumn(currentPosition: Position, value: Int) = currentPosition.copy(
        endOffset = currentPosition.endOffset + value,
        endColumn = currentPosition.endColumn + value
    )

    private fun previousEndToNewStart(currentPosition: Position, value: Int) = currentPosition.copy(
        startOffset = currentPosition.endOffset + value,
        startColumn = currentPosition.endColumn + value
    )

    private fun addToken(
        tokenList: ArrayList<Token>,
        currentPosition: Position,
        currentString: StringBuilder
    ): ArrayList<Token> {
        tokenList.add(
            Token(
                currentPosition.copy(endOffset = currentPosition.endOffset),
                currentString.toString(),
                essentialStr.get(currentString.toString())!!
            )
        )
        return tokenList
    }


}