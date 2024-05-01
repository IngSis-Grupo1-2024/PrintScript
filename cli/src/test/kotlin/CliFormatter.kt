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

class CliFormatter {
    @Test
    fun testFormatWithInputStream() {
        val data = data()
        for ((version, fileName) in data) {
            val testDirectory = "src/test/resources/formatter/$version/$fileName/"
            val srcFile = File(testDirectory + "main.ps")
            val expectedOutput: List<String> = readLines(testDirectory + "expectedOutput.txt")
            val input: List<String> = readLinesIfExists(testDirectory + "input.txt").orElse(emptyList())
            val rulePath: String = getRulePath(testDirectory + "rulePath.txt")

            var errorCollector = ErrorCollector()
            val inputProvider = QueueInputProvider(Queues.toQueue(input))
            val printCollector = PrintCollector()

            val fileInputStream = FileInputStream(srcFile)
            val formatterCli = createFormatter(version, inputProvider, printCollector)

            val pair = format(formatterCli, errorCollector, fileInputStream, rulePath)
            errorCollector = pair.first
            val formatOutput = pair.second

            assertThat(errorCollector.errors, CoreMatchers.`is`(emptyList<Any>()))
            assertThat(formatOutput, CoreMatchers.`is`(expectedOutput))
        }
    }

    @Test
    fun testFormatWithOutputFile() {
        val data = data()
        for ((version, fileName) in data) {
            val testDirectory = "src/test/resources/formatter/$version/$fileName/"

            val srcFile = Path(testDirectory + "main.ps")
            val expectedOutput: List<String> = readLines(testDirectory + "expectedOutput.txt")
            val actualOutputFile = Path(testDirectory + "actualOutput.txt")
            val input: List<String> = readLinesIfExists(testDirectory + "input.txt").orElse(emptyList())
            val rulePath: String = getRulePath(testDirectory + "rulePath.txt")

            var errorCollector = ErrorCollector()
            val inputProvider = QueueInputProvider(Queues.toQueue(input))
            val printCollector = PrintCollector()

            val formatterCli = createFormatter(version, inputProvider, printCollector)

            errorCollector = formatInFile(formatterCli, errorCollector, srcFile, actualOutputFile, rulePath)

            val actualOutput = readLinesIfExists(testDirectory + "actualOutput.txt").orElse(emptyList())

            assertThat(errorCollector.errors, CoreMatchers.`is`(emptyList<Any>()))
            assertThat(actualOutput, CoreMatchers.`is`(expectedOutput))
        }
    }

    private fun createFormatter(
        version: String,
        inputProvider: QueueInputProvider,
        printCollector: PrintCollector,
    ): FormatterCli {
        val v = if (version == "1/0") Version.VERSION_1 else Version.VERSION_2
        return FormatterCli(printCollector, v, inputProvider)
    }

    private fun format(
        formatterCli: FormatterCli,
        errorCollector: ErrorCollector,
        fileInputStream: FileInputStream,
        rulePath: String,
    ): Pair<ErrorCollector, List<String>> {
        var inputStream: InputStream = ByteArrayInputStream("".toByteArray())
        try {
            inputStream = formatterCli.formatInputStream(rulePath, fileInputStream)
        } catch (e: Exception) {
            errorCollector.reportError(e.localizedMessage)
        } catch (e: Error) {
            errorCollector.reportError(e.localizedMessage)
        }
        return Pair(errorCollector, getString(inputStream))
    }

    private fun formatInFile(
        formatterCli: FormatterCli,
        errorCollector: ErrorCollector,
        fileInput: Path,
        fileOutput: Path,
        rulePath: String,
    ): ErrorCollector {
        try {
            formatterCli.formatFileResultInOutput(rulePath, fileInput, fileOutput)
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
//            Pair("1.0", "arithmetic-operations"),
//            Pair("1.0", "arithmetic-operations-decimal"),
//            Pair("1.0", "simple-declare-assign"),
//            Pair("1.0", "string-and-number-concat"),
//            Pair("1.1", "if-statement-true"),
//            Pair("1.1", "if-statement-false"),
//            Pair("1.1", "else-statement-true"),
//            Pair("1.1", "else-statement-false"),
//            Pair("1.1", "read-input"),
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
            val s = Scanner(file)
            val list = ArrayList<String>()
            while (s.hasNextLine()) {
                list.add(s.nextLine())
            }
            s.close()
            return Optional.of(list)
        }

        return Optional.empty()
    }

    private fun getRulePath(filePath: String): String {
        val file = File(filePath)
        return if (file.exists()) {
            filePath
        } else {
            "../formatter/src/main/kotlin/ingsis/formatter/rules/rules.json"
        }
    }
}
