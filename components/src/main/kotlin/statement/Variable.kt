package components.statement

import components.Position

class Variable(val name: String, val position: Position) {
    override fun toString(): String {
        return "\tname: $name"
    }
}
