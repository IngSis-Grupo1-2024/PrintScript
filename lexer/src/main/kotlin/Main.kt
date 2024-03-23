import components.Position
import components.Token

fun main() {
//    val lexer1 = Lexer(Position(1, 1,1, 1, 1, 1))
//    val array1: List<Token> = lexer1.tokenize("let a: string = \"hola\" ;")
//    for (i in array1) {
//        println(i.type)
//        println(i.value)
//        println(i.position)
//        println()
//    }

    val lexer2 = Lexer(Position(1, 1, 1, 1,1,1))
    val array2: List<Token> = lexer2.tokenize("let a: number = 3;")
    for (i in array2) {
        println(i.getType())
        println(i.getValue())
        println(i.getPosition())
        println()
    }
}