package ingsis.formatter.extractor

import ingsis.components.Token
import ingsis.components.TokenType
import ingsis.components.statement.Operator
import ingsis.components.statement.Value

class ValueExtractor(private val value: Value) {
    fun getValue(): String {
        val stringBuilder = StringBuilder()
        inorderTraversalHelper(value, stringBuilder)
        // This line is to remove the extra space at the end, strip is deprecated
        return stringBuilder.toString().substring(0, stringBuilder.length - 1)
    }

    private fun inorderTraversalHelper(
        value: Value,
        stringBuilder: StringBuilder,
    ) {
        if (!value.isLeaf()) {
            val operator = value as Operator
            inorderTraversalHelper(operator.getLeftOperator(), stringBuilder)
            stringBuilder.append(checkIfString(operator.getToken())).append(" ")
            inorderTraversalHelper(operator.getRightOperator(), stringBuilder)
        } else {
            stringBuilder.append(checkIfString(value.getToken())).append(" ")
        }
    }

    private fun checkIfString(valueToken: Token): String {
        if (valueToken.getType() == TokenType.STRING) {
            return "'${valueToken.getValue()}'"
        }
        return valueToken.getValue()
    }
}
