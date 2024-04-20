package ingsis.utils

import ingsis.components.statement.Modifier
import ingsis.components.statement.Type

data class Result(
    private val type: Type,
    private val modifier: Modifier? = Modifier.IMMUTABLE,
    private val value: String? = null,
) { // type may be string, integer or boolean
    constructor(type: Type, modifier: Modifier?) : this(type, modifier, "")

    fun getValue(): String? = value

    fun getType(): Type = type

    fun getModifier(): Modifier? = modifier

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Result) return false

        return this.type.getValue() == other.type.getValue() && this.value == other.value
    }

    override fun hashCode(): Int {
        return type.hashCode() * 31 + (value?.hashCode() ?: 0)
    }

    fun updateModifier(modifier: Modifier?): Result {
        return Result(type, modifier, value)
    }
}
