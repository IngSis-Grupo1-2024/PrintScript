
import components.statement.Statement
import result.Result

interface ScaInterface {
    fun analyze(statement: Statement): Result
}
