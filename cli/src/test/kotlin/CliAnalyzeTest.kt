package cli

import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import util.ErrorCollector
import util.PrintCollector
import util.QueueInputProvider
import util.Queues
import java.io.*
import java.nio.file.Path
import java.util.*
import kotlin.io.path.Path

class CliAnalyzeTest {
    @Test
    fun testFormatWithInputStream() {
        val data = data()
        for ((version, fileName) in data) {
            val testDirectory = "src/test/resources/analyze/$version/$fileName/"
            val srcFile = File(testDirectory + "main.ps")
            val expectedOutput: List<String> = readLinesIfExists(testDirectory + "expectedOutput.txt").orElse(emptyList())
            val input: List<String> = readLinesIfExists(testDirectory + "input.txt").orElse(emptyList())
            val rulePath: String = getRulePath(testDirectory + "rulePath.txt")

            var errorCollector = ErrorCollector()
            val inputProvider = QueueInputProvider(Queues.toQueue(input))
            val printCollector = PrintCollector()

            val fileInputStream = FileInputStream(srcFile)
            val analyzeCli = createAnalyzer(version, inputProvider, printCollector)

            val pair = format(analyzeCli, errorCollector, fileInputStream, rulePath)
            errorCollector = pair.first
            val formatOutput = pair.second

            assertThat(errorCollector.errors, CoreMatchers.`is`(emptyList<Any>()))
            assertThat(formatOutput.lines(), CoreMatchers.`is`(expectedOutput))
        }
    }

    @Test
    fun testFormatWithOutputFile() {
        val data = data()
        for ((version, fileName) in data) {
            val testDirectory = "src/test/resources/analyze/$version/$fileName/"

            val srcFile = Path(testDirectory + "main.ps")
            val expectedOutput: List<String> = readLinesIfExists(testDirectory + "expectedOutput.txt").orElse(emptyList())
            val actualOutputFile = Path(testDirectory + "actualOutput.txt")
            val input: List<String> = readLinesIfExists(testDirectory + "input.txt").orElse(emptyList())
            val rulePath: String = getRulePath(testDirectory + "rulePath.txt")

            var errorCollector = ErrorCollector()
            val inputProvider = QueueInputProvider(Queues.toQueue(input))
            val printCollector = PrintCollector()

            val formatterCli = createAnalyzer(version, inputProvider, printCollector)

            errorCollector = analyzeInFile(formatterCli, errorCollector, srcFile, actualOutputFile, rulePath)

            val actualOutput = readLinesIfExists(testDirectory + "actualOutput.txt").orElse(emptyList())

            assertThat(errorCollector.errors, CoreMatchers.`is`(emptyList<Any>()))
            assertThat(actualOutput, CoreMatchers.`is`(expectedOutput))
        }
    }

    private fun createAnalyzer(
        version: String,
        inputProvider: QueueInputProvider,
        printCollector: PrintCollector,
    ): AnalyzeCli {
        val v = if (version == "1.0") Version.VERSION_1 else Version.VERSION_2
        return AnalyzeCli(printCollector, v, inputProvider)
    }

    private fun format(
        analyzeCli: AnalyzeCli,
        errorCollector: ErrorCollector,
        fileInputStream: FileInputStream,
        rulePath: String,
    ): Pair<ErrorCollector, String> {
        var string = ""
        try {
            string = analyzeCli.analyzeInputStream(rulePath, fileInputStream)
        } catch (e: Exception) {
            errorCollector.reportError(e.localizedMessage)
        } catch (e: Error) {
            errorCollector.reportError(e.localizedMessage)
        }
        return Pair(errorCollector, string)
    }

    private fun analyzeInFile(
        analyzeCli: AnalyzeCli,
        errorCollector: ErrorCollector,
        fileInput: Path,
        fileOutput: Path,
        rulePath: String,
    ): ErrorCollector {
        try {
            analyzeCli.analyzeFileInFileOutput(rulePath, fileInput, fileOutput)
        } catch (e: Exception) {
            errorCollector.reportError(e.localizedMessage)
        } catch (e: Error) {
            errorCollector.reportError(e.localizedMessage)
        }
        return errorCollector
    }

    private fun getString(inputStream: InputStream): List<String> {
        val inputReader = InputReader(inputStream)
        var line: String? = inputReader.nextLine()
        val list = mutableListOf<String>()
        while (line != null) {
            list.add(line)
            line = inputReader.nextLine()
        }
        return list.toList()
    }

    private fun data(): List<Pair<String, String>> {
        return listOf(
//            Pair("1.0", "printWithExpression"),
//            Pair("1.0", "printWithLiteral"),
//            Pair("1.0", "writeInSnakeCase"),
            Pair("1.1", "readInputWithExpression"),
        )
    }

    @Throws(FileNotFoundException::class)
    private fun readLines(filePath: String): List<String> {
        return readLinesIfExists(filePath).orElseThrow {
            FileNotFoundException(
                filePath,
            )
        }
    }

    @Throws(FileNotFoundException::class)
    private fun readLinesIfExists(filePath: String): Optional<List<String>> {
        val file = File(filePath)
        if (file.exists()) {
            val list = file.readText().lines()
//            val s = Scanner(file)
//            val list = ArrayList<String>()
//            while (s.hasNextLine()) {
//                list.add(s.nextLine())
//            }
//            s.close()
            return Optional.of(list)
        }

        return Optional.empty()
    }

    private fun getRulePath(filePath: String): String {
        val file = File(filePath)
        return if (file.exists()) {
            filePath
        } else {
            ""
        }
    }
}
