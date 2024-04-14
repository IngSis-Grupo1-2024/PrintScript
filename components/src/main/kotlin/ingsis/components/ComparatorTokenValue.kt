package ingsis.components

class ComparatorTokenValue : Comparator<Token> {
    // it returns 0 if they are equals
    // for example in ASSIGNATION, you have to compare the values
    // it returns 1 if token > root
    // it returns -1 if token < root
    override fun compare(
        token: Token?,
        root: Token?,
    ): Int {
        return when (token!!.getType()) {
            TokenType.ASSIGNATION -> compareAssignation(token.getValue(), root!!.getValue())
            TokenType.OPERATOR -> compareOperator(token.getValue(), root!!.getValue())
            else -> 0
        }
    }

    private fun compareOperator(
        token: String,
        root: String,
    ): Int {
        return when (token) {
            root -> 0
            "+" -> checkValue(root, "-", 1)
            "-" -> checkValue(root, "+", 1)
            "*" -> checkValue(root, "/", -1)
            "/" -> checkValue(root, "*", -1)
            else -> -1
        }
    }

    private fun checkValue(
        root: String,
        s: String,
        ifNot: Int,
    ): Int {
        return if (root == s) 0 else ifNot
    }

    private fun compareAssignation(
        value: String,
        value1: String,
    ): Int {
        return when (value) {
            value1 -> 0
            "=" -> 1
            else -> -1
        }
    }
}
