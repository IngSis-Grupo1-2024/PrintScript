import components.statement.Statement
import ingsis.utils.ReadScaRulesFile
import result.InvalidResult
import scan.ScanStatement
import java.io.File
import java.io.FileWriter

class Sca(private val scanners: List<ScanStatement>) : ScaInterface {
    fun getRules(): List<ScanStatement> {
        return scanners
    }

    override fun analyze(statement: Statement) {
        val fileWriter = FileWriter("src/main/kotlin/rules/scaScanResult.txt", true)
        val jsonReader = ReadScaRulesFile("src/main/kotlin/rules/rules.json")
        for (scan in scanners) {
            if (scan.canHandle(statement)) {
                when (val result = scan.analyze(statement, jsonReader)) {
                    is InvalidResult -> {
                        fileWriter.write("${result.getMessage()} at line " +
                                "${result.getPosition().startLine} and column ${result.getPosition().startColumn}\n")
                    }
                }
            }
        }
        fileWriter.close()
    }
}
