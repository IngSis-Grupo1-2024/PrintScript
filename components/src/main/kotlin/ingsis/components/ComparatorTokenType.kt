package ingsis.components

class ComparatorTokenType : Comparator<TokenType> {
    private val leafTypes = listOf(TokenType.TYPE, TokenType.VALUE, TokenType.IDENTIFIER)
    private val rootTypes = listOf(TokenType.ASSIGNATION, TokenType.OPERATOR)

    // it returns 0 if they are equals
    // for example in ASSIGNATION, you have to compare the values
    // it returns 1 if o1 > o2
    // it returns -1 if o1 < o2
    // it returns 2 to finish the tree
    // it returns -2 if it doesn't matter (keywords)
    override fun compare(
        o1: TokenType?,
        o2: TokenType?,
    ): Int {
        return when (o1) {
            o2 -> 0
            TokenType.SEMICOLON -> 2
            TokenType.KEYWORD -> -2
            in rootTypes -> 1
            TokenType.PARENTHESIS -> -2
            TokenType.OPERATOR -> checkRoot(o2)
            in leafTypes -> checkLeaf(o2)
            else -> -1
        }
    }

    private fun checkRoot(o2: TokenType?): Int {
        return if (o2 in rootTypes) -1 else 1
    }

    private fun checkLeaf(o2: TokenType?): Int {
        return if (o2 in leafTypes) 0 else -1
    }
}
