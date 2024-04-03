import components.TokenType
import ingsis.utils.Variable
import org.junit.jupiter.api.Test

class VariableTest {


    @Test
    fun testVariableStringCreation(){
        val variable = Variable(TokenType.STRING, "Hello")
        assert(variable.getValue() == "Hello")
        assert(variable.getType() == TokenType.STRING)
    }

    @Test
    fun testVariableNumberCreation(){
        val variable = Variable(TokenType.INTEGER, "4")
        assert(variable.getValue() == "4")
        assert(variable.getType() == TokenType.INTEGER)
    }


}