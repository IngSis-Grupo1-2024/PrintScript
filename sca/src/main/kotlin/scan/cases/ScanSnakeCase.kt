package scan.cases

import components.statement.Declaration
import result.InvalidResult
import result.Result
import result.ValidResult

class ScanSnakeCase : CaseScanner {
    override fun checkIdentifierFormat(declaration: Declaration): Result {
        return if (!declaration.getVariable().getName().matches(Regex("[a-z]+(?:_[a-z0-9]+)*$"))) {
            InvalidResult(
                declaration.getPosition(),
                "Variable name ${declaration.getVariable().getName()} is not in snake_case format"
            )
        } else ValidResult()
    }
}