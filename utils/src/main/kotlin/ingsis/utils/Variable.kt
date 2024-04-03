package ingsis.utils

import components.TokenType


data class Variable(private val type: TokenType, private val value:String){

    constructor(type: TokenType) : this(type, "")

    fun getValue(): String = value
    fun getType(): TokenType = type
}