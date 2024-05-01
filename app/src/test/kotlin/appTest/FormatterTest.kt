package appTest

import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import util.FormatKT
import java.io.File
import java.io.FileNotFoundException
import java.util.*

class FormatterTest {
    private val formatter = FormatKT()

    @Test
    fun `v1 - test 01 - print line with one operator`() {
        val data = data()
        for ((version, direc) in data) {
            val versionCommand = getVersion(version)
            val testDirectory = "src/test/resources/formatter/$version/$direc/"
            val srcFile = testDirectory + "main.ps"
            val expectedOutput: List<String> = readLines(testDirectory + "expectedOutput.txt")
            val actualOutputFile = testDirectory + "actualOutput.txt"
            val rulePath: String = getRulePath(testDirectory + "rulePath.txt")

            formatter.format("format", srcFile, versionCommand, actualOutputFile, rulePath)

            val actualOutput = readLinesIfExists(testDirectory + "actualOutput.txt").orElse(emptyList())

            assertThat(actualOutput, CoreMatchers.`is`(expectedOutput))
        }
    }

    private fun getVersion(version: String): String {
        return if (version == "1.0") {
            "v1"
        } else {
            "v2"
        }
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

    private fun getRulePath(filePath: String): String {
        val file = File(filePath)
        return if (file.exists()) {
            filePath
        } else {
            "../formatter/src/main/kotlin/ingsis/formatter/rules/rules.json"
        }
    }
}
