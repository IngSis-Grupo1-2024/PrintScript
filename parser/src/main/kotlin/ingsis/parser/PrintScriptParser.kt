package ingsis.parser

import ingsis.parser.scan.*
import ingsis.parser.symbolType.*

object PrintScriptParser {
    fun createParser(version: String): Parser {
        return when (version) {
            "VERSION_1" -> Parser(getScannersOfV1(version), getSymbolChangersV1())
            "VERSION_2" -> Parser(getScannersOfV2(version), getSymbolChangersV2())
            else -> Parser(getScannersOfV1(version), getSymbolChangersV1())
        }
    }

    private fun getSymbolChangersV1(): List<SymbolChanger> =
        listOf(
            StringSymbolChanger(),
            DoubleSymbolChanger(),
            IntegerSymbolChanger(),
            IdentifierSymbolChanger(),
        )

    private fun getScannersOfV1(version: String) = listOf(scanDeclaration(version), scanAssignation(version), scanPrintLine(version))

    private fun getScannersOfV2(version: String): List<ScanStatement> =
        getScannersOfV1(version) + listOf(scanIf(version), scanElse(version))

    private fun scanElse(version: String) = PSScanElse.createScanElse(version)

    private fun scanPrintLine(version: String) = PSScanPrintLine.createPrintLine(version)

    private fun getSymbolChangersV2(): List<SymbolChanger> =
        listOf(
            StringSymbolChanger(),
            DoubleSymbolChanger(),
            IntegerSymbolChanger(),
            BooleanSymbolChanger(),
            IdentifierSymbolChanger(),
        )

    private fun scanDeclaration(version: String) = PSScanDeclaration.createScanDeclaration(version)

    private fun scanAssignation(version: String) = PSScanAssignation.createScanAssignation(version)

    private fun scanIf(version: String) = PSScanIf.createIf(version)
}