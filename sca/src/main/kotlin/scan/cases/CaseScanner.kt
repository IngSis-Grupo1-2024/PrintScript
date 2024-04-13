package scan.cases

import components.statement.Declaration
import result.Result

interface CaseScanner {

    fun checkIdentifierFormat(declaration: Declaration): Result
}