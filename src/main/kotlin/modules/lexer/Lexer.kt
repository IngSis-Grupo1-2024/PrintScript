package modules.lexer

import components.Position
import components.Token
import components.TokenType

class Lexer(override val position: Position) : LexerInterface {


    override fun tokenize(input: String): List<Token> {
        val tokenList = ArrayList<Token>()
        var isInsideString = false
        val currentString = StringBuilder()

        var currentOffset = position.startOffset

        for (char in input) {

            currentOffset++

            when (char) {
                '"' -> {
                    isInsideString = !isInsideString
                    currentString.append(char)
                    if (!isInsideString) {
                        tokenList.add(Token(position.copy(endOffset = currentOffset + currentString.length), currentString.toString(), TokenType.VALUE))
                        currentString.clear()
                    }
                }

                '+' -> {
                    when (isInsideString) {
                        true -> {
                            currentString.append(char)
                        }

                        false -> {
                            tokenList.add(Token(position.copy(endOffset = currentOffset), "+", TokenType.OPERATOR))
                        }
                    }
                }

                '-' -> {
                    when (isInsideString) {
                        true -> {
                            currentString.append(char)
                        }

                        false -> {
                            tokenList.add(Token(position.copy(endOffset = currentOffset), "-", TokenType.OPERATOR))
                        }
                    }
                }

                '*' -> {
                    when (isInsideString) {
                        true -> {
                            currentString.append(char)
                        }

                        false -> {
                            tokenList.add(Token(position.copy(endOffset = currentOffset), "*", TokenType.OPERATOR))
                        }
                    }
                }

                '/' -> {
                    when (isInsideString) {
                        true -> {
                            currentString.append(char)
                        }

                        false -> {
                            tokenList.add(Token(position.copy(endOffset = currentOffset), "/", TokenType.OPERATOR))
                        }
                    }
                }

                '(' -> {
                    when (isInsideString) {
                        true -> {
                            currentString.append(char)
                        }

                        false -> {
                            tokenList.add(Token(position.copy(endOffset = currentOffset), "(", TokenType.OPERATOR))
                        }
                    }
                }

                ')' -> {
                    when (isInsideString) {
                        true -> {
                            currentString.append(char)
                        }

                        false -> {
                            tokenList.add(Token(position.copy(endOffset = currentOffset), ")", TokenType.OPERATOR))
                        }
                    }
                }

                ';' -> {
                    when (isInsideString) {
                        true -> {
                            currentString.append(char)
                        }

                        false -> {
                            if (currentString.isNotEmpty()) {
                                tokenList.add(
                                    Token(
                                        position.copy(endOffset = currentOffset + currentString.length),
                                        currentString.toString(),
                                        TokenType.VALUE
                                    )
                                )
                            }
                            tokenList.add(Token(position.copy(startOffset = currentOffset, endOffset = currentOffset), ";", TokenType.SEMICOLON))
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
                                tokenList.add(
                                    Token(
                                        position.copy(endOffset = currentOffset + currentString.length),
                                        currentString.toString(),
                                        TokenType.IDENTIFIER
                                    )
                                )
                            }
                            tokenList.add(Token(position.copy(endOffset = currentOffset), ":", TokenType.ASSIGNATION))
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
                            tokenList.add(Token(position.copy(endOffset = currentOffset), "=", TokenType.ASSIGNATION))
                        }
                    }
                }

                ' ' -> {
                    when (isInsideString) {
                        true -> {
                            currentString.append(char)
                        }

                        false -> {
                            when (currentString.toString()) {
                                "let" -> tokenList.add(Token(position.copy(endOffset = currentOffset + 3), "let", TokenType.KEYWORD))
                                "string" -> tokenList.add(Token(position.copy(endOffset = currentOffset + 6), "string", TokenType.TYPE))
                                "number" -> tokenList.add(Token(position.copy(endOffset = currentOffset + 6), "number", TokenType.TYPE))
                                "println" -> tokenList.add(Token(position.copy(endOffset = currentOffset + 7), "println", TokenType.KEYWORD))
                                else ->
                                    if (currentString.isNotEmpty()) tokenList.add(
                                        Token(
                                            position.copy(endOffset = currentOffset + currentString.length),
                                            currentString.toString(),
                                            TokenType.IDENTIFIER
                                        )
                                    )
                            }
                            currentString.clear()

                        }
                    }
                }

                else -> {
                    currentString.append(char)
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
