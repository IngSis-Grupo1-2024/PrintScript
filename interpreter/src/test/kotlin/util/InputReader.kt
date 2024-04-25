package util

import ingsis.interpreter.interpretStatement.Input
import java.util.Scanner

class InputReader : Input {
    override fun readInput(string: String): String {
        val scanner = Scanner(System.`in`)
        return scanner.nextLine()
    }
}
