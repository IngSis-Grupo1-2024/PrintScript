package scan.cases


import components.statement.Declaration
import result.InvalidResult
import result.ValidResult
import result.Result

class ScanCamelCase : CaseScanner {

    override fun checkIdentifierFormat(declaration: Declaration): Result {
        return if (!declaration.getVariable().getName().matches(Regex("[a-z][a-zA-Z0-9]*"))) {
            InvalidResult(
                declaration.getPosition(),
                "Variable name ${declaration.getVariable().getName()} is not in camelCase format"
            )
        } else ValidResult()

    }

}