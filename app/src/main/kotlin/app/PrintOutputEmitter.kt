package app

import ingsis.utils.OutputEmitter

class PrintOutputEmitter : OutputEmitter {
    override fun print(string: String) {
        println(string)
    }
}
