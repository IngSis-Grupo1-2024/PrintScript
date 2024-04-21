package ingsis.components

enum class TokenType {
    TYPE,
    ASSIGNATION, // for the =
    DECLARATION, // for the :
    KEYWORD,

    // when the lexer doesn't know what it is, then it's a SYMBOL.
    // Later on, the parser can tell if it's an identifier for example
    SYMBOL,
    IDENTIFIER, // in 'let x', x would be the identifier

    VALUE,
    FUNCTION,

    // Special characters
    PARENTHESIS, // can be either ( open or ) closed
    OPERATOR,
    DELIMITER,

    // Types
    STRING,
    INTEGER,
    DOUBLE,
    BOOLEAN,
}
