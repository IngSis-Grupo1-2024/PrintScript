package ingsis.lexer

import components.Position
import components.Token
import components.TokenType

class Lexer(override val position: Position) : LexerInterface {

    private val essentialStr = mapOf(
        "let" to TokenType.KEYWORD,
        "number" to TokenType.TYPE,
        "string" to TokenType.TYPE,
        "println" to TokenType.FUNCTION
    )

    private val operators = listOf('+', '-', '/', '*', '(', ')')

    override fun tokenize(input: String): List<Token> {
        var tokenList = ArrayList<Token>()
        var isInsideString = false
        val currentString = StringBuilder()
        var isBeforeEqual = true
        var isString = false
//        val dictionary = mutableMapOf<String, Boolean>() This can be used to remove the isString variable and when new types like lists are added.


        var currentPosition = position.copy()

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
                                var tokenValue = TokenType.INTEGER
                                if(isString){
                                    tokenValue = TokenType.STRING
                                    isString = false
                                }
                                tokenList.add(
                                    Token(
                                        currentPosition.copy(
                                            endOffset = currentPosition.startOffset + currentString.length - 1,
                                            endColumn = currentPosition.startColumn + currentString.length - 1
                                        ),
                                        tempString.toString(),
                                        tokenValue
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
                                        TokenType.DECLARATION
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
                                        TokenType.DECLARATION
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
                                    //This has to be changed because in case it is a list is different, there can be a switch or pattern matching then
                                    var tokenValue = TokenType.INTEGER
                                    if(isString){
                                        tokenValue = TokenType.STRING
                                        isString = false
                                    }
                                        tokenList.add(
                                            Token(
                                                changeEndOffsetAndColumn(currentPosition, -1),
                                                value = currentString.deleteAt(currentString.length - 1).toString(),
                                                type = tokenValue
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
                '\n' -> {
                    currentPosition = moveToNewLine(currentPosition)
                    currentString.clear()
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
            currentPosition1.startOffset + 1,
            currentPosition1.endOffset + 1,
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