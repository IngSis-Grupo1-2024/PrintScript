package modules.lexer

import components.Position
import components.Token
import components.TokenType

class Lexer(override val position: Position) : LexerInterface {
    override fun tokenize(input: String): List<Token> {
        val tokenList = ArrayList<Token>()
        var isInsideString = false
        val currentString = StringBuilder()

        var currentPosition = position.copy()

        for (char in input) {

            currentString.append(char)

            when (char) {
                '"' -> {
                    isInsideString = !isInsideString
                    if (!isInsideString) {
                        currentPosition = currentPosition.copy(
                            endOffset = currentPosition.endOffset + 2,
                            endColumn = currentPosition.endColumn + 2
                        )
                    }
                }

                '+' -> {
                    when (isInsideString) {
                        true -> {
                            currentString.append(char)
                        }

                        false -> {
                            tokenList.add(Token(currentPosition, "+", TokenType.OPERATOR))
                            currentPosition = currentPosition.copy(
                                startOffset = currentPosition.endOffset + 1,
                                endOffset = currentPosition.endOffset + 1,
                                startColumn = currentPosition.endColumn + 1,
                                endColumn = currentPosition.endColumn + 1
                            )
                            currentString.clear()
                        }
                    }
                }

                '-' -> {
                    when (isInsideString) {
                        true -> {
                            currentString.append(char)
                        }

                        false -> {
                            tokenList.add(Token(currentPosition, "-", TokenType.OPERATOR))
                            currentPosition = currentPosition.copy(
                                startOffset = currentPosition.endOffset + 1,
                                startColumn = currentPosition.endColumn + 1
                            )
                        }
                    }
                }

                '*' -> {
                    when (isInsideString) {
                        true -> {
                            currentString.append(char)
                        }

                        false -> {
                            tokenList.add(Token(currentPosition, "*", TokenType.OPERATOR))
                            currentPosition = currentPosition.copy(
                                startOffset = currentPosition.endOffset + 1,
                                startColumn = currentPosition.endColumn + 1
                            )
                        }
                    }
                }

                '/' -> {
                    when (isInsideString) {
                        true -> {
                            currentString.append(char)
                        }

                        false -> {
                            tokenList.add(Token(currentPosition, "/", TokenType.OPERATOR))
                            currentPosition = currentPosition.copy(
                                startOffset = currentPosition.endOffset + 1,
                                startColumn = currentPosition.endColumn + 1
                            )
                        }
                    }
                }

                '(' -> {
                    when (isInsideString) {
                        true -> {
                            currentString.append(char)
                        }

                        false -> {
                            tokenList.add(Token(currentPosition, "(", TokenType.OPERATOR))
                            currentPosition = currentPosition.copy(
                                startOffset = currentPosition.endOffset + 1,
                                startColumn = currentPosition.endColumn + 1
                            )
                        }
                    }
                }

                ')' -> {
                    when (isInsideString) {
                        true -> {
                            currentString.append(char)
                        }

                        false -> {
                            tokenList.add(Token(currentPosition, ")", TokenType.OPERATOR))
                            currentPosition = currentPosition.copy(
                                startOffset = currentPosition.endOffset + 1,
                                startColumn = currentPosition.endColumn + 1
                            )
                        }
                    }
                }

                ';' -> {
                    when (isInsideString) {
                        true -> {
                            currentString.append(char)
                        }

                        false -> {
                            tokenList.add(
                                Token(
                                    currentPosition.copy(
                                        endOffset = currentPosition.startOffset + currentString.length - 2,
                                        endColumn = currentPosition.startColumn + currentString.length - 2
                                    ),
                                    currentString.deleteAt(currentString.length - 1).toString(),
                                    TokenType.VALUE
                                )
                            )

                            currentPosition = currentPosition.copy(
                                startOffset = currentPosition.endOffset,
                                startColumn = currentPosition.endColumn
                            )

                            tokenList.add(
                                Token(
                                    currentPosition,
                                    ";",
                                    TokenType.SEMICOLON
                                )
                            )

                            currentPosition = Position(
                                currentPosition.startOffset,
                                currentPosition.endOffset,
                                currentPosition.startLine + 1,
                                currentPosition.endLine + 1,
                                1,
                                1
                            )
                        }
                    }
                }

                ':' -> {
                    when (isInsideString) {
                        true -> {
                            currentString.append(char)
                        }

                        false -> {
                            if (currentString.isNotEmpty()) {
                                currentString.deleteAt(currentString.length - 1)
                                tokenList.add(
                                    Token(
                                        currentPosition.copy(
                                            endOffset = currentPosition.endOffset - 1,
                                            endColumn = currentPosition.endColumn - 1
                                        ),
                                        currentString.toString(),
                                        TokenType.IDENTIFIER
                                    )
                                )
                            }
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
                            currentPosition = currentPosition.copy(
                                startOffset = currentPosition.endOffset + 1,
                                endOffset = currentPosition.endOffset + 1,
                                startColumn = currentPosition.endColumn + 1,
                                endColumn = currentPosition.endColumn + 1
                            )
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
                            currentPosition = currentPosition.copy(
                                startOffset = currentPosition.endOffset + 1,
                                endOffset = currentPosition.endOffset + 1,
                                startColumn = currentPosition.endColumn + 1,
                                endColumn = currentPosition.endColumn + 1
                            )
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
                                tokenList.add(
                                    Token(
                                        currentPosition.copy(
                                            endOffset = currentPosition.endOffset - 1,
                                            endColumn = currentPosition.endColumn - 1
                                        ),
                                        value = currentString.deleteAt(currentString.length - 1).toString(),
                                        type = TokenType.VALUE
                                    )
                                )
                            }
                            currentPosition = currentPosition.copy(
                                startOffset = currentPosition.endOffset + 1,
                                endOffset = currentPosition.endOffset + 1,
                                startColumn = currentPosition.endColumn + 1,
                                endColumn = currentPosition.endColumn + 1
                            )
                            currentString.clear()
                        }
                    }
                }

                else -> {
                    when (currentString.toString()) {
                        "let" -> {
                            tokenList.add(
                                Token(
                                    currentPosition.copy(endOffset = currentPosition.endOffset),
                                    "let",
                                    TokenType.KEYWORD
                                )

                            )
                            currentPosition = currentPosition.copy(
                                startOffset = currentPosition.endOffset + 1,
                                startColumn = currentPosition.endColumn + 1
                            )
                            currentString.clear()
                        }

                        "string" -> {
                            tokenList.add(
                                Token(
                                    currentPosition.copy(endOffset = currentPosition.endOffset),
                                    "string",
                                    TokenType.TYPE
                                )
                            )
                            currentPosition = currentPosition.copy(
                                startOffset = currentPosition.endOffset + 1,
                                startColumn = currentPosition.endColumn + 1
                            )
                            currentString.clear()
                        }

                        "number" -> {
                            tokenList.add(
                                Token(
                                    currentPosition.copy(endOffset = currentPosition.endOffset),
                                    "number",
                                    TokenType.TYPE
                                )
                            )
                            currentPosition = currentPosition.copy(
                                startOffset = currentPosition.endOffset + 1,
                                startColumn = currentPosition.endColumn + 1
                            )
                            currentString.clear()
                        }

                        "println" -> {
                            tokenList.add(
                                Token(
                                    currentPosition.copy(endOffset = currentPosition.endOffset),
                                    "println",
                                    TokenType.KEYWORD
                                )
                            )
                            currentPosition = currentPosition.copy(
                                startOffset = currentPosition.endOffset + 1,
                                startColumn = currentPosition.endColumn + 1
                            )
                            currentString.clear()
                        }
                    }
                    currentPosition = currentPosition.copy(
                        endOffset = currentPosition.endOffset + 1,
                        endColumn = currentPosition.endColumn + 1
                    )
                }
            }
        }

        return tokenList
    }

    override fun tokenize(input: String, line: Int, column: Int): List<Token> {
        TODO("Not yet implemented")
    }

    override fun tokenize(input: String, line: Int, column: Int, offset: Int): List<Token> {
        TODO("Not yet implemented")
    }
}