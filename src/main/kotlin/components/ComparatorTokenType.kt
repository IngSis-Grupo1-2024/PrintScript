package components

class ComparatorTokenType : Comparator<TokenType> {
    private val leafTypes = listOf(TokenType.TYPE, TokenType.VALUE, TokenType.IDENTIFIER)

    // it returns 0 if they are equals
        // for example in ASSIGNATION, you have to compare the values
    // it returns 1 if o1 > o2
    // it returns -1 if o1 < o2
    override fun compare(o1: TokenType?, o2: TokenType?): Int {
        return when (o1) {
            o2 -> 0
            TokenType.SEMICOLON -> 1
            TokenType.KEYWORD -> checkSemicolon(o2)
            TokenType.ASSIGNATION -> checkSemicolonAndLet(o2)
            TokenType.TYPE -> checkLeaf(o2)
            TokenType.VALUE -> checkLeaf(o2)
            TokenType.IDENTIFIER -> checkLeaf(o2)
            else -> -1
        }
    }

    private fun checkLeaf(o2: TokenType?): Int {
        return if(o2 in leafTypes) 0 else -1
    }

    private fun checkSemicolonAndLet(o2: TokenType?): Int {
        if(checkSemicolon(o2) == -1) return -1
        return if (o2 == TokenType.KEYWORD) -1 else 1
    }

    private fun checkSemicolon(o2: TokenType?) : Int {
        return if (o2 == TokenType.SEMICOLON) -1 else 1
    }
}