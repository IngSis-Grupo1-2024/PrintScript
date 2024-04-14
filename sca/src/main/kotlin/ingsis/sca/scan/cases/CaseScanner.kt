package ingsis.sca.scan.cases

import ingsis.components.statement.Declaration
import ingsis.sca.result.Result

interface CaseScanner {
    fun checkIdentifierFormat(declaration: Declaration): Result
}
