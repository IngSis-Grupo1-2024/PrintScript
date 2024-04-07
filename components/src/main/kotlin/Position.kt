package components

data class Position(
    val startOffset: Int = 1,
    val endOffset: Int = 1,
    val startLine: Int = 1,
    val endLine: Int = 1,
    val startColumn: Int = 1,
    val endColumn: Int = 1,
) {
    override fun toString(): String {
        return "{\n\tstartOffset: $startOffset,\n\tendOffset: $endOffset,\n\tstartLine: $startLine," +
            "\n\tendLine: $endLine,\n\tstartColumn: $startColumn,\n\tendColumn: $endColumn}"
    }
}
