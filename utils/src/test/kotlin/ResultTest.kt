import components.Position
import components.TokenType
import components.statement.Type
import ingsis.utils.Result
import org.junit.jupiter.api.Test

class ResultTest {


    @Test
    fun testVariableStringCreation(){
        val position = Position()
        val variable = Result(Type("string", position), "Hello")
        assert(variable.getValue() == "Hello")
        assert(variable.getType().getValue() == "string")
    }

    @Test
    fun testVariableNumberCreation(){
        val position = Position()
        val variable = Result(Type("integer", position), "4")
        assert(variable.getValue() == "4")
        assert(variable.getType().getValue() == "integer")
    }


}