package ingsis.components

object PrintScriptComparatorTokenType {
    fun createComparatorTokenType(version: String): ComparatorTokenType {
        return when (version) {
            "VERSION_1" -> ComparatorTokenType(leafTypesV1())
            "VERSION_2" -> ComparatorTokenType(leafTypesV2())
            else -> ComparatorTokenType(leafTypesV1())
        }
    }

    private fun leafTypesV1() = listOf(TokenType.TYPE, TokenType.INTEGER, TokenType.STRING, TokenType.DOUBLE, TokenType.IDENTIFIER)

    private fun leafTypesV2() = leafTypesV1() + listOf(TokenType.BOOLEAN)
}

class ComparatorTokenType(private val leafTypes: List<TokenType>) : Comparator<TokenType> {
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
            TokenType.DELIMITER -> 2
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
