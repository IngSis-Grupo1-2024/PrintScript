
import components.statement.Statement
import result.InvalidResult
import result.Result
import result.ValidResult
import scan.ScanStatement

class Sca(private val scanners: List<ScanStatement>) : ScaInterface {
    fun getRules(): List<ScanStatement> {
        return scanners
    }

    override fun analyze(statement: Statement): Result {
        for (scan in scanners) {
            if (scan.canHandle(statement)) {
                when (val result = scan.analyze(statement)) {
                    is InvalidResult -> {
                        // We would have to raise an error
                        println(result.getMessage() + "at" + result.getPosition())
                    }
                }
            }
        }
        return ValidResult()
    }
}
