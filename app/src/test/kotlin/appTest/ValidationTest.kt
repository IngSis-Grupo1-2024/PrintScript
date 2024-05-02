package appTest

import ingsis.parser.error.ParserError
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import util.ValidateKT
import java.io.File
import java.io.FileNotFoundException
import java.util.*

class ValidationTest {
    private val validate = ValidateKT()

    @Test
    fun `v1 - test 01 - print line with one operator`() {
        val data = data()
        for ((version, direc) in data) {
            val versionCommand = getVersion(version)
            val testDirectory = "src/test/resources/validation/$version/$direc/"
            val srcFile = testDirectory + "main.ps"
            val actualOutputFile = testDirectory + "actualOutput.txt"
            val shouldBeValid = direc.startsWith("valid")
            if (!shouldBeValid) {
                println(srcFile)
                assertThrows<ParserError> {
                    validate.validate("validate", srcFile, versionCommand, actualOutputFile)
                }
            }
        }
    }

    private fun getVersion(version: String): String {
        return if (version == "1/0") {
            "v1"
        } else {
            "v2"
        }
    }

    private fun data(): List<Pair<String, String>> {
        return listOf(
            Pair("1/0", "invalid-const-declaration"),
            Pair("1/0", "invalid-if-statement"),
            Pair("1/0", "invalid-missing-semi-colon"),
            Pair("1/0", "valid-arithmetic-ops"),
            Pair("1/1", "valid-const-declaration"),
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
            ""
        }
    }
}
