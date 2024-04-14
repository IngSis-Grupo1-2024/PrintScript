package ingsis.sca
import ingsis.components.TokenType
import ingsis.components.statement.Statement
import ingsis.sca.result.InvalidResult
import ingsis.sca.scan.ScanIdentifierCase
import ingsis.sca.scan.ScanPrintLine
import ingsis.sca.scan.ScanStatement
import ingsis.utils.ReadScaRulesFile
import java.io.FileWriter

object PrintScriptSca{
    fun createSCA(version: String): Sca{
        return when (version) {
            "VERSION_1" -> Sca(listOf(
                ScanPrintLine(arrayListOf(TokenType.INTEGER, TokenType.STRING)),
                ScanIdentifierCase()))
            else -> Sca(listOf(
                ScanPrintLine(arrayListOf(TokenType.INTEGER, TokenType.STRING)),
                ScanIdentifierCase()))
        }
    }
}
class Sca(private val scanners: List<ScanStatement>) {
    fun getRules(): List<ScanStatement> {
        return scanners
    }

    fun analyze(
        statement: Statement,
        reportFileName: String,
    ) {
        val fileWriter = FileWriter(reportFileName, true)
        val jsonReader = ReadScaRulesFile("src/main/kotlin/ingsis/sca/rules/rules.json")
        for (scan in scanners) {
            if (scan.canHandle(statement)) {
                when (val result = scan.analyze(statement, jsonReader)) {
                    is InvalidResult -> {
                        fileWriter.write(
                            "${result.getMessage()} at line " +
                                "${result.getPosition().startLine} and column ${result.getPosition().startColumn}\n",
                        )
                    }
                }
            }
        }
        fileWriter.close()
    }
}
