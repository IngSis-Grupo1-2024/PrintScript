package ingsis.utils

import components.TokenType
import components.statement.Type

data class Result(private val type: Type, private val value: String ? = null) { //type may be string, integer or boolean
    constructor(type: Type) : this(type, "")

    fun getValue(): String? = value

    fun getType(): Type = type
}
