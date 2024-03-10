package components

class ComparatorTokenType : Comparator<TokenType> {
    // it returns 0 if they are equals
        // for example in ASSIGNATION, you have to compare the values
    // it returns 1 if o1 > o2
    // it returns -1 if o1 < o2
    override fun compare(o1: TokenType?, o2: TokenType?): Int {
        if(o1 == o2) return 0
        else if (o1 == TokenType.SEMICOLON) return 1
        else if (o1 == TokenType.LET_KEYWORD) return checkSemicolon(o2)
        else if (o1 == TokenType.ASSIGNATION) return checkSemicolonAndLet(o2)
        return -1
    }

    private fun checkSemicolonAndLet(o2: TokenType?): Int {
        if(checkSemicolon(o2) == -1) return -1
        return if (o2 == TokenType.LET_KEYWORD) -1 else 1
    }

    private fun checkSemicolon(o2: TokenType?) : Int {
        return if (o2 == TokenType.SEMICOLON) -1 else 1
    }
}