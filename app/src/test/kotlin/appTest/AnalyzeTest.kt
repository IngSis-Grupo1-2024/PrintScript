package appTest

import cli.Version
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import util.AnalyzeKT
import util.FormatKT
import java.io.File
import java.io.FileNotFoundException
import java.util.*

class AnalyzeTest {
    private val analyze = AnalyzeKT()
    @Test
    fun `v1 - test 01 - print line with one operator`() {
        val data = data()
        for((version, direc) in data){
            val versionCommand = getVersion(version)
            val testDirectory = "src/test/resources/analyze/$version/$direc/"
            val srcFile = testDirectory + "main.ps"
            val expectedOutput: List<String> = readLines(testDirectory + "expectedOutput.txt")
            val actualOutputFile = testDirectory + "actualOutput.txt"
            val rulePath: String = getRulePath(testDirectory + "rulePath.txt")
            if(rulePath == "")
                analyze.analyzeWORules("analyzer", srcFile, versionCommand, actualOutputFile)
            else
                analyze.analyzeWRules("analyzer", srcFile, versionCommand, actualOutputFile, rulePath)

            val actualOutput = readLinesIfExists(testDirectory + "actualOutput.txt").orElse(emptyList())


            assertThat(actualOutput, CoreMatchers.`is`(expectedOutput))
        }
    }

    private fun getVersion(version: String): String {
        return if(version == "1.0") "v1"
        else "v2"
    }

    private fun data(): List<Pair<String, String>> {
        return listOf(
            Pair("1.0", "printWithExpression"),
            Pair("1.0", "printWithLiteral"),
            Pair("1.0", "writeInSnakeCase"),
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
