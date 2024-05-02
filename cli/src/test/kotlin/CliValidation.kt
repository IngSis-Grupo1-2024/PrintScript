package cli

import org.hamcrest.CoreMatchers
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import util.ErrorCollector
import util.PrintCollector
import util.QueueInputProvider
import util.Queues
import java.io.FileInputStream
import kotlin.io.path.Path

class CliValidation {
    @Test
    fun testValidationInputStream() {
        val data = data()
        for ((version, fileName) in data) {
            val testDirectory = "src/test/resources/validation/$version/$fileName"
            var errorCollector = ErrorCollector()
            val fileInputStream = FileInputStream(testDirectory)
            val validationCli = createValidationCli(version)
            errorCollector = validate(validationCli, errorCollector, fileInputStream)
            val shouldBeValid = fileName.startsWith("valid")
            val errorMatcher = getErrorMatcherForExpectedResult(shouldBeValid)
            assertThat(errorCollector.errors, errorMatcher)
        }
    }

    private fun validate(
        validationCli: ValidationCli,
        errorCollector: ErrorCollector,
        fileInputStream: FileInputStream,
    ): ErrorCollector {
        try {
            validationCli.validate(fileInputStream)
        } catch (e: Exception) {
            errorCollector.reportError(e.localizedMessage)
        } catch (e: Error) {
            errorCollector.reportError(e.localizedMessage)
        }
        return errorCollector
    }

    private fun data(): List<Pair<String, String>> {
        return listOf(
            Pair("1/0", "invalid-const-declaration.ps"),
            Pair("1/0", "invalid-if-statement.ps"),
            Pair("1/0", "invalid-missing-semi-colon.ps"),
            Pair("1/0", "valid-arithmetic-ops.ps"),
            Pair("1/1", "valid-const-declaration.ps"),
        )
    }

    private fun createValidationCli(version: String?): ValidationCli {
        val v = if (version == "1/0") Version.VERSION_1 else Version.VERSION_2
        return ValidationCli(PrintCollector(), v, QueueInputProvider(Queues.toQueue(emptyList())))
    }

    private fun getErrorMatcherForExpectedResult(shouldBeValid: Boolean): Matcher<List<String>> {
        return if (shouldBeValid) {
            CoreMatchers.`is`(emptyList())
        } else {
            CoreMatchers.not(
                emptyList(),
            )
        }
    }

    @Test
    fun testValidationFile() {
        val data = data()
        for ((version, fileName) in data) {
            val testDirectory = "src/test/resources/validation/$version/$fileName"
            var errorCollector = ErrorCollector()
            val validationCli = createValidationCli(version)
            errorCollector = validateFile(validationCli, errorCollector, testDirectory)
            val shouldBeValid = fileName.startsWith("valid")
            print(shouldBeValid)
            val errorMatcher = getErrorMatcherForExpectedResult(shouldBeValid)
            assertThat(errorCollector.errors, errorMatcher)
        }
    }

    private fun validateFile(
        validationCli: ValidationCli,
        errorCollector: ErrorCollector,
        testDirectory: String,
    ): ErrorCollector {
        try {
            validationCli.validateFile(Path(testDirectory))
        } catch (e: Exception) {
            errorCollector.reportError(e.localizedMessage)
        } catch (e: Error) {
            errorCollector.reportError(e.localizedMessage)
        }
        return errorCollector
    }
}
