package modules.lexer

import components.Position
import components.Token
import components.TokenType

class Lexer(override val position: Position) : LexerInterface {

    override fun tokenize(input: String): List<Token> {
        val splitString = input.split(" ", ":")
        val tokenList = ArrayList<Token>()
        for (i in splitString){
//            if (i.contains('"')) {
//                count += 1
//                val string = i.replace('"', "")
//                tokenList.add(Token(position, string, TokenType.STRING_TYPE))
//            }
            when (i) {
                "let" -> tokenList.add(Token(position, "", TokenType.LET_KEYWORD))
                "=" -> tokenList.add(Token(position, "", TokenType.ASSIGNATION))
                "string" -> tokenList.add(Token(position, "", TokenType.STRING_TYPE))
                "number" -> tokenList.add(Token(position, "", TokenType.NUMBER_TYPE))
                "+" -> tokenList.add(Token(position, "+", TokenType.OPERATOR))
                "-" -> tokenList.add(Token(position, "-", TokenType.OPERATOR))
                "*" -> tokenList.add(Token(position, "*", TokenType.OPERATOR))
                "/" -> tokenList.add(Token(position, "/", TokenType.OPERATOR))
                "(" -> tokenList.add(Token(position, "(", TokenType.PARENTHESIS))
                ")" -> tokenList.add(Token(position, ")", TokenType.PARENTHESIS))
                "println" -> tokenList.add(Token(position, "println", TokenType.FUNCTION_KEYWORD))
                ";" -> tokenList.add(Token(position, ";", TokenType.SEMICOLON))
                else -> {
                    if (i.contains("//")) {
                        tokenList.add(Token(position, i, TokenType.COMMENT))
                    } else {
                        tokenList.add(Token(position, i, TokenType.IDENTIFIER))
                    }
                }
            }
        }
        return tokenList
    }

    override fun tokenize(input: String, start: Int, end: Int): List<Token> {
        TODO("Not yet implemented")
    }

    override fun tokenize(input: String, start: Int, end: Int, line: Int, column: Int): List<Token> {
        TODO("Not yet implemented")
    }

    override fun tokenize(input: String, start: Int, end: Int, line: Int, column: Int, offset: Int): List<Token> {
        TODO("Not yet implemented")
    }

}
