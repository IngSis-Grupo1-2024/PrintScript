package app

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands

class App : CliktCommand() {
    override fun run() {
    }
}

fun main(args: Array<String>) {
    App()
        .subcommands(Validate(), Execution(), Analyzer(), Format())
        .main(args)
}