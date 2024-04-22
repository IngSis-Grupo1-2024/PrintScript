package cli

import java.io.BufferedReader
import java.io.InputStream

class InputReader(inputStream: InputStream) {
    private val bufferReader: BufferedReader = inputStream.bufferedReader()

    fun nextLine(): String? = bufferReader.readLine()
}
