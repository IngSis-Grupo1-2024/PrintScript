package components

class ComparatorTokenValue : Comparator<Token> {

    // it returns 0 if they are equals
    // for example in ASSIGNATION, you have to compare the values
    // it returns 1 if o1 > o2
    // it returns -1 if o1 < o2
    override fun compare(o1: Token?, o2: Token?): Int {
        return when(o1!!.getType()){
            TokenType.ASSIGNATION -> compareAssignation(o1.getValue(), o2!!.getValue())
            TokenType.OPERATOR -> compareOperator(o1.getValue(), o2!!.getValue())
            else -> 0
        }
    }

    private fun compareOperator(value: String, value1: String): Int {
        return when(value) {
            value1 -> 0
            "+" -> checkValue(value1, "-", 1)
            "-" -> checkValue(value1, "+", 1)
            "*" -> checkValue(value1, "/", -1)
            "/" -> checkValue(value1, "*", -1)
            else -> -1
        }
    }

    private fun checkValue(value1: String, s: String, ifNot: Int): Int {
        return if (value1 == s) 0 else ifNot
    }

    private fun compareAssignation(value: String, value1: String): Int {
        return when(value){
            value1 -> 0
            "=" -> 1
            else -> -1
        }
    }


}