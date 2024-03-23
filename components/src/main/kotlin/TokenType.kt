package components

enum class TokenType {

    TYPE,
    SEMICOLON,
    ASSIGNATION, // for the =
    DECLARATION, // for the :
    OPERATOR,
    KEYWORD,
    IDENTIFIER, // in 'let x', x would be the identifiers
    COMMENT,
    VALUE,
    FUNCTION,
    PARENTHESIS,
    STRING,
    INTEGER

}