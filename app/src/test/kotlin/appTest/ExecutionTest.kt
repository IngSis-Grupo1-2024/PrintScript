package appTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import util.ReadFromConsole

class ExecutionTest {
    @Test
    fun `v1 - test 01 - print line with single value`() {
        val result = ReadFromConsole.execute("src/test/resources/printLine/divisionOfVariables", "v1")
        val expected = ""
        assertEquals(expected, result)
    }

    @Test
    fun `v1 - test 02 - print line with one operator`() {
        val result = ReadFromConsole.execute("src/test/resources/printLine/OneOperator", "v1")
        val expected = "11\n"
        assertEquals(expected, result)
    }

    @Test
    fun `v1 - test 03 - print line with two operator`() {
        val result = ReadFromConsole.execute("src/test/resources/printLine/TwoOperator", "v1")
        val expected = "14\n"
        assertEquals(expected, result)
    }

    @Test
    fun `v1 - test 04 - print line with three operator`() {
        val result = ReadFromConsole.execute("src/test/resources/printLine/ThreeOperator", "v1")
        val expected = "10\n"
        assertEquals(expected, result)
    }
//
//    @Test
//    fun `v1 - test 05 - empty file`() {
//        val result = ReadFromConsole.execute("src/test/resources/emptyFIle", "v1")
//        val expected = ""
//        assertEquals(expected, result)
//    }
//
//    @Test
//    fun `v1 - test 06 - print a variable`() {
//        val result = ReadFromConsole.execute("src/test/resources/printLine/variable", "v1")
//        val expected = "8"
//        assertEquals(expected, result)
//    }
//
//    @Test
//    fun `v1 - test 07 - sum of variables`() {
//        val result = ReadFromConsole.execute("src/test/resources/printLine/sumOfVariables", "v1")
//        val expected ="30\nconi"
//        assertEquals(expected, result)
//    }
//
// ////    @Test
// ////    fun `v1 - test 08 - division of variables`() {
// ////        val result = cliV1.startCli(readFile("src/test/resources/printLine/divisionOfVariables"))
// ////        val expected = "2\n"
// ////        assertEquals(expected, result)
// ////    }
//
//    @Test
//    fun `v1 - test 01 - print line with huge file`() {
//        val result = ReadFromConsole.execute("src/test/resources/printLine/hugeExecutableFile", "v1")
//        val expected =
//            "25\n" +
//                    "1\n" +
//                    "20\n" +
//                    "0\n" +
//                    "30\n" +
//                    "30"
//        assertEquals(expected, result)
//    }
}
