package ingsis.sca
import ingsis.components.TokenType
import ingsis.components.statement.Statement
import ingsis.sca.result.InvalidResult
import ingsis.sca.scan.ScanIdentifierCase
import ingsis.sca.scan.ScanPrintLine
import ingsis.sca.scan.ScanStatement
import ingsis.utils.ReadScaRulesFile

object PrintScriptSca {
    fun createSCA(version: String): Sca {
        return when (version) {
            "VERSION_1" ->
                Sca(
                    listOf(
                        ScanPrintLine(arrayListOf(TokenType.INTEGER, TokenType.STRING)),
                        ScanIdentifierCase(),
                    ),
                )
            else ->
                Sca(
                    listOf(
                        ScanPrintLine(arrayListOf(TokenType.INTEGER, TokenType.STRING)),
                        ScanIdentifierCase(),
                    ),
                )
        }
    }
}

class Sca(private val scanners: List<ScanStatement>) {
    fun getRules(): List<ScanStatement> {
        return scanners
    }

    fun analyze(
        statement: Statement,
        jsonPath: String,
    ): String {
        val result = StringBuilder()
        var jsonReader = ReadScaRulesFile()
        try {
            jsonReader.readSCARulesAndStackMap(jsonPath)
        } catch (
            e: Exception
        ) {
            println("Error reading the rules file")
        }
        for (scan in scanners) {
            if (scan.canHandle(statement)) {
                when (val scanResult = scan.analyze(statement, jsonReader)) {
                    is InvalidResult -> {
                        result.append(
                            "${scanResult.getMessage()} at line " +
                                "${scanResult.getPosition().startLine} and column ${scanResult.getPosition().startColumn}\n",
                        )
                    }
                }
            }
        }
        return result.toString()
    }
}
