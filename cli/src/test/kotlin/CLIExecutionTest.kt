import cli.Cli
import cli.Version
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

class CLIExecutionTest {
    private val cliV1 = Cli(Version.VERSION_1)

    private fun readFile(fileName: String): String = File(fileName).readText(Charsets.UTF_8)

    @Test
    fun `v1 - test 01 - print line with single value`() {
        val result = cliV1.startCli(readFile("src/test/resources/printLine/SingleValue"))
        val expected =
            "\n" +
                "statement of line 0 -> PrintLine(value=SingleValue(token=type: INTEGER, value: 8)," +
                " statementType=PRINT_LINE)\n" +
                "8"
        assertEquals(expected, result)
    }

    @Test
    fun `v1 - test 02 - print line with one operator`() {
        val result = cliV1.startCli(readFile("src/test/resources/printLine/OneOperator"))
        val expected =
            "\n" +
                "statement of line 0 -> PrintLine(value=\n" +
                "\tOperator(\n" +
                "\ttoken=type: OPERATOR, value: +, \n" +
                "\tleftOperator=SingleValue(token=type: INTEGER, value: 8), \n" +
                "\trightOperator=SingleValue(token=type: INTEGER, value: 3)), statementType=PRINT_LINE)\n" +
                "11"
        assertEquals(expected, result)
    }

    @Test
    fun `v1 - test 03 - print line with two operator`() {
        val result = cliV1.startCli(readFile("src/test/resources/printLine/TwoOperator"))
        val expected =
            "\n" +
                "statement of line 0 -> PrintLine(value=\n" +
                "\tOperator(\n" +
                "\ttoken=type: OPERATOR, value: +, \n" +
                "\tleftOperator=SingleValue(token=type: INTEGER, value: 8), \n" +
                "\trightOperator=\n" +
                "\tOperator(\n" +
                "\ttoken=type: OPERATOR, value: *, \n" +
                "\tleftOperator=SingleValue(token=type: INTEGER, value: 3), \n" +
                "\trightOperator=SingleValue(token=type: INTEGER, value: 2))), statementType=PRINT_LINE)\n" +
                "14"
        assertEquals(expected, result)
    }

    @Test
    fun `v1 - test 04 - print line with three operator`() {
        val result = cliV1.startCli(readFile("src/test/resources/printLine/ThreeOperator"))
        val expected =
            "\n" +
                "statement of line 0 -> PrintLine(value=\n" +
                "\tOperator(\n" +
                "\ttoken=type: OPERATOR, value: +, \n" +
                "\tleftOperator=SingleValue(token=type: INTEGER, value: 2), \n" +
                "\trightOperator=\n" +
                "\tOperator(\n" +
                "\ttoken=type: OPERATOR, value: *, \n" +
                "\tleftOperator=SingleValue(token=type: INTEGER, value: 2), \n" +
                "\trightOperator=\n" +
                "\tOperator(\n" +
                "\ttoken=type: OPERATOR, value: +, \n" +
                "\tleftOperator=SingleValue(token=type: INTEGER, value: 2), \n" +
                "\trightOperator=SingleValue(token=type: INTEGER, value: 2)))), statementType=PRINT_LINE)\n" +
                "10"
        assertEquals(expected, result)
    }

    @Test
    fun `v1 - test 21 - empty file`() {
        val result = cliV1.startCli(readFile("src/test/resources/emptyFIle"))
        val expected = ""
        assertEquals(expected, result)
    }
}
