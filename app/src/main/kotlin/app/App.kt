package app

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands

class App : CliktCommand() {
    override fun run() {
        echo("Welcome to PrintScript!")
        echo("\n\n-------------------------\n\n")
    }
}
fun main(args: Array<String>) {
    App()
        .subcommands(Validate(), Execution(), Analyzer(), Format())
        .main(args)
//    test1WOoutput()
}

//fun test1WOoutput() =
//    App()
//        .subcommands(Validate(), Execution(), Analyzer(), Format())
//        .main(listOf("-f", "v1", "app/src/main/resources/test1"))
//
//fun test1Woutput() = App().main(listOf("v1", "execution", "app/src/main/resources/test1", "app/src/main/resources/resultTest1"))
//
//fun test2() = App().main(listOf("v1", "execution", "app/src/main/resources/test2"))
//
//fun test2Woutput() = App().main(listOf("validation", "app/src/main/resources/test2", "app/src/main/resources/resultTest2"))
//
//fun test3Woutput() =
//    App().main(listOf("validation", "app/src/main/resources/errorWOSemicolon", "app/src/main/resources/resultErrorWOSemicolon"))
