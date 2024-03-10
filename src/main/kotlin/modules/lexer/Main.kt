package modules.lexer

import components.Position
import components.Token

fun main(args: Array<String>) {
    val lexer1: Lexer = Lexer(Position(1, 1,1, 1, 1, 1))
    val array1: List<Token> = lexer1.tokenize("let a: string = \"hola\";")
    for (i in array1) {
        println(i.type)
        println(i.value)
        println(i.position)
        println()
    }

//    val lexer2: Lexer = Lexer(Position(0, 0, 0, 0))
//    val array2: List<Token> = lexer2.tokenize("let a: string = 3;")
//    for (i in array2) {
//        println(i.type)
//        println(i.value)
//        println()
//    }


}