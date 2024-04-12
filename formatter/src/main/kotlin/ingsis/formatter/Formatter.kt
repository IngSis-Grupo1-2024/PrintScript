package ingsis.formatter
import components.statement.Statement
import scan.ScanStatement
import utils.readJsonAndStackMap
import java.io.FileWriter

class Formatter(private val scanners: List<ScanStatement>) {
    fun format(
        statement: Statement,
        file: String,
    ) {
        val ruleMap = readJsonAndStackMap("src/main/rules/rules.json")
        val fileWriter = FileWriter(file, true)
        for (scanner in scanners) {
            if (scanner.canHandle(statement)) {
                fileWriter.write(scanner.format(statement, ruleMap))
            }
        }
        fileWriter.close()
    }
}
