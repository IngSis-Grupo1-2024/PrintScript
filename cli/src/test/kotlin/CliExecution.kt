package cli

import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import util.ErrorCollector
import util.PrintCollector
import util.QueueInputProvider
import util.Queues
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.util.*

class CliExecution {
    @Test
    fun testExecution() {
        val data = data()
        for ((version, fileName) in data) {
            val testDirectory = "src/test/resources/print-statement/$version/$fileName/"
            val srcFile = File(testDirectory + "main.ps")
            val expectedOutput: List<String> = readLines(testDirectory + "expectedOutput.txt")
            val input: List<String> = readLinesIfExists(testDirectory + "input.txt").orElse(emptyList())

            var errorCollector = ErrorCollector()
            val inputProvider = QueueInputProvider(Queues.toQueue(input))
            val printCollector = PrintCollector()

            val fileInputStream = FileInputStream(srcFile)
            val executionCli = createExecution(version, inputProvider, printCollector)

            errorCollector = execute(executionCli, errorCollector, fileInputStream)
            assertThat(errorCollector.errors, CoreMatchers.`is`(emptyList<Any>()))
            assertThat<List<String>>(printCollector.messages, CoreMatchers.`is`(expectedOutput))
        }
    }

    private fun createExecution(
        version: String,
        inputProvider: QueueInputProvider,
        printCollector: PrintCollector,
    ): ExecutionCli {
        val v = if (version == "1/0") Version.VERSION_1 else Version.VERSION_2
        return ExecutionCli(printCollector, v, inputProvider)
    }

    private fun execute(
        executionCli: ExecutionCli,
        errorCollector: ErrorCollector,
        fileInputStream: FileInputStream,
    ): ErrorCollector {
        try {
            executionCli.executeInputStream(fileInputStream)
        } catch (e: Exception) {
            errorCollector.reportError(e.localizedMessage)
        } catch (e: Error) {
            errorCollector.reportError(e.localizedMessage)
        }
        return errorCollector
    }

    private fun data(): List<Pair<String, String>> {
        return listOf(
            Pair("1.0", "arithmetic-operations"),
            Pair("1.0", "arithmetic-operations-decimal"),
            Pair("1.0", "simple-declare-assign"),
            Pair("1.0", "string-and-number-concat"),
            Pair("1.1", "if-statement-true"),
            Pair("1.1", "if-statement-false"),
            Pair("1.1", "else-statement-true"),
            Pair("1.1", "else-statement-false"),
            Pair("1.1", "read-input"),
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
}
