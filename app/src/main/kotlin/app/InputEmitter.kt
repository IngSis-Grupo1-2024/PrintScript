package app

import ingsis.interpreter.interpretStatement.Input
import java.util.*

class InputEmitter : Input {
    override fun readInput(string: String): String {
        val scanner = Scanner(System.`in`)
        return scanner.nextLine()
    }
}
