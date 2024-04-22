package util

import ingsis.utils.OutputEmitter

class PrintOutputEmitterTests : OutputEmitter {
    override fun print(string: String) {
        println(string)
    }
}
