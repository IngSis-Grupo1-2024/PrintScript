class CLIValidationTest {
//    private val cliV1 = Cli(Version.VERSION_1)
//
//    private fun readFile(fileName: String): String = File(fileName).readText(Charsets.UTF_8)
//
//    @Test
//    fun `v1 - test 01 - declaration of x as string`() {
//        val result = cliV1.validate(readFile("src/test/resources/declaration/String"))
//        val expected = "VALIDATION SUCCESSFUL"
//        assertEquals(expected, result)
//    }
//
//    @Test
//    fun `v1 - test 02 - declaration of x as string with an = instead of declaration`() {
//        val result = cliV1.validate(readFile("src/test/resources/declaration/WithEqualsSign"))
//        val expected =
//            "\n" +
//                "error: to declare a variable, it's expected to do " +
//                "it by 'let <name of the variable>: <type of the variable>' in position :{\n" +
//                "\tstartOffset: 1,\n" +
//                "\tendOffset: 4,\n" +
//                "\tstartLine: 1,\n" +
//                "\tendLine: 1,\n" +
//                "\tstartColumn: 1,\n" +
//                "\tendColumn: 4}"
//        assertEquals(expected, result)
//    }
//
//    @Test
//    fun `v1 - test 03 - simple assignation with single value`() {
//        val result = cliV1.validate(readFile("src/test/resources/simpleAssignation/SingleValue"))
//        val expected = "VALIDATION SUCCESSFUL"
//        assertEquals(expected, result)
//    }
//
//    @Test
//    fun `v1 - test 04 - simple assignation with single value and one operator`() {
//        val result = cliV1.validate(readFile("src/test/resources/simpleAssignation/SingleValueAndOneOperator"))
//        val expected =
//            "\nerror: wrong number of values and operators in position :{\n" +
//                "\tstartOffset: 7,\n" +
//                "\tendOffset: 8,\n" +
//                "\tstartLine: 1,\n" +
//                "\tendLine: 1,\n" +
//                "\tstartColumn: 7,\n" +
//                "\tendColumn: 8}"
//        assertEquals(expected, result)
//    }
//
//    @Test
//    fun `v1 - test 05 - simple assignation with one operator`() {
//        val result = cliV1.validate(readFile("src/test/resources/simpleAssignation/OneOperator"))
//        val expected = "VALIDATION SUCCESSFUL"
//        assertEquals(expected, result)
//    }
//
//    @Test
//    fun `v1 - test 06 - simple assignation with two operator`() {
//        val result = cliV1.validate(readFile("src/test/resources/simpleAssignation/TwoOperator"))
//        val expected = "VALIDATION SUCCESSFUL"
//        assertEquals(expected, result)
//    }
//
//    @Test
//    fun `v1 - test 07 - simple assignation with three operator`() {
//        val result = cliV1.validate(readFile("src/test/resources/simpleAssignation/ThreeOperator"))
//        val expected = "VALIDATION SUCCESSFUL"
//        assertEquals(expected, result)
//    }
//
//    @Test
//    fun `v1 - test 08 - compound assignation with single value`() {
//        val result = cliV1.validate(readFile("src/test/resources/compoundAssignation/SingleValue"))
//        val expected = "VALIDATION SUCCESSFUL"
//        assertEquals(expected, result)
//    }
//
//    @Test
//    fun `v1 - test 09 - compound assignation with single value and one operator`() {
//        val result = cliV1.validate(readFile("src/test/resources/compoundAssignation/SingleValueAndOneOperator"))
//        val expected =
//            "\n" +
//                "error: wrong number of values and operators in position :{\n" +
//                "\tstartOffset: 19,\n" +
//                "\tendOffset: 20,\n" +
//                "\tstartLine: 1,\n" +
//                "\tendLine: 1,\n" +
//                "\tstartColumn: 19,\n" +
//                "\tendColumn: 20}"
//        assertEquals(expected, result)
//    }
//
//    @Test
//    fun `v1 - test 10 - compound assignation with one operator`() {
//        val result = cliV1.validate(readFile("src/test/resources/compoundAssignation/OneOperator"))
//        val expected = "VALIDATION SUCCESSFUL"
//        assertEquals(expected, result)
//    }
//
//    @Test
//    fun `v1 - test 11 - compound assignation with two operator`() {
//        val result = cliV1.validate(readFile("src/test/resources/compoundAssignation/TwoOperator"))
//        val expected = "VALIDATION SUCCESSFUL"
//        assertEquals(expected, result)
//    }
//
//    @Test
//    fun `v1 - test 12 - compound assignation with three operator`() {
//        val result = cliV1.validate(readFile("src/test/resources/compoundAssignation/ThreeOperator"))
//        val expected = "VALIDATION SUCCESSFUL"
//        assertEquals(expected, result)
//    }
//
//    @Test
//    fun `v1 - test 13 - print line with single value`() {
//        val result = cliV1.validate(readFile("src/test/resources/printLine/SingleValue"))
//        val expected = "VALIDATION SUCCESSFUL"
//        assertEquals(expected, result)
//    }
//
//    @Test
//    fun `v1 - test 14 - print line with single value and one operator`() {
//        val result = cliV1.validate(readFile("src/test/resources/printLine/SingleValueAndOneOperator"))
//        val expected =
//            "\n" +
//                "error: wrong number of values and operators in position :{\n" +
//                "\tstartOffset: 12,\n" +
//                "\tendOffset: 13,\n" +
//                "\tstartLine: 1,\n" +
//                "\tendLine: 1,\n" +
//                "\tstartColumn: 12,\n" +
//                "\tendColumn: 13}"
//        assertEquals(expected, result)
//    }
//
//    @Test
//    fun `v1 - test 15 - print line with one operator`() {
//        val result = cliV1.validate(readFile("src/test/resources/printLine/OneOperator"))
//        val expected = "VALIDATION SUCCESSFUL"
//        assertEquals(expected, result)
//    }
//
//    @Test
//    fun `v1 - test 16 - print line with two operator`() {
//        val result = cliV1.validate(readFile("src/test/resources/printLine/TwoOperator"))
//        val expected = "VALIDATION SUCCESSFUL"
//        assertEquals(expected, result)
//    }
//
//    @Test
//    fun `v1 - test 17 - print line with three operator`() {
//        val result = cliV1.validate(readFile("src/test/resources/printLine/ThreeOperator"))
//        val expected = "VALIDATION SUCCESSFUL"
//        assertEquals(expected, result)
//    }
//
//    @Test
//    fun `v1 - test 18 - print line with three operator without last parenthesis`() {
//        val result = cliV1.validate(readFile("src/test/resources/printLine/ThreeOperatorWOLastParenthesis"))
//        val expected =
//            "\n" +
//                "error: expected ')' in position :{\n" +
//                "\tstartOffset: 22,\n" +
//                "\tendOffset: 23,\n" +
//                "\tstartLine: 1,\n" +
//                "\tendLine: 1,\n" +
//                "\tstartColumn: 22,\n" +
//                "\tendColumn: 23}"
//        assertEquals(expected, result)
//    }
//
//    @Test
//    fun `v1 - test 19 - huge file with errors`() {
//        val result = cliV1.validate(readFile("src/test/resources/hugeFileWErrors"))
//        val expected =
//            "\n" +
//                "error: wrong number of values and operators in position :{\n" +
//                "\tstartOffset: 19,\n" +
//                "\tendOffset: 20,\n" +
//                "\tstartLine: 3,\n" +
//                "\tendLine: 3,\n" +
//                "\tstartColumn: 19,\n" +
//                "\tendColumn: 20}\n" +
//                "error: wrong number of values and operators in position :{\n" +
//                "\tstartOffset: 12,\n" +
//                "\tendOffset: 13,\n" +
//                "\tstartLine: 9,\n" +
//                "\tendLine: 9,\n" +
//                "\tstartColumn: 12,\n" +
//                "\tendColumn: 13}\n" +
//                "error: expected ')' in position :{\n" +
//                "\tstartOffset: 22,\n" +
//                "\tendOffset: 23,\n" +
//                "\tstartLine: 11,\n" +
//                "\tendLine: 11,\n" +
//                "\tstartColumn: 22,\n" +
//                "\tendColumn: 23}"
//        assertEquals(expected, result)
//    }
//
//    @Test
//    fun `v1 - test 20 - huge file without errors`() {
//        val result = cliV1.validate(readFile("src/test/resources/hugeFile"))
//        val expected = "VALIDATION SUCCESSFUL"
//        assertEquals(expected, result)
//    }
//
//    @Test
//    fun `v1 - test 21 - empty file`() {
//        val result = cliV1.validate(readFile("src/test/resources/emptyFIle"))
//        val expected = "VALIDATION SUCCESSFUL"
//        assertEquals(expected, result)
//    }
}
