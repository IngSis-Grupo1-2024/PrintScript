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
    fun `v1 - test 05 - empty file`() {
        val result = cliV1.startCli(readFile("src/test/resources/emptyFIle"))
        val expected = ""
        assertEquals(expected, result)
    }

    @Test
    fun `v1 - test 06 - print a variable`() {
        val result = cliV1.startCli(readFile("src/test/resources/printLine/variable"))
        val expected =
            "\n" +
                "statement of line 0 -> name: x \t  value: type: INTEGER, value: 10\n" +
                "\n" +
                "statement of line 1 -> PrintLine(value=SingleValue(token=type: IDENTIFIER, value: x), statementType=PRINT_LINE)\n" +
                "10\n" +
                "statement of line 2 -> name: x \t  value: SingleValue(token=type: INTEGER, value: 8)\n" +
                "\n" +
                "statement of line 3 -> PrintLine(value=SingleValue(token=type: IDENTIFIER, value: x), statementType=PRINT_LINE)\n" +
                "8\n" +
                "statement of line 4 -> name: x \t  value: SingleValue(token=type: STRING, value: hola)\n" +
                "\n" +
                "Type mismatch\n" +
                "statement of line 5 -> PrintLine(value=SingleValue(token=type: IDENTIFIER, value: x), statementType=PRINT_LINE)\n" +
                "8"
        assertEquals(expected, result)
    }

    @Test
    fun `v1 - test 07 - sum of variables`() {
        val result = cliV1.startCli(readFile("src/test/resources/printLine/sumOfVariables"))
        val expected =
            "\n" +
                "statement of line 0 -> name: x \t  value: type: INTEGER, value: 10\n" +
                "\n" +
                "statement of line 1 -> name: y \t  value: type: INTEGER, value: 20\n" +
                "\n" +
                "statement of line 2 -> PrintLine(value=\n" +
                "\tOperator(\n" +
                "\ttoken=type: OPERATOR, value: +, \n" +
                "\tleftOperator=SingleValue(token=type: IDENTIFIER, value: x), \n" +
                "\trightOperator=SingleValue(token=type: IDENTIFIER, value: y)), statementType=PRINT_LINE)\n" +
                "30\n" +
                "statement of line 3 -> name: a \t  value: type: STRING, value: co\n" +
                "\n" +
                "statement of line 4 -> name: b \t  value: type: STRING, value: ni\n" +
                "\n" +
                "statement of line 5 -> PrintLine(value=\n" +
                "\tOperator(\n" +
                "\ttoken=type: OPERATOR, value: +, \n" +
                "\tleftOperator=SingleValue(token=type: IDENTIFIER, value: a), \n" +
                "\trightOperator=SingleValue(token=type: IDENTIFIER, value: b)), statementType=PRINT_LINE)\n" +
                "coni"
        assertEquals(expected, result)
    }

    @Test
    fun `v1 - test 08 - division of variables`() {
        val result = cliV1.startCli(readFile("src/test/resources/printLine/divisionOfVariables"))
        val expected =
            "\n" +
                "statement of line 0 -> name: x \t  value: type: INTEGER, value: 10\n" +
                "\n" +
                "statement of line 1 -> name: y \t  value: type: INTEGER, value: 20\n" +
                "\n" +
                "statement of line 2 -> PrintLine(value=\n" +
                "\tOperator(\n" +
                "\ttoken=type: OPERATOR, value: /, \n" +
                "\tleftOperator=SingleValue(token=type: IDENTIFIER, value: y), \n" +
                "\trightOperator=SingleValue(token=type: IDENTIFIER, value: x)), statementType=PRINT_LINE)\n" +
                "2\n" +
                "statement of line 3 -> name: a \t  value: type: STRING, value: co\n" +
                "\n" +
                "statement of line 4 -> name: b \t  value: type: STRING, value: ni\n" +
                "\n" +
                "statement of line 5 -> PrintLine(value=\n" +
                "\tOperator(\n" +
                "\ttoken=type: OPERATOR, value: /, \n" +
                "\tleftOperator=SingleValue(token=type: IDENTIFIER, value: a), \n" +
                "\trightOperator=SingleValue(token=type: IDENTIFIER, value: b)), statementType=PRINT_LINE)\n" +
                "\n" +
                "Can't do division using no integer types in line 7 at position 11"
        assertEquals(expected, result)
    }

    @Test
    fun `v1 - test 01 - print line with huge file`() {
        val result = cliV1.startCli(readFile("src/test/resources/hugeExecutableFile"))
        val expected =
            "\n" +
                "statement of line 0 -> name: x1 \t  value: type: INTEGER, value: 5\n" +
                "\n" +
                "statement of line 1 -> name: y1 \t  value: type: INTEGER, value: 5\n" +
                "\n" +
                "statement of line 2 -> name: y2 \t  value: type: INTEGER, value: 5\n" +
                "\n" +
                "statement of line 3 -> name: x2 \t  value: type: INTEGER, value: 5\n" +
                "\n" +
                "statement of line 4 -> PrintLine(value=\n" +
                "\tOperator(\n" +
                "\ttoken=type: OPERATOR, value: *, \n" +
                "\tleftOperator=SingleValue(token=type: IDENTIFIER, value: x1), \n" +
                "\trightOperator=SingleValue(token=type: IDENTIFIER, value: y1)), statementType=PRINT_LINE)\n" +
                "25\n" +
                "statement of line 5 -> PrintLine(value=\n" +
                "\tOperator(\n" +
                "\ttoken=type: OPERATOR, value: /, \n" +
                "\tleftOperator=SingleValue(token=type: IDENTIFIER, value: x2), \n" +
                "\trightOperator=SingleValue(token=type: IDENTIFIER, value: y2)), statementType=PRINT_LINE)\n" +
                "1\n" +
                "statement of line 6 -> name: x1 \t  value: SingleValue(token=type: INTEGER, value: 10)\n" +
                "\n" +
                "statement of line 7 -> name: y1 \t  value: SingleValue(token=type: INTEGER, value: 10)\n" +
                "\n" +
                "statement of line 8 -> name: y2 \t  value: SingleValue(token=type: INTEGER, value: 10)\n" +
                "\n" +
                "statement of line 9 -> name: x2 \t  value: SingleValue(token=type: INTEGER, value: 10)\n" +
                "\n" +
                "statement of line 10 -> PrintLine(value=\n" +
                "\tOperator(\n" +
                "\ttoken=type: OPERATOR, value: +, \n" +
                "\tleftOperator=SingleValue(token=type: IDENTIFIER, value: x1), \n" +
                "\trightOperator=SingleValue(token=type: IDENTIFIER, value: y1)), statementType=PRINT_LINE)\n" +
                "20\n" +
                "statement of line 11 -> PrintLine(value=\n" +
                "\tOperator(\n" +
                "\ttoken=type: OPERATOR, value: -, \n" +
                "\tleftOperator=SingleValue(token=type: IDENTIFIER, value: x2), \n" +
                "\trightOperator=SingleValue(token=type: IDENTIFIER, value: y2)), statementType=PRINT_LINE)\n" +
                "0\n" +
                "statement of line 12 -> name: x1 \t  value: SingleValue(token=type: INTEGER, value: 15)\n" +
                "\n" +
                "statement of line 13 -> name: y1 \t  value: SingleValue(token=type: INTEGER, value: 15)\n" +
                "\n" +
                "statement of line 14 -> name: y2 \t  value: SingleValue(token=type: INTEGER, value: 15)\n" +
                "\n" +
                "statement of line 15 -> name: x2 \t  value: SingleValue(token=type: INTEGER, value: 15)\n" +
                "\n" +
                "statement of line 16 -> PrintLine(value=\n" +
                "\tOperator(\n" +
                "\ttoken=type: OPERATOR, value: +, \n" +
                "\tleftOperator=SingleValue(token=type: IDENTIFIER, value: x1), \n" +
                "\trightOperator=SingleValue(token=type: IDENTIFIER, value: y1)), statementType=PRINT_LINE)\n" +
                "30\n" +
                "statement of line 17 -> PrintLine(value=\n" +
                "\tOperator(\n" +
                "\ttoken=type: OPERATOR, value: +, \n" +
                "\tleftOperator=SingleValue(token=type: IDENTIFIER, value: x2), \n" +
                "\trightOperator=SingleValue(token=type: IDENTIFIER, value: y2)), statementType=PRINT_LINE)\n" +
                "30"
        assertEquals(expected, result)
    }
}
