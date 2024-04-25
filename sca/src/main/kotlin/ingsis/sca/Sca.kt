package ingsis.sca
import ingsis.components.TokenType
import ingsis.components.statement.Statement
import ingsis.sca.result.InvalidResult
import ingsis.sca.scan.ScanIdentifierCase
import ingsis.sca.scan.ScanPrintLine
import ingsis.sca.scan.ScanReadInput
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
            "VERSION_2" ->
                Sca(
                    listOf(
                        ScanPrintLine(arrayListOf(TokenType.INTEGER, TokenType.STRING)),
                        ScanIdentifierCase(),
                        ScanReadInput(arrayListOf(TokenType.INTEGER, TokenType.STRING)),
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
        rules: ReadScaRulesFile,
    ): String {
        val result = StringBuilder()
        for (scan in scanners) {
            if (scan.canHandle(statement)) {
                when (val scanResult = scan.analyze(statement, rules)) {
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
