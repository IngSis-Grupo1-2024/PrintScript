import java.io.File

class CodeReader {

    companion object {
        fun readFile(fileName: String) : String{
            return File(fileName)
                .inputStream()
                .bufferedReader()
                .use { it.readText() }
        }
    }
}