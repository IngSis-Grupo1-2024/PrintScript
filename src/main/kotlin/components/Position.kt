package components

data class Position (val startOffset: Int = 0,
                     val endOffset: Int = 0,
                     val startLine: Int = 0,
                     val endLine: Int = 0,
                     val startColumn: Int = 0,
                     val endColumn: Int = 0) {
    override fun toString(): String {
        return "{\n\tstartOffset: $startOffset,\n\tendOffset: $endOffset,\n\tstartLine: $startLine," +
                "\n\tendLine: $endLine,\n\tstartColumn: $startColumn,\n\tendColumn: $endColumn}"
    }
}