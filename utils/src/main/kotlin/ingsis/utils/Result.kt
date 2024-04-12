package ingsis.utils

import components.statement.Type

data class Result(private val type: Type, private val value: String? = null) { // type may be string, integer or boolean
    constructor(type: Type) : this(type, "")

    fun getValue(): String? = value

    fun getType(): Type = type

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Result) return false

        val otherObject = other as Result

        return this.type.getValue() == otherObject.type.getValue() && this.value == otherObject.value
    }

    override fun hashCode(): Int {
        return type.hashCode() * 31 + (value?.hashCode() ?: 0)
    }
}
